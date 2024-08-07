package id.kokowilly.filebrowser.connection.editor

import id.kokowilly.filebrowser.connection.database.entity.Connection
import id.kokowilly.filebrowser.lib.network.NetworkController
import id.kokowilly.filebrowser.lib.network.api.AuthService
import id.kokowilly.filebrowser.lib.network.api.LoginRequest
import id.kokowilly.filebrowser.lib.network.build
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal interface NetworkTestRepository {
  suspend fun test(connection: Connection): Boolean
}

internal class NetworkTestRepositoryImpl(
  private val networkController: NetworkController,
  private val regex: Regex,
  private val dispatcher: CoroutineDispatcher
) : NetworkTestRepository {
  override suspend fun test(connection: Connection): Boolean = withContext(dispatcher) {
    listOf(
      connection.primaryUrl,
      connection.secondaryUrl
    ).filter { it.isNotEmpty() }
      .map { it to testNetwork(it, connection.username, connection.password) }
      .onEach {
        require(it.second) {
          "Connection to ${it.first} is failed"
        }
      }
      .none { !it.second }
  }

  private suspend fun testNetwork(
    url: String,
    username: String,
    password: String
  ): Boolean {
    networkController.initialize(url)

    val authService: AuthService = networkController.build()

    return regex.matches(
      authService.login(LoginRequest(username, password))
    )
  }
}
