package id.kokowilly.filebrowser.log

object Logger {
  interface LogWriter {
    fun i(tag: String, message: String)
    fun d(tag: String, message: String, throwable: Throwable? = null)
    fun e(tag: String, message: String, throwable: Throwable? = null)

    object NOTHING : LogWriter {
      override fun i(tag: String, message: String) = Unit

      override fun d(tag: String, message: String, throwable: Throwable?) = Unit

      override fun e(tag: String, message: String, throwable: Throwable?) = Unit
    }
  }

  var logWriter: LogWriter = LogWriter.NOTHING
}

class Tag(name: String) {
  private val name = "FileBrowser-$name"

  fun i(message: String) =
    Logger.logWriter.i(name, message)

  fun d(message: String, throwable: Throwable? = null) =
    Logger.logWriter.d(name, message, throwable)

  fun e(message: String, throwable: Throwable? = null) =
    Logger.logWriter.e(name, message, throwable)
}
