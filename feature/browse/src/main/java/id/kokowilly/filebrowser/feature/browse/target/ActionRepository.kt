package id.kokowilly.filebrowser.feature.browse.target

import id.kokowilly.filebrowser.lib.network.api.FileModificationService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal interface ActionRepository {
  suspend fun move(from: String, to: String)
}

internal class ActionRepositoryImpl(
  private val fileModificationService: FileModificationService,
  private val dispatcher: CoroutineDispatcher,
) : ActionRepository {
  override suspend fun move(from: String, to: String) = withContext(dispatcher) {
    fileModificationService.rename(
      from,
      "rename",
      to,
    )
  }
}
