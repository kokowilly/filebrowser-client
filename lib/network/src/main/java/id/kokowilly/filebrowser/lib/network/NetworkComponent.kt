package id.kokowilly.filebrowser.lib.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import id.kokowilly.filebrowser.lib.network.api.DataService
import id.kokowilly.filebrowser.log.i
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import org.koin.dsl.module

val libNetworkModule = module {
  single { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

  single<NetworkController> {
    NetworkControllerImpl(
      OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor {
          i(it)
        }.apply {
          level = BODY
        })
        .build(),
      get()
    )
  }

  factory<NetworkFactory> { get<NetworkController>() }

  factory<DataService> { get<NetworkFactory>().build() }
}
