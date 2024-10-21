package id.kokowilly.filebrowser.feature.browse.browse.menu

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.kokowilly.filebrowser.lib.network.NetworkController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.io.File

class ItemOptionViewModel(
  private val networkController: NetworkController,
) : ViewModel() {

  private val _command = MutableSharedFlow<Command>()
  val command: Flow<Command> = _command

  fun startDownload(path: String) {
    viewModelScope.launch {
      _command.emit(
        Command.Download(
          filename = File(path).name,
          url = Uri.parse(makeUrl(path)),
        )
      )
    }
  }

  fun startMove(path: String) {
    viewModelScope.launch {
      _command.emit(
        Command.Move(
          filePath = path,
        )
      )
    }
  }

  fun startCopy(path: String) {
    viewModelScope.launch {
      _command.emit(
        Command.Copy(
          filePath = path,
        )
      )
    }
  }

  fun startRename(path: String) {
    viewModelScope.launch {
      _command.emit(
        Command.Rename(
          filePath = path,
        )
      )
    }
  }

  private fun makeUrl(path: String): String {
    return networkController.baseUrl +
      "api/raw/" +
      path +
      "?auth=" +
      networkController.accessToken
  }

  sealed interface Command {
    class Download(val filename: String, val url: Uri) : Command

    class Move(val filePath: String) : Command

    class Copy(val filePath: String) : Command

    class Rename(val filePath: String) : Command
  }
}
