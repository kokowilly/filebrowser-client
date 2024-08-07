package id.kokowilly.filebrowser.connection.editor

import id.kokowilly.filebrowser.connection.database.dao.ConnectionDao
import id.kokowilly.filebrowser.connection.database.entity.Connection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal interface EditConnectionRepository {
  suspend fun getConnection(id: Int): Connection

  suspend fun saveConnection(connection: Connection)
}

internal class EditConnectionRepositoryImpl(
  private val dao: ConnectionDao,
  private val dispatcher: CoroutineDispatcher
) : EditConnectionRepository {
  override suspend fun getConnection(id: Int): Connection = withContext(dispatcher) {
    dao.getById(id)
  }

  override suspend fun saveConnection(connection: Connection) = withContext(dispatcher) {
    if (connection.id == 0) {
      dao.insert(connection)
    } else {
      dao.update(connection)
    }
  }
}