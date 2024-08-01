package id.kokowilly.filebrowser.lib.network

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

interface NetworkFactory {
  fun <T> build(service: Class<T>): T
}

interface NetworkController : NetworkFactory {
  fun initialize(baseUrl: String)

  fun setAccessToken(accessToken: String)
}

class NetworkControllerImpl(
  private val okhttp: OkHttpClient,
  private val moshi: Moshi
) : NetworkController {
  private lateinit var retrofit: Retrofit

  override fun initialize(baseUrl: String) {
    retrofit = Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(okhttp)
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()
  }

  override fun setAccessToken(accessToken: String) {
    TODO("Not yet implemented")
  }

  override fun <T> build(service: Class<T>): T {
    return retrofit.create(service)
  }
}
