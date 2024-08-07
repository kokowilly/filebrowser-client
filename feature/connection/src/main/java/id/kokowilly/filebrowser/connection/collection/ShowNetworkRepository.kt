package id.kokowilly.filebrowser.connection.collection

import id.kokowilly.filebrowser.connection.database.dao.ConnectionDao
import id.kokowilly.filebrowser.connection.database.entity.Connection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

internal interface ShowNetworkRepository {
  fun getAllConnection(): Flow<List<Connection>>

  suspend fun remove(id: Int)
}

internal class ShowNetworkRepositoryImpl(
  private val connectionDao: ConnectionDao,
  private val dispatcher: CoroutineDispatcher
) : ShowNetworkRepository {
  override fun getAllConnection(): Flow<List<Connection>> = connectionDao.getAll()
    .flowOn(dispatcher)

  override suspend fun remove(id: Int) = withContext(dispatcher){
    connectionDao.delete(id)
  }
}
