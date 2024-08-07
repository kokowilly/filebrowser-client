package id.kokowilly.filebrowser.connection.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import id.kokowilly.filebrowser.connection.database.entity.Connection
import kotlinx.coroutines.flow.Flow

@Dao
interface ConnectionDao {
  @Insert
  fun insert(connection: Connection)

  @Update
  fun update(connection: Connection)

  @Query("DELETE FROM connection WHERE id = :id")
  fun delete(id: Int)

  @Query("SELECT * FROM connection")
  fun getAll(): Flow<List<Connection>>

  @Query("SELECT * FROM connection WHERE id = :id")
  fun getById(id: Int): Connection
}
