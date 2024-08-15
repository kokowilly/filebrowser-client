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

inline fun <reified T : Any> T.i(message: String) {
    Logger.logWriter.i(T::class.java.simpleName, message)
}

inline fun <reified T : Any> T.d(message: String, throwable: Throwable? = null) {
    Logger.logWriter.d(T::class.java.simpleName, message, throwable)
}

inline fun <reified T : Any> T.e(message: String, throwable: Throwable? = null) {
    Logger.logWriter.e(T::class.java.simpleName, message, throwable)
}
