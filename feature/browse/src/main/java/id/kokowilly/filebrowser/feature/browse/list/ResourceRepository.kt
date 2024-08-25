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
  private val dispatcher: CoroutineDispatcher,
  private val baseUrl: String,
  private val auth: String,
) : ResourceRepository {
  override suspend fun getUsage(): UsageResponse = withContext(dispatcher) {
    dataService.usage()
  }

  override suspend fun getResource(path: String): List<Resource> = withContext(dispatcher) {
    dataService.directory(path).items
      .map {
        when {
          it.type == "image" -> Resource.ImageResource(it, buildThumbnailUrl(it.path))
          it.isDir -> Resource.FolderResource(it)
          else -> Resource.IconResource(it)
        }
      }
  }

  private fun buildThumbnailUrl(path: String): String = "${baseUrl}api/preview/thumb/$path?auth=$auth&inline=true&key=${System.currentTimeMillis()}"
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
    val iconResource: Int,
    val iconColor: Int,
    val thumbnail: String,
  ) : Resource {
    constructor(response: ItemResponse, iconAndColor: Pair<Int, Int>, thumbnailUrl: String) : this(
      name = response.name,
      path = response.path,
      size = response.size,
      extension = response.extension,
      iconResource = iconAndColor.first,
      iconColor = iconAndColor.second,
      thumbnail = thumbnailUrl
    )

    constructor(response: ItemResponse, thumbnailUrl: String) : this(
      response = response,
      iconAndColor = IconsAndColors.getIconAndColor(response.extension),
      thumbnailUrl = thumbnailUrl,
    )
  }

  data class IconResource(
    override val name: String,
    override val path: String,
    override val size: Long,
    override val extension: String,
    val iconResource: Int,
    val iconColor: Int,
  ) : Resource {
    constructor(response: ItemResponse, iconAndColor: Pair<Int, Int>) : this(
      name = response.name,
      path = response.path,
      size = response.size,
      extension = response.extension,
      iconResource = iconAndColor.first,
      iconColor = iconAndColor.second,
    )

    constructor(response: ItemResponse) : this(
      response = response,
      iconAndColor = IconsAndColors.getIconAndColor(response.extension)
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
      extension = response.extension,
    )
  }
}

