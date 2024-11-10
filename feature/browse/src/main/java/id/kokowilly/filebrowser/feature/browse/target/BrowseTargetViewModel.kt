package id.kokowilly.filebrowser.feature.browse.target

import androidx.lifecycle.viewModelScope
import id.kokowilly.filebrowser.feature.browse.browse.BrowseViewModel
import id.kokowilly.filebrowser.feature.browse.browse.ResourceRepository
import id.kokowilly.filebrowser.foundation.logics.cleanPath
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

internal class BrowseTargetViewModel(
  resourceRepository: ResourceRepository,
  private val actionRepository: ActionRepository,
) : BrowseViewModel(
  "BrowseTargetViewModel",
  resourceRepository,
) {
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
      val (from, to) = originalFile.value.let { original ->
        original.absolutePath to "${path.value.path}/${original.name}".cleanPath()
      }
      tag.i("copy $from to $to")
      actionRepository.copy(from, to)
      stateCommand.emit(Command.Success)
    }
  }

  fun move() {
    viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
      viewModelScope.launch {
        stateCommand.emit(Command.Error(throwable))
      }
    }) {
      val (from, to) = originalFile.value.let { original ->
        original.absolutePath to "${path.value.path}/${original.name}".cleanPath()
      }
      tag.i("move $from to $to")
      actionRepository.move(from, to)
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
