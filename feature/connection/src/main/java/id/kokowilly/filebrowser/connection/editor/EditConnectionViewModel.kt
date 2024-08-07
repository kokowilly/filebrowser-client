package id.kokowilly.filebrowser.connection.editor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.kokowilly.filebrowser.connection.database.entity.Connection
import id.kokowilly.filebrowser.connection.database.entity.Connection.Companion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private const val TAG = "EditConnection"

internal class EditConnectionViewModel(
  private val connectionRepository: EditConnectionRepository,
  private val networkTestRepository: NetworkTestRepository,
) : ViewModel() {
  private val stateConnection: MutableStateFlow<Connection> =
    MutableStateFlow(Connection.empty)
  val connection: Flow<Connection> get() = stateConnection

  private val stateConnectionValidation: MutableStateFlow<Boolean> =
    MutableStateFlow(false)
  val connectionValidation: Flow<Boolean> get() = stateConnectionValidation

  private val stateError: MutableSharedFlow<Throwable> = MutableSharedFlow()
  val error: Flow<Throwable> get() = stateError

  fun load(id: Int) {
    viewModelScope.launch {
      stateConnection.value = runCatching { connectionRepository.getConnection(id) }
        .getOrDefault(Companion.empty)
    }
  }

  fun save(connection: Connection) {
    viewModelScope.launch {
      connectionRepository.saveConnection(connection)
    }
  }

  fun invalidate() {
    stateConnectionValidation.value = false
  }

  fun test(connection: Connection) {
    viewModelScope.launch {
      runCatching {
        networkTestRepository.test(connection)
      }
        .onFailure {
          Log.d(TAG, "Error on test network", it)
          stateError.emit(it)
        }
        .getOrDefault(false)
        .also { stateConnectionValidation.value = it }
    }
  }
}
