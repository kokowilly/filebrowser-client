package id.kokowilly.filebrowser

import android.os.Build
import android.util.Log
import androidx.multidex.MultiDexApplication
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import id.kokowilly.filebrowser.connection.featureConnectionModule
import id.kokowilly.filebrowser.feature.browse.featureBrowseLibrary
import id.kokowilly.filebrowser.feature.browse.featureBrowseModule
import id.kokowilly.filebrowser.lib.navigation.Navigation
import id.kokowilly.filebrowser.lib.navigation.NavigationLibrary
import id.kokowilly.filebrowser.lib.navigation.alias
import id.kokowilly.filebrowser.lib.network.NetworkController
import id.kokowilly.filebrowser.lib.network.libNetworkModule
import id.kokowilly.filebrowser.log.Logger
import id.kokowilly.filebrowser.log.Logger.LogWriter
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FileBrowserApp : MultiDexApplication() {
  override fun onCreate() {
    super.onCreate()

    configureLog()
    configureNavigation()

    startKoin {
      androidContext(this@FileBrowserApp)
      modules(
        libNetworkModule,
        featureConnectionModule,
        featureBrowseModule,
      )
    }

    get<NetworkController>().requestInjectNetworkClient { okhttp ->
      Coil.setImageLoader(
        ImageLoader.Builder(this@FileBrowserApp)
          .okHttpClient(okhttp)
          .components {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
              add(ImageDecoderDecoder.Factory())
            }
            add(SvgDecoder.Factory())
            add(GifDecoder.Factory())
          }
          .diskCachePolicy(CachePolicy.ENABLED)
          .memoryCachePolicy(CachePolicy.ENABLED)
          .build()
      )
    }
  }

  private fun configureNavigation() {
    listOf(
      aliasRegistry,
      featureBrowseLibrary,
    ).forEach {
      it.initialize()
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

private val aliasRegistry
  get() = NavigationLibrary {
    Navigation.register("connection:success", alias("browse:list"))
  }
