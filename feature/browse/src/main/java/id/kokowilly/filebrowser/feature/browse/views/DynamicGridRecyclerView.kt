package id.kokowilly.filebrowser.feature.browse.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DynamicGridRecyclerView(context: Context, attrs: AttributeSet) :
  RecyclerView(context, attrs) {

  private val gridLayoutManager = GridLayoutManager(context, 2)

  var itemWidthInDp: Float = 120f

  init {
    layoutManager = gridLayoutManager
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    updateSpanCount(w)
  }

  private fun updateSpanCount(recyclerViewWidth: Int) {
    val itemWidthPx =
      TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        itemWidthInDp,
        resources.displayMetrics
      )
    val spanCount = maxOf(1, (recyclerViewWidth / itemWidthPx).toInt())
    gridLayoutManager?.spanCount = spanCount
  }
}
