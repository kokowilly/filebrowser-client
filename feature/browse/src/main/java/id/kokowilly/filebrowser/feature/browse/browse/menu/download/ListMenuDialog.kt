package id.kokowilly.filebrowser.feature.browse.browse.menu.download

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.kokowilly.filebrowser.feature.browse.R
import id.kokowilly.filebrowser.feature.browse.browse.Resource
import id.kokowilly.filebrowser.feature.browse.databinding.FragmentListMenuBinding
import kotlinx.coroutines.launch
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
          vm.startDownload(filePath)
        }
      }
    }

    lifecycleScope.launch {
      vm.command.collect { command ->
        when (command) {
          is ListMenuViewModel.Command.Download -> {
            downloadFile(command.file, command.url)
            dismiss()
          }
        }
      }
    }
  }

  private fun downloadFile(file: File, url: Uri) {
    val context = requireContext()

    (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(
      DownloadManager.Request(url)
        .setTitle(getString(R.string.title_downloading))
        .setDescription(getString(R.string.message_downloading, file.name))
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file.name)
    )

    Toast.makeText(
      context,
      R.string.message_download_in_progress,
      Toast.LENGTH_LONG
    ).show()
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
