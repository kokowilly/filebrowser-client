package id.kokowilly.filebrowser.connection.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "connection")
data class Connection(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Int,

  @ColumnInfo(name = "name")
  val name: String,

  @ColumnInfo(name = "primaryUrl")
  val primaryUrl: String,

  @ColumnInfo(name = "secondaryUrl")
  val secondaryUrl: String,

  @ColumnInfo(name = "username")
  val username: String,

  @ColumnInfo(name = "password")
  val password: String
) {
  companion object {
    val empty = Connection(0, "", "", "", "", "")
  }
}
