package id.kokowilly.filebrowser.lib.network.api

import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface FileModificationService {
  @PATCH("/api/resources/{path}")
  suspend fun rename(
    @Path("path") path: String,
    @Query("action") action: String,
    @Query("destination") destination: String,
    @Query("override") override: Boolean = false,
    @Query("rename") rename: Boolean = false,
  )
}
