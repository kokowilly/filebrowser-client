package id.kokowilly.filebrowser.connection.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.kokowilly.filebrowser.connection.database.dao.ConnectionDao
import id.kokowilly.filebrowser.connection.database.entity.Connection

@Database(
  entities = [
    Connection::class
  ],
  version = 1,
  exportSchema = true
)
abstract class ConnectionDatabase : RoomDatabase(), DaoProvider

interface DaoProvider {
  val connectionDao: ConnectionDao
}
