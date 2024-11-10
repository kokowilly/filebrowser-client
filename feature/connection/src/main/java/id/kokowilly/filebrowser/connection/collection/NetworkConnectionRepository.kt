package id.kokowilly.filebrowser.connection.collection

import id.kokowilly.filebrowser.connection.database.dao.ConnectionDao
import id.kokowilly.filebrowser.connection.database.entity.Connection
import id.kokowilly.filebrowser.connection.errors.ConnectionException
import id.kokowilly.filebrowser.lib.network.NetworkController
import id.kokowilly.filebrowser.lib.network.api.AuthService
import id.kokowilly.filebrowser.lib.network.api.LoginRequest
import id.kokowilly.filebrowser.lib.network.build
import id.kokowilly.filebrowser.log.Tag
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal interface NetworkConnectionRepository {
  suspend fun initializeConnection(connectionId: Int)
}

internal class NetworkConnectionRepositoryImpl(
  private val connectionDao: ConnectionDao,
  private val networkController: NetworkController,
  private val regex: Regex,
  private val dispatcher: CoroutineDispatcher,
) : NetworkConnectionRepository {
  private val tag = Tag("NetworkConnectionRepository")

  override suspend fun initializeConnection(connectionId: Int) = withContext(dispatcher) {
    val connection = connectionDao.getById(connectionId)

    test(connection)
  }

  private suspend fun test(connection: Connection) = withContext(dispatcher) {
    var cause: Throwable? = null

    listOf(connection.primaryUrl, connection.secondaryUrl)
      .filterNot { it.isEmpty() }
      .onEach { url ->
        runCatching { testNetwork(url, connection.username, connection.password) }
          .onFailure {
            cause = it
            tag.e("could not connect to $url", it)
          }
          .onSuccess {
            networkController.setAccessToken(it)
            return@withContext
          }
      }

    throw ConnectionException(connection, cause)
  }

  private suspend fun testNetwork(
    url: String,
    username: String,
    password: String,
  ): String {
    networkController.initialize(url)

    val authService: AuthService = networkController.build()

    val token = authService.login(LoginRequest(username, password))
    require(regex.matches(token)) {}
    return token
  }
}
