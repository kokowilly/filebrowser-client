package id.kokowilly.filebrowser.foundation.logics

fun String.cleanPath(): String = replace("//", "/") // Replace double slashes with single slash
  .let { if (!it.startsWith("/")) "/$it" else it } // Add leading slash if missing
