package id.kokowilly.filebrowser.lib.network

import id.kokowilly.test.common.shouldNot
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.FileInputStream
import java.util.Properties

class NetworkControllerTest {

  private lateinit var serverBaseUrl: String
  private lateinit var serverUser: String
  private lateinit var serverPassword: String

  private fun loadProperties() {
    Properties().also {
      runCatching {
        it.load(FileInputStream("../../local.properties"))
        serverBaseUrl = it.getProperty("integrationUrl")
        serverUser = it.getProperty("integrationUser")
        serverPassword = it.getProperty("integrationPassword")
      }.onFailure {
        it.printStackTrace()
      }
    }
  }

  private lateinit var factory: NetworkController

  private lateinit var service: LoginService

  @Before
  fun setUp() {
    loadProperties()

    factory = NetworkControllerImpl(
      OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor {
          println(it)
        }.apply {
          level = BODY
        })
        .build(),
      NetworkModule.moshi()
    ).apply {
      initialize(serverBaseUrl)
    }

    service = factory.build(LoginService::class.java)
  }

  @Test
  fun `call renew, when in clean state, should returns 401`() {
    var exception: Throwable? = null

    runTest {
      runCatching {
        service.renew()
      }.onFailure {
        exception = it
      }
    }

    exception shouldNot null
  }

  @Test
  fun `call login, should return new accesstoken, and able to renew`() = runTest {
    val token = service.login(
      mapOf(
        "username" to serverUser,
        "password" to serverPassword
      )
    )

    token shouldNot null

    val refreshToken = factory.build(RefreshTokenService::class.java)
    factory.setRefreshToken {
      refreshToken.renew().raw().message
    }
    factory.setAccessToken(token)

    service.renew()
  }

  interface RefreshTokenService {
    @POST("/api/renew")
    fun renew(): Response<String>
  }

  interface LoginService {
    @POST("/api/renew")
    suspend fun renew(): String

    @POST("/api/login")
    suspend fun login(@Body payload: Map<String, String>): String
  }
}
