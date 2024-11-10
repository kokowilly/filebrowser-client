package id.kokowilly.filebrowser.feature.browse

import id.kokowilly.filebrowser.log.Tag
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

internal class BrowseNotificationChannel {

  private val tag = Tag("NotificationChannel")

  private val _command = MutableSharedFlow<Command>(
    extraBufferCapacity = 2,
    onBufferOverflow = BufferOverflow.DROP_OLDEST,
  )
  val command: SharedFlow<Command> get() = _command

  suspend fun emit(command: Command) = _command.emit(command)
    .also { tag.d("emit: $command") }

  sealed interface Command {
    data class Invalidate(val path: String) : Command
  }
}
