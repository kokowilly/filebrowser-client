package id.kokowilly.filebrowser.lib.network

import id.kokowilly.test.common.shouldNot
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.http.POST
import java.io.FileInputStream
import java.util.Properties

class RetrofitFactoryTest {

    private val serverBaseUrl: String by lazy {
        var url = ""
        Properties().also {
            runCatching {
                it.load(FileInputStream("../../local.properties"))
                url = it.getProperty("integrationUrl")
            }.onFailure {
                it.printStackTrace()
            }
        }
        url.also {
            println(it)
        }
    }

    private lateinit var factory: RetrofitFactory

    @Before
    fun setUp() {
        factory = RetrofitFactory()

        factory.initialize(serverBaseUrl)
    }

    @Test
    fun `call renew, when in clean state, should returns 401`() {
        val service: LoginService = factory.build(LoginService::class.java)
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

    interface LoginService {
        @POST("/api/renew")
        suspend fun renew(): Map<String, String>
    }
}
