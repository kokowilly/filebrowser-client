package id.kokowilly.filebrowser.lib.network.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
  @POST("/api/renew")
  suspend fun renew(): String

  @POST("/api/login")
  suspend fun login(@Body request: LoginRequest): String
}

@JsonClass(generateAdapter = true)
data class LoginRequest(
  @field:Json(name = "username")
  val username: String,
  @field:Json(name = "password")
  val password: String
)
