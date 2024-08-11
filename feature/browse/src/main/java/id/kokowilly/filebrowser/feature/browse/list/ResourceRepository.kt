package id.kokowilly.filebrowser.feature.browse.list

import id.kokowilly.filebrowser.lib.network.api.DataService
import id.kokowilly.filebrowser.lib.network.api.ItemResponse
import id.kokowilly.filebrowser.lib.network.api.UsageResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal interface ResourceRepository {
  suspend fun getUsage(): UsageResponse

  suspend fun getResource(path: String): List<Resource>
}

internal class ResourceRepositoryImpl(
  private val dataService: DataService,
  private val dispatcher: CoroutineDispatcher
) : ResourceRepository {
  override suspend fun getUsage(): UsageResponse = withContext(dispatcher) {
    dataService.usage()
  }

  override suspend fun getResource(path: String): List<Resource> = withContext(dispatcher) {
    dataService.directory(path).items
      .mapNotNull {
        when (it.type) {
          "image" -> Resource.ImageResource(it)
          "icon" -> Resource.IconResource(it)
          "" -> Resource.FolderResource(it)
          else -> null
        }
      }
  }
}

sealed interface Resource {
  val name: String
  val path: String
  val size: Long
  val extension: String

  data class ImageResource(
    override val name: String,
    override val path: String,
    override val size: Long,
    override val extension: String,
    val thumbnail: String,
  ) : Resource {
    constructor(response: ItemResponse) : this(
      name = response.name,
      path = response.path,
      size = response.size,
      extension = response.extension,
      thumbnail = response.path
    )
  }

  data class IconResource(
    override val name: String,
    override val path: String,
    override val size: Long,
    override val extension: String,
    val iconResource: Int
  ) : Resource {
    constructor(response: ItemResponse) : this(
      name = response.name,
      path = response.path,
      size = response.size,
      extension = response.extension,
      iconResource = 0
    )
  }

  data class FolderResource(
    override val name: String,
    override val path: String,
    override val size: Long,
    override val extension: String,
  ) : Resource {
    constructor(response: ItemResponse) : this(
      name = response.name,
      path = response.path,
      size = response.size,
      extension = response.extension
    )
  }
}
