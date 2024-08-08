package id.kokowilly.filebrowser

import android.util.Log
import androidx.multidex.MultiDexApplication
import id.kokowilly.filebrowser.connection.featureConnectionModule
import id.kokowilly.filebrowser.lib.network.libNetworkModule
import id.kokowilly.filebrowser.log.Logger
import id.kokowilly.filebrowser.log.Logger.LogWriter
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FileBrowserApp : MultiDexApplication() {
  override fun onCreate() {
    super.onCreate()

    configureLog()

    startKoin {
      androidContext(this@FileBrowserApp)
      modules(
        libNetworkModule,
        featureConnectionModule,
      )
    }
  }

  private fun configureLog() {
    Logger.logWriter = object : LogWriter {
      override fun i(tag: String, message: String) {
        Log.i(tag, message)
      }

      override fun d(tag: String, message: String, throwable: Throwable?) {
        Log.d(tag, message, throwable)
      }

      override fun e(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
      }
    }
  }
}
