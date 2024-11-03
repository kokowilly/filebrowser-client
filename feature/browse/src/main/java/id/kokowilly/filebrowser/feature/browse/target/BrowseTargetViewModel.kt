package id.kokowilly.filebrowser.feature.browse.target

import androidx.lifecycle.viewModelScope
import id.kokowilly.filebrowser.feature.browse.browse.BrowseViewModel
import id.kokowilly.filebrowser.feature.browse.browse.ResourceRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

internal class BrowseTargetViewModel(
  resourceRepository: ResourceRepository,
  private val actionRepository: ActionRepository,
) : BrowseViewModel(resourceRepository) {
  private lateinit var stateOriginalFile: StateFlow<File>
  val originalFile: StateFlow<File> get() = stateOriginalFile

  private val stateCommand = MutableStateFlow<Command>(Command.None)
  val command: StateFlow<Command> get() = stateCommand

  fun copy() {
    viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
      viewModelScope.launch {
        stateCommand.emit(Command.Error(throwable))
      }
    }) {
      val original = originalFile.value
      actionRepository.copy(
        original.absolutePath,
        "${path.value.path}/${original.name}"
      )
      stateCommand.emit(Command.Success)
    }
  }

  fun move() {
    viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
      viewModelScope.launch {
        stateCommand.emit(Command.Error(throwable))
      }
    }) {
      val original = originalFile.value
      actionRepository.move(
        original.absolutePath,
        "${path.value.path}/${original.name}"
      )
      stateCommand.emit(Command.Success)
    }
  }

  fun initialize(source: File) {
    stateOriginalFile = MutableStateFlow(source)
    go(
      PathRequest(
        source.parent.orEmpty(),
        PathRequest.Origin.SYSTEM
      )
    )
  }

  internal sealed interface Command {
    data object None : Command
    data object Success : Command
    class Error(val exception: Throwable) : Command
  }
}
