package id.kokowilly.filebrowser.feature.browse.list.menu.download

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.kokowilly.filebrowser.feature.browse.databinding.FragmentListMenuBinding
import id.kokowilly.filebrowser.feature.browse.list.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ListMenuDialog : BottomSheetDialogFragment() {

  private var _binding: FragmentListMenuBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    return FragmentListMenuBinding.inflate(inflater, container, false).also {
      _binding = it
    }.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private val path: String? by lazy { arguments?.getString(EXTRA_PATH) }

  private val vm: ListMenuViewModel by viewModel()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    path.also { filePath ->
      if (filePath == null) {
        dismiss()
      } else {
        binding.textTitle.text = File(filePath).name

        binding.menuDownload.setOnClickListener {
          vm.startDownload(requireContext(), filePath)
        }
      }
    }
  }

  companion object {
    fun start(activity: AppCompatActivity, resource: Resource) {
      ListMenuDialog().apply {
        arguments = bundleOf(
          EXTRA_PATH to resource.path
        )
      }.show(activity.supportFragmentManager, "ListMenuDialog")
    }
  }
}

private const val EXTRA_PATH = "extra_path"
