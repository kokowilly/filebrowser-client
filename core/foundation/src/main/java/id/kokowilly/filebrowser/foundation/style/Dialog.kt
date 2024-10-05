package id.kokowilly.filebrowser.foundation.style

import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

fun BottomSheetDialogFragment.fullExpand() = setExpand(BottomSheetBehavior.STATE_EXPANDED)

fun BottomSheetDialogFragment.halfExpand() = setExpand(BottomSheetBehavior.STATE_HALF_EXPANDED)

private fun BottomSheetDialogFragment.setExpand(expand: Int) {
  dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
    ?.also { bottomSheet ->
      BottomSheetBehavior.from(bottomSheet).state = expand
    }
}
