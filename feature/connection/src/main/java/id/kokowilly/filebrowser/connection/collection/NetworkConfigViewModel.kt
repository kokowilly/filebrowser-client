package id.kokowilly.filebrowser.connection.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.kokowilly.filebrowser.connection.database.entity.Connection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class NetworkConfigViewModel(
  private val showNetworkRepository: ShowNetworkRepository,
  private val dispatcher: CoroutineDispatcher
) : ViewModel() {
  val allConnections by lazy {
    showNetworkRepository.getAllConnection()
      .map { connections ->
        connections.map { connection ->
          ConnectionData(connection)
        }
      }.flowOn(dispatcher)
  }

  fun remove(id: Int) = viewModelScope.launch(dispatcher) {
    showNetworkRepository.remove(id)
  }
}

internal data class ConnectionData(
  val id: Int,
  val name: String
) {
  constructor(connection: Connection) : this(connection.id, connection.name)
}
