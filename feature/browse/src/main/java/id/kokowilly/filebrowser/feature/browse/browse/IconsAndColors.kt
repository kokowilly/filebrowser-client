package id.kokowilly.filebrowser.feature.browse.browse

import id.kokowilly.filebrowser.feature.browse.R

object IconsAndColors {

  fun getIconAndColor(extension: String): Pair<Int, Int> {
    return types[extension] ?: Pair(R.drawable.ic_file_32, R.color.icon_grey)
  }

  private val types = mapOf(
    // Archives
    ".7z" to Pair(R.drawable.ic_compress_32, R.color.icon_grey),
    ".bz" to Pair(R.drawable.ic_compress_32, R.color.icon_grey),
    ".bz2" to Pair(R.drawable.ic_compress_32, R.color.icon_grey),
    ".cab" to Pair(R.drawable.ic_compress_32, R.color.icon_blue),
    ".deb" to Pair(R.drawable.ic_archive_32, R.color.icon_grey),
    ".gtar" to Pair(R.drawable.ic_compress_32, R.color.icon_red),
    ".gz" to Pair(R.drawable.ic_compress_32, R.color.icon_grey),
    ".gzip" to Pair(R.drawable.ic_compress_32, R.color.icon_grey),
    ".msi" to Pair(R.drawable.ic_archive_32, R.color.icon_blue),
    ".pkg" to Pair(R.drawable.ic_archive_32, R.color.icon_grey),
    ".rar" to Pair(R.drawable.ic_compress_32, R.color.icon_violet),
    ".rpm" to Pair(R.drawable.ic_archive_32, R.color.icon_grey),
    ".tar" to Pair(R.drawable.ic_compress_32, R.color.icon_grey),
    ".xz" to Pair(R.drawable.ic_compress_32, R.color.icon_grey),
    ".z" to Pair(R.drawable.ic_compress_32, R.color.icon_grey),
    ".zip" to Pair(R.drawable.ic_compress_32, R.color.icon_yellow),
    ".zst" to Pair(R.drawable.ic_compress_32, R.color.icon_grey),

    // Audio
    ".aac" to Pair(R.drawable.ic_audio_32, R.color.icon_orange),
    ".aif" to Pair(R.drawable.ic_audio_32, R.color.icon_red),
    ".aifc" to Pair(R.drawable.ic_audio_32, R.color.icon_red),
    ".aiff" to Pair(R.drawable.ic_audio_32, R.color.icon_red),
    ".au" to Pair(R.drawable.ic_audio_32, R.color.icon_orange),
    ".flac" to Pair(R.drawable.ic_audio_32, R.color.icon_green),
    ".m2a" to Pair(R.drawable.ic_audio_32, R.color.icon_blue),
    ".m3u" to Pair(R.drawable.ic_audio_32, R.color.icon_blue),
    ".m4a" to Pair(R.drawable.ic_audio_32, R.color.icon_blue),
    ".mid" to Pair(R.drawable.ic_audio_32, R.color.icon_blue),
    ".midi" to Pair(R.drawable.ic_audio_32, R.color.icon_blue),
    ".mp2" to Pair(R.drawable.ic_audio_32, R.color.icon_blue),
    ".mp3" to Pair(R.drawable.ic_audio_32, R.color.icon_blue),
    ".mpa" to Pair(R.drawable.ic_audio_32, R.color.icon_blue),
    ".mpga" to Pair(R.drawable.ic_audio_32, R.color.icon_blue),
    ".oga" to Pair(R.drawable.ic_audio_32, R.color.icon_orange),
    ".ogg" to Pair(R.drawable.ic_audio_32, R.color.icon_orange),
    ".opus" to Pair(R.drawable.ic_audio_32, R.color.icon_violet),

    // Images
    ".ai" to Pair(R.drawable.ic_image_32, R.color.icon_red),
    ".bmp" to Pair(R.drawable.ic_image_32, R.color.icon_blue),
    ".dwg" to Pair(R.drawable.ic_image_32, R.color.icon_blue),
    ".eps" to Pair(R.drawable.ic_image_32, R.color.icon_blue),
    ".gif" to Pair(R.drawable.ic_image_32, R.color.icon_grey),
    ".ico" to Pair(R.drawable.ic_image_32, R.color.icon_grey),
    ".jpe" to Pair(R.drawable.ic_image_32, R.color.icon_yellow),
    ".jpeg" to Pair(R.drawable.ic_image_32, R.color.icon_yellow),
    ".jpg" to Pair(R.drawable.ic_image_32, R.color.icon_yellow),
    ".jps" to Pair(R.drawable.ic_image_32, R.color.icon_yellow),
    ".odg" to Pair(R.drawable.ic_image_32, R.color.icon_orange),
    ".odi" to Pair(R.drawable.ic_image_32, R.color.icon_violet),
    ".pct" to Pair(R.drawable.ic_image_32, R.color.icon_red),
    ".pcx" to Pair(R.drawable.ic_image_32, R.color.icon_red),
    ".pic" to Pair(R.drawable.ic_image_32, R.color.icon_red),
    ".pict" to Pair(R.drawable.ic_image_32, R.color.icon_red),
    ".png" to Pair(R.drawable.ic_image_32, R.color.icon_red),
    ".ps" to Pair(R.drawable.ic_image_32, R.color.icon_red),
    ".psd" to Pair(R.drawable.ic_image_32, R.color.icon_blue),
    ".qti" to Pair(R.drawable.ic_image_32, R.color.icon_grey),
    ".qtif" to Pair(R.drawable.ic_image_32, R.color.icon_grey),
    ".svg" to Pair(R.drawable.ic_image_32, R.color.icon_grey),
    ".tif" to Pair(R.drawable.ic_image_32, R.color.icon_violet),
    ".tiff" to Pair(R.drawable.ic_image_32, R.color.icon_violet),
    ".webp" to Pair(R.drawable.ic_image_32, R.color.icon_green),
    ".x-png" to Pair(R.drawable.ic_image_32, R.color.icon_red),
    ".xbm" to Pair(R.drawable.ic_image_32, R.color.icon_blue),
    ".xcf" to Pair(R.drawable.ic_image_32, R.color.icon_orange),
    ".xif" to Pair(R.drawable.ic_image_32, R.color.icon_violet),

    // Binary
    ".apk" to Pair(R.drawable.ic_app_32, R.color.icon_green),
    ".bat" to Pair(R.drawable.ic_app_32, R.color.icon_blue),
    ".bin" to Pair(R.drawable.ic_app_32, R.color.icon_grey),
    ".exe" to Pair(R.drawable.ic_app_32, R.color.icon_blue),
    ".jar" to Pair(R.drawable.ic_app_32, R.color.icon_red),
    ".ps1" to Pair(R.drawable.ic_app_32, R.color.icon_blue),
    ".sh" to Pair(R.drawable.ic_app_32, R.color.icon_grey),

    // Code and Scripts
    ".asp" to Pair(R.drawable.ic_code_32, R.color.icon_green),
    ".c" to Pair(R.drawable.ic_code_32, R.color.icon_grey),
    ".c++" to Pair(R.drawable.ic_code_32, R.color.icon_violet),
    ".cc" to Pair(R.drawable.ic_code_32, R.color.icon_violet),
    ".class" to Pair(R.drawable.ic_code_32, R.color.icon_red),
    ".conf" to Pair(R.drawable.ic_code_32, R.color.icon_grey),
    ".cpp" to Pair(R.drawable.ic_code_32, R.color.icon_violet),
    ".crt" to Pair(R.drawable.ic_code_32, R.color.icon_grey),
    ".cs" to Pair(R.drawable.ic_code_32, R.color.icon_blue),
    ".css" to Pair(R.drawable.ic_code_32, R.color.icon_yellow),
    ".go" to Pair(R.drawable.ic_code_32, R.color.icon_green),
    ".h" to Pair(R.drawable.ic_code_32, R.color.icon_violet),
    ".hh" to Pair(R.drawable.ic_code_32, R.color.icon_violet),
    ".htm" to Pair(R.drawable.ic_code_32, R.color.icon_orange),
    ".html" to Pair(R.drawable.ic_code_32, R.color.icon_orange),
    ".htmls" to Pair(R.drawable.ic_code_32, R.color.icon_orange),
    ".java" to Pair(R.drawable.ic_code_32, R.color.icon_red),
    ".js" to Pair(R.drawable.ic_code_32, R.color.icon_yellow),
    ".json" to Pair(R.drawable.ic_code_32, R.color.icon_yellow),
    ".kt" to Pair(R.drawable.ic_code_32, R.color.icon_grey),
    ".manifest" to Pair(R.drawable.ic_code_32, R.color.icon_green),
    ".mht" to Pair(R.drawable.ic_code_32, R.color.icon_blue),
    ".mhtml" to Pair(R.drawable.ic_code_32, R.color.icon_blue),
    ".p" to Pair(R.drawable.ic_code_32, R.color.icon_blue),
    ".pas" to Pair(R.drawable.ic_code_32, R.color.icon_blue),
    ".php" to Pair(R.drawable.ic_code_32, R.color.icon_violet),
    ".py" to Pair(R.drawable.ic_code_32, R.color.icon_grey),
    ".rb" to Pair(R.drawable.ic_code_32, R.color.icon_red),
    ".rs" to Pair(R.drawable.ic_code_32, R.color.icon_grey),
    ".shtml" to Pair(R.drawable.ic_code_32, R.color.icon_grey),
    ".vue" to Pair(R.drawable.ic_code_32, R.color.icon_orange),
    ".xml" to Pair(R.drawable.ic_code_32, R.color.icon_grey),
    ".yml" to Pair(R.drawable.ic_code_32, R.color.icon_grey),
    ".zsh" to Pair(R.drawable.ic_code_32, R.color.icon_red),

    // Compact Disc
    ".ccd" to Pair(R.drawable.ic_cd_32, R.color.icon_grey),
    ".dmg" to Pair(R.drawable.ic_cd_32, R.color.icon_blue),
    ".iso" to Pair(R.drawable.ic_cd_32, R.color.icon_violet),
    ".mdf" to Pair(R.drawable.ic_cd_32, R.color.icon_grey),
    ".vdi" to Pair(R.drawable.ic_cd_32, R.color.icon_grey),
    ".vhd" to Pair(R.drawable.ic_cd_32, R.color.icon_grey),
    ".vmdk" to Pair(R.drawable.ic_cd_32, R.color.icon_grey),
    ".wim" to Pair(R.drawable.ic_cd_32, R.color.icon_blue),

    // Documents
    ".doc" to Pair(R.drawable.ic_docs_32, R.color.icon_blue),
    ".docm" to Pair(R.drawable.ic_docs_32, R.color.icon_blue),
    ".docx" to Pair(R.drawable.ic_docs_32, R.color.icon_blue),
    ".latex" to Pair(R.drawable.ic_docs_32, R.color.icon_grey),
    ".log" to Pair(R.drawable.ic_docs_32, R.color.icon_blue),
    ".odc" to Pair(R.drawable.ic_docs_32, R.color.icon_green),
    ".odf" to Pair(R.drawable.ic_docs_32, R.color.icon_green),
    ".odm" to Pair(R.drawable.ic_docs_32, R.color.icon_red),
    ".odp" to Pair(R.drawable.ic_docs_32, R.color.icon_orange),
    ".ods" to Pair(R.drawable.ic_docs_32, R.color.icon_green),
    ".odt" to Pair(R.drawable.ic_docs_32, R.color.icon_blue),
    ".pdf" to Pair(R.drawable.ic_docs_32, R.color.icon_red),
    ".rtf" to Pair(R.drawable.ic_docs_32, R.color.icon_blue),
    ".rtx" to Pair(R.drawable.ic_docs_32, R.color.icon_blue),
    ".texinfo" to Pair(R.drawable.ic_docs_32, R.color.icon_grey),
    ".text" to Pair(R.drawable.ic_docs_32, R.color.icon_grey),
    ".txt" to Pair(R.drawable.ic_docs_32, R.color.icon_grey),
    ".word" to Pair(R.drawable.ic_docs_32, R.color.icon_blue),

    // Video
    ".avi" to Pair(R.drawable.ic_movie_32, R.color.icon_grey),
    ".divx" to Pair(R.drawable.ic_movie_32, R.color.icon_grey),
    ".flv" to Pair(R.drawable.ic_movie_32, R.color.icon_orange),
    ".m1v" to Pair(R.drawable.ic_movie_32, R.color.icon_blue),
    ".m2v" to Pair(R.drawable.ic_movie_32, R.color.icon_blue),
    ".m3u8" to Pair(R.drawable.ic_movie_32, R.color.icon_blue),
    ".m4v" to Pair(R.drawable.ic_movie_32, R.color.icon_blue),
    ".mjpg" to Pair(R.drawable.ic_movie_32, R.color.icon_blue),
    ".mkv" to Pair(R.drawable.ic_movie_32, R.color.icon_grey),
    ".moov" to Pair(R.drawable.ic_movie_32, R.color.icon_grey),
    ".mov" to Pair(R.drawable.ic_movie_32, R.color.icon_grey),
    ".movie" to Pair(R.drawable.ic_movie_32, R.color.icon_grey),
    ".mp4" to Pair(R.drawable.ic_movie_32, R.color.icon_blue),
    ".mpe" to Pair(R.drawable.ic_movie_32, R.color.icon_blue),
    ".mpeg" to Pair(R.drawable.ic_movie_32, R.color.icon_blue),
    ".mpg" to Pair(R.drawable.ic_movie_32, R.color.icon_blue),
    ".mv" to Pair(R.drawable.ic_movie_32, R.color.icon_blue),
    ".ogv" to Pair(R.drawable.ic_movie_32, R.color.icon_violet),
    ".opus" to Pair(R.drawable.ic_movie_32, R.color.icon_violet),
    ".qt" to Pair(R.drawable.ic_movie_32, R.color.icon_grey),
    ".qt" to Pair(R.drawable.ic_movie_32, R.color.icon_grey),
    ".qtc" to Pair(R.drawable.ic_movie_32, R.color.icon_grey),

    // Fonts
    ".otf" to Pair(R.drawable.ic_font_32, R.color.icon_grey),
    ".ttf" to Pair(R.drawable.ic_font_32, R.color.icon_grey),
    ".woff" to Pair(R.drawable.ic_font_32, R.color.icon_grey),
    ".woff2" to Pair(R.drawable.ic_font_32, R.color.icon_grey),

    // Tables
    ".csv" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".db" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".odb" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".odf" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xl" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xla" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xlam" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xlb" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xlc" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xld" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xlk" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xll" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xlm" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xls" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xlsb" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xlsm" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xlsx" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xlt" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xltm" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xltx" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xlv" to Pair(R.drawable.ic_table_32, R.color.icon_green),
    ".xlw" to Pair(R.drawable.ic_table_32, R.color.icon_green),

    // Presentation Files
    ".odp" to Pair(R.drawable.ic_present_32, R.color.icon_orange),
    ".ppa" to Pair(R.drawable.ic_present_32, R.color.icon_orange),
    ".ppam" to Pair(R.drawable.ic_present_32, R.color.icon_orange),
    ".pps" to Pair(R.drawable.ic_present_32, R.color.icon_orange),
    ".ppsm" to Pair(R.drawable.ic_present_32, R.color.icon_orange),
    ".ppsx" to Pair(R.drawable.ic_present_32, R.color.icon_orange),
    ".ppt" to Pair(R.drawable.ic_present_32, R.color.icon_orange),
    ".pptm" to Pair(R.drawable.ic_present_32, R.color.icon_orange),
    ".pptx" to Pair(R.drawable.ic_present_32, R.color.icon_orange),
    ".ppz" to Pair(R.drawable.ic_present_32, R.color.icon_orange),
    ".sldm" to Pair(R.drawable.ic_present_32, R.color.icon_orange),
    ".sldx" to Pair(R.drawable.ic_present_32, R.color.icon_orange)
  )


}
