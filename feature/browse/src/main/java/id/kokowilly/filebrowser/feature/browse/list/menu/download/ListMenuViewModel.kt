package id.kokowilly.filebrowser.feature.browse.list.menu.download

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import id.kokowilly.filebrowser.lib.network.NetworkController
import java.io.File

class ListMenuViewModel(
  private val networkController: NetworkController,
) : ViewModel() {

  fun startDownload(context: Context, path: String) {
    val file = File(path)
    val downloadUrl = makeUrl(path)

    val request = DownloadManager.Request(Uri.parse(downloadUrl))
      .setTitle("File Download")
      .setDescription("Downloading...")
      .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
      .setAllowedOverMetered(true)  // Allow mobile data downloads
      .setAllowedOverRoaming(true)  // Allow roaming downloads
      .setDestinationInExternalPublicDir(
        Environment.DIRECTORY_DOWNLOADS,
        file.name
      ) // Change filename and extension

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)
  }

  private fun makeUrl(path: String): String {
    return networkController.baseUrl +
      "api/raw/" +
      path +
      "?auth=" +
      networkController.accessToken
  }
}
