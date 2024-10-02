package id.kokowilly.filebrowser.feature.browse.target

import androidx.lifecycle.viewModelScope
import id.kokowilly.filebrowser.feature.browse.browse.BrowseViewModel
import id.kokowilly.filebrowser.feature.browse.browse.ResourceRepository
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

  fun move() {
    viewModelScope.launch {
      val original = originalFile.value
      runCatching {
        actionRepository.move(
          original.absolutePath,
          "${path.value}/${original.name}"
        )
      }.onSuccess {
        stateCommand.emit(Command.Success)
      }.onFailure {
        stateCommand.emit(Command.Error)
      }
    }
  }

  fun initialize(source: File) {
    stateOriginalFile = MutableStateFlow(source)
    go(source.parent.orEmpty())
  }

  internal sealed interface Command {
    data object None : Command
    data object Success : Command
    data object Error : Command
  }
}

