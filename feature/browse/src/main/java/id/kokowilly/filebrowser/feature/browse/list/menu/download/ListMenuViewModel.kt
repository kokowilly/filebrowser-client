package id.kokowilly.filebrowser.feature.browse.list.menu.download

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.kokowilly.filebrowser.lib.network.NetworkController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.io.File

class ListMenuViewModel(
  private val networkController: NetworkController,
) : ViewModel() {

  private val _command = MutableSharedFlow<Command>()
  val command: Flow<Command> = _command

  fun startDownload(path: String) {
    viewModelScope.launch {
      _command.emit(
        Command.Download(
          file = File(path),
          url = Uri.parse(makeUrl(path)),
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
    class Download(val file: File, val url: Uri) : Command
  }
}
