package id.kokowilly.filebrowser.lib.network

import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.HttpURLConnection

typealias Callback = (OkHttpClient) -> Unit

interface NetworkFactory {
  fun <T> build(service: Class<T>): T
}

inline fun <reified T : Any> NetworkFactory.build(): T = build(T::class.java)

interface NetworkController : NetworkFactory {
  fun initialize(baseUrl: String)

  fun requestInjectNetworkClient(callback: Callback)

  fun setRefreshToken(refreshToken: () -> String)

  fun setAccessToken(accessToken: String)

  val baseUrl: String

  val accessToken: String
}

class NetworkControllerImpl(
  okhttp: OkHttpClient,
  private val moshi: Moshi,
) : NetworkController {
  private lateinit var retrofit: Retrofit

  private lateinit var refreshToken: () -> String

  override fun setRefreshToken(refreshToken: () -> String) {
    this.refreshToken = refreshToken
  }

  override var accessToken: String = ""
    private set

  private val interceptor = object : Interceptor {
    private fun execute(chain: Chain): Response {
      val request = chain.request().newBuilder()
        .header("Cookie", "auth=$accessToken")
        .header("X-Auth", accessToken)
        .build()

      return chain.proceed(request)
    }

    override fun intercept(chain: Chain): Response {
      val response = execute(chain)

      return if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
        accessToken = refreshToken.invoke()

        execute(chain)
      } else {
        response
      }
    }
  }

  private val internalOkHttpClient = okhttp.newBuilder()
    .addInterceptor(interceptor)
    .build()

  //TODO: refactor
  override fun requestInjectNetworkClient(callback: Callback) {
    callback.invoke(internalOkHttpClient)
  }

  override val baseUrl: String get() = retrofit.baseUrl().toString()

  override fun initialize(baseUrl: String) {
    retrofit = Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(internalOkHttpClient)
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
