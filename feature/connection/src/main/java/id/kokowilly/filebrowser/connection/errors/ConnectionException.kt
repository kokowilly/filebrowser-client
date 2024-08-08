package id.kokowilly.filebrowser.connection.errors

import id.kokowilly.filebrowser.connection.database.entity.Connection
import java.io.IOException

class ConnectionException(val connection: Connection, cause: Throwable? = null) : IOException(cause)
