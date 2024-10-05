package id.kokowilly.filebrowser.feature.browse.target

import androidx.lifecycle.viewModelScope
import id.kokowilly.filebrowser.feature.browse.BrowseNotificationChannel
import id.kokowilly.filebrowser.feature.browse.browse.BrowseViewModel
import id.kokowilly.filebrowser.feature.browse.browse.ResourceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

internal class BrowseTargetViewModel(
  resourceRepository: ResourceRepository,
  private val actionRepository: ActionRepository,
  private val notificationChannel: BrowseNotificationChannel,
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
          "${path.value.path}/${original.name}"
        )
      }.onSuccess {
        stateCommand.emit(Command.Success)
        notificationChannel.emit(
          BrowseNotificationChannel.Command.Invalidate(original.parent.orEmpty())
        )
        notificationChannel.emit(
          BrowseNotificationChannel.Command.Invalidate(path.value.path)
        )
      }.onFailure {
        stateCommand.emit(Command.Error)
      }
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
    data object Error : Command
  }
}
