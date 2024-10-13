package id.kokowilly.filebrowser.feature.browse.browse.menu

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
import id.kokowilly.filebrowser.feature.browse.databinding.DialogItemOptionBinding
import id.kokowilly.filebrowser.feature.browse.target.BrowseTargetDialog
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ItemOptionDialog : BottomSheetDialogFragment() {

  private var _binding: DialogItemOptionBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    return DialogItemOptionBinding.inflate(inflater, container, false).also {
      _binding = it
    }.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private val path: String? by lazy { arguments?.getString(EXTRA_PATH) }

  private val vm: ItemOptionViewModel by viewModel()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    path.also { filePath ->
      if (filePath == null) {
        dismiss()
      } else {
        binding.textTitle.text = File(filePath).name

        binding.menuDownload.setOnClickListener {
          vm.startDownload(filePath)
        }

        binding.menuMove.setOnClickListener {
          vm.startMove(filePath)
        }

        binding.menuCopy.setOnClickListener {
          vm.startCopy(filePath)
        }
      }
    }

    lifecycleScope.launch {
      vm.command.collect { command ->
        when (command) {
          is ItemOptionViewModel.Command.Download -> {
            downloadFile(command.filename, command.url)
            dismiss()
          }

          is ItemOptionViewModel.Command.Move -> {
            BrowseTargetDialog.start(parentFragmentManager, command.filePath)
            dismiss()
          }

          is ItemOptionViewModel.Command.Copy -> {
            BrowseTargetDialog.start(parentFragmentManager, command.filePath)
            dismiss()
          }
        }
      }
    }
  }

  private fun downloadFile(filename: String, url: Uri) {
    val context = requireContext()

    (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(
      DownloadManager.Request(url)
        .setTitle(getString(R.string.title_downloading))
        .setDescription(getString(R.string.message_downloading, filename))
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
    )

    Toast.makeText(
      context,
      R.string.message_download_in_progress,
      Toast.LENGTH_LONG
    ).show()
  }

  companion object {
    fun start(activity: AppCompatActivity, resource: Resource) {
      ItemOptionDialog().apply {
        arguments = bundleOf(
          EXTRA_PATH to resource.path
        )
      }.show(activity.supportFragmentManager, "ItemOptionDialog")
    }
  }
}

private const val EXTRA_PATH = "extra_path"
