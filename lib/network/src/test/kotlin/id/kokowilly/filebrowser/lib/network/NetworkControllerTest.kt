package id.kokowilly.filebrowser.lib.network

import id.kokowilly.test.common.shouldBe
import id.kokowilly.test.common.shouldNot
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.FileInputStream
import java.util.*

class NetworkControllerTest : KoinComponent {

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
    startKoin {
      modules(libNetworkModule)
    }
    loadProperties()

    factory = NetworkControllerImpl(
      OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor {
          println(it)
        }.apply {
          level = BODY
        })
        .build(),
      get()
    ).apply {
      initialize(serverBaseUrl)
    }

    service = factory.build(LoginService::class.java)
  }

  @After
  fun tearDown() {
    stopKoin()
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
  fun `call login, should return new accesstoken, when fake token is assigned, should refresh token`() =
    runTest {
      val token = service.login(
        mapOf(
          "username" to serverUser,
          "password" to serverPassword
        )
      )

      token shouldNot null
      var tokenRefreshed = 0
      factory.setRefreshToken {
        tokenRefreshed++
        token
      }

      factory.setAccessToken("fake token")

      service.renew()

      tokenRefreshed shouldBe 1
    }


  interface LoginService {
    @POST("/api/renew")
    suspend fun renew(): String

    @POST("/api/login")
    suspend fun login(@Body payload: Map<String, String>): String
  }
}
