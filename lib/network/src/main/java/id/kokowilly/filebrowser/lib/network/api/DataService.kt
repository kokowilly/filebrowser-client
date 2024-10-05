package id.kokowilly.filebrowser.lib.network.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path

interface DataService {
  @GET("/api/resources/{path}")
  suspend fun directory(@Path("path") path: String?): PathResponse

  @GET("/api/usage")
  suspend fun usage(): UsageResponse
}

@JsonClass(generateAdapter = true)
data class UsageResponse(
  @field:Json(name = "total")
  val total: Long,
  @field:Json(name = "used")
  val used: Long,
) {
  companion object {
    val EMPTY = UsageResponse(1L, 0L)
  }
}

@JsonClass(generateAdapter = true)
data class PathResponse(
  @field:Json(name = "name")
  val name: String,
  @field:Json(name = "path")
  val path: String,
  @field:Json(name = "size")
  val size: Long,
  @field:Json(name = "isDir")
  val isDir: Boolean,
  @field:Json(name = "extension")
  val extension: String,
  @field:Json(name = "type")
  val type: String,
  @field:Json(name = "modified")
  val modified: String,
  @field:Json(name = "items")
  val items: List<ItemResponse>,
)

@JsonClass(generateAdapter = true)
data class ItemResponse(
  @field:Json(name = "name")
  val name: String,
  @field:Json(name = "path")
  val path: String,
  @field:Json(name = "size")
  val size: Long,
  @field:Json(name = "isDir")
  val isDir: Boolean,
  @field:Json(name = "extension")
  val extension: String,
  @field:Json(name = "type")
  val type: String,
  @field:Json(name = "modified")
  val modified: String,
)
