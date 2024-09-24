package id.kokowilly.filebrowser.foundation.style

import android.view.View
import androidx.core.content.res.ResourcesCompat

fun View.getColor(color: Int): Int = ResourcesCompat.getColor(
  resources,
  color,
  null
)
