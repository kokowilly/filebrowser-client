package id.kokowilly.filebrowser.feature.browse.browse.input

import androidx.lifecycle.viewModelScope
import id.kokowilly.filebrowser.feature.browse.browse.BrowseViewModel
import id.kokowilly.filebrowser.feature.browse.browse.ResourceRepository
import id.kokowilly.filebrowser.feature.browse.target.ActionRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

internal class NameInputViewModel(
  resourceRepository: ResourceRepository,
  private val actionRepository: ActionRepository,
) : BrowseViewModel(
  "NameInputViewModel",
  resourceRepository,
) {
  private lateinit var stateOriginalFile: StateFlow<File>
  val originalFile: StateFlow<File> get() = stateOriginalFile

  private val stateCommand = MutableStateFlow<Command>(Command.None)
  val command: StateFlow<Command> get() = stateCommand

  fun rename(newName: String) {
    viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
      viewModelScope.launch {
        stateCommand.emit(Command.Error(throwable))
      }
    }) {
      val original = originalFile.value
      actionRepository.move(
        from = original.absolutePath,
        to = "${original.parentFile?.absolutePath.orEmpty()}/$newName",
      )
      stateCommand.emit(Command.Success)
    }
  }

  fun initialize(source: File) {
    stateOriginalFile = MutableStateFlow(source)
    go(
      PathRequest(
        source.parent.orEmpty(),
        PathRequest.Origin.System
      )
    )
  }

  internal sealed interface Command {
    data object None : Command
    data object Success : Command
    class Error(val exception: Throwable) : Command
  }
}
