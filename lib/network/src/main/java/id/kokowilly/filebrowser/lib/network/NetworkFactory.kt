package id.kokowilly.filebrowser.lib.network

import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

interface NetworkFactory {
  fun <T> build(service: Class<T>): T
}

interface NetworkController : NetworkFactory {
  fun initialize(baseUrl: String)

  fun setRefreshToken(refreshToken: (String) -> String)

  fun setAccessToken(accessToken: String)
}

class NetworkControllerImpl(
  private val okhttp: OkHttpClient,
  private val moshi: Moshi
) : NetworkController {
  private lateinit var retrofit: Retrofit

  private lateinit var refreshToken: (String) -> String

  override fun setRefreshToken(refreshToken: (String) -> String) {
    this.refreshToken = refreshToken
  }

  private var accessToken: String = ""

  private val interceptor = Interceptor { chain ->
    val request = chain.request().newBuilder()
      .header("Cookie", "auth=$accessToken")
      .header("X-Auth", accessToken)
      .build()
    chain.proceed(request)
  }

  override fun initialize(baseUrl: String) {
    retrofit = Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(
        okhttp.newBuilder()
          .addInterceptor(interceptor)
          .build()
      )
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()
  }

  override fun setAccessToken(accessToken: String) {
    this.accessToken = accessToken
  }

  override fun <T> build(service: Class<T>): T {
    return retrofit.create(service)
  }
}
