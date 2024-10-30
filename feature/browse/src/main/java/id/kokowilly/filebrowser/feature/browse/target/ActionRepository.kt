package id.kokowilly.filebrowser.feature.browse.target

import id.kokowilly.filebrowser.lib.network.api.FileModificationService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal interface ActionRepository {
  suspend fun move(from: String, to: String)
  suspend fun rename(from: String, to: String)
  suspend fun copy(from: String, to: String)
  suspend fun delete(path: String)
}

internal class ActionRepositoryImpl(
  private val fileModificationService: FileModificationService,
  private val dispatcher: CoroutineDispatcher,
) : ActionRepository {
  override suspend fun move(from: String, to: String) = withContext(dispatcher) {
    fileModificationService.rename(
      path = from,
      action = "rename",
      destination = to,
    )
  }

  override suspend fun rename(from: String, to: String) {
    fileModificationService.rename(
      path = from,
      action = "rename",
      destination = to,
      override = true
    )
  }

  override suspend fun copy(from: String, to: String) = withContext(dispatcher) {
    fileModificationService.rename(
      path = from,
      action = "copy",
      destination = to,
    )
  }

  override suspend fun delete(path: String) = withContext(dispatcher) {
    fileModificationService.delete(
      path = path,
    ).body() ?: Unit
  }
}
