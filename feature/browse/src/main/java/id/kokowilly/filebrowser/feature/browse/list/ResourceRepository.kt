package id.kokowilly.filebrowser.feature.browse.list

import id.kokowilly.filebrowser.lib.network.api.DataService
import id.kokowilly.filebrowser.lib.network.api.UsageResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal interface ResourceRepository {
  suspend fun getUsage(): UsageResponse
}

internal class ResourceRepositoryImpl(
  private val dataService: DataService,
  private val dispatcher: CoroutineDispatcher
) : ResourceRepository {
  override suspend fun getUsage(): UsageResponse = withContext(dispatcher) {
    dataService.usage()
  }
}