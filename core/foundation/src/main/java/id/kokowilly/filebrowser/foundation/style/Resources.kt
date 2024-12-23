package id.kokowilly.filebrowser.foundation.style

import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.core.content.res.ResourcesCompat

fun View.getColor(color: Int): Int = ResourcesCompat.getColor(
  resources,
  color,
  null
)

fun View.animate(@AnimRes res: Int) {
  startAnimation(
    AnimationUtils.loadAnimation(context, res)
  )
}
