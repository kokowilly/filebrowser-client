package id.kokowilly.filebrowser.feature.browse

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

internal class BrowseNotificationChannel {

  private val _command = MutableSharedFlow<Command>(
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST,
  )
  val command: SharedFlow<Command> get() = _command

  fun emit(command: Command) = _command.tryEmit(command).also { println("emit $it") }

  sealed interface Command {
    class Invalidate(val path: String) : Command
  }
}
