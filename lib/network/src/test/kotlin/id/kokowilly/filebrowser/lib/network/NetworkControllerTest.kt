package id.kokowilly.filebrowser.lib.network

import id.kokowilly.test.common.shouldNot
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import org.junit.Before
import org.junit.Test
import retrofit2.Call
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
        .addInterceptor(HttpLoggingInterceptor {
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
  fun `call login, should return new accesstoken`() {
    val call = service.login(
      mapOf(
        "username" to serverUser,
        "password" to serverPassword
      )
    )
    call.execute().raw().message shouldNot null
  }

  interface LoginService {
    @POST("/api/renew")
    suspend fun renew(): Map<String, String>

    @POST("/api/login")
    fun login(@Body payload: Map<String, String>): Call<String>
  }
}
