package id.kokowilly.filebrowser.lib.network.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path

interface DataService {

  @GET("/api/resources/{path}")
  suspend fun directory(@Path("path") path: String?): String

  @GET("/api/usage")
  suspend fun usage(): UsageResponse
}

@JsonClass(generateAdapter = true)
data class UsageResponse(
  @field:Json(name = "total")
  val total: Long,
  @field:Json(name = "used")
  val used: Long
) {
  companion object {
    val EMPTY = UsageResponse(1L, 0L)
  }

}
