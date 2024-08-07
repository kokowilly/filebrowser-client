package id.kokowilly.filebrowser

import androidx.multidex.MultiDexApplication
import id.kokowilly.filebrowser.connection.featureConnectionModule
import id.kokowilly.filebrowser.lib.network.libNetworkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FileBrowserApp : MultiDexApplication() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@FileBrowserApp)
      modules(
        libNetworkModule,
        featureConnectionModule,
      )
    }
  }
}
