package id.kokowilly.filebrowser.foundation.logics

import java.util.*

object DataFormat {
  private val DEFAULT_LOCALE = Locale.US

  private const val KB = 1024.0
  private const val MB = KB * 1024
  private const val GB = MB * 1024

  fun formatBytes(bytes: Long): String {

    return when {
      bytes >= GB -> String.format(DEFAULT_LOCALE, "%.2f GB", bytes / GB)
      bytes >= MB -> String.format(DEFAULT_LOCALE, "%.2f MB", bytes / MB)
      else -> String.format(DEFAULT_LOCALE, "%.2f KB", bytes / KB)
    }
  }
}