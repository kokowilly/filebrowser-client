package id.kokowilly.filebrowser.feature.browse.browse.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.kokowilly.filebrowser.feature.browse.browse.Resource
import id.kokowilly.filebrowser.feature.browse.browse.menu.ItemOptionDialog
import id.kokowilly.filebrowser.feature.browse.databinding.DialogTextInputBinding

class NameInputDialog : BottomSheetDialogFragment() {
  private var _binding: DialogTextInputBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    return DialogTextInputBinding.inflate(inflater, container, false).also {
      _binding = it
    }.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    fun start(activity: AppCompatActivity, resource: Resource) {
      ItemOptionDialog().apply {
        arguments = bundleOf(
          EXTRA_PATH to resource.path
        )
      }.show(activity.supportFragmentManager, "NameInputDialog")
    }
  }
}

private const val EXTRA_PATH = "extra_path"
