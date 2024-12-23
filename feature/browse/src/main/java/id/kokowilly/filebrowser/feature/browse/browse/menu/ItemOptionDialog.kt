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
import id.kokowilly.filebrowser.feature.browse.browse.input.NameInputDialog
import id.kokowilly.filebrowser.feature.browse.databinding.DialogItemOptionBinding
import id.kokowilly.filebrowser.feature.browse.target.BrowseTargetDialog
import id.kokowilly.filebrowser.foundation.logics.Toggle
import id.kokowilly.filebrowser.foundation.style.animate
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

        binding.menuRename.setOnClickListener {
          vm.startRename(filePath)
        }

        binding.menuDelete.setOnClickListener {
          deleteToggle.enable()
        }

        binding.menuDeleteCancel.setOnClickListener {
          deleteToggle.disable()
        }

        binding.menuDeleteConfirm.setOnClickListener {
          vm.delete(filePath)
        }
      }
    }

    deleteToggle.disable()

    lifecycleScope.launch {
      vm.command.collect { command ->
        when (command) {
          is ItemOptionViewModel.Command.Download -> {
            downloadFile(
              filename = command.filename,
              url = command.url,
            )
            dismiss()
          }

          is ItemOptionViewModel.Command.Move -> {
            BrowseTargetDialog.start(
              fragmentManager = parentFragmentManager,
              filePath = command.filePath,
              action = BrowseTargetDialog.Action.MOVE,
            )
            dismiss()
          }

          is ItemOptionViewModel.Command.Copy -> {
            BrowseTargetDialog.start(
              fragmentManager = parentFragmentManager,
              filePath = command.filePath,
              action = BrowseTargetDialog.Action.COPY,
            )
            dismiss()
          }

          is ItemOptionViewModel.Command.Rename -> {
            NameInputDialog.start(
              fragmentManager = parentFragmentManager,
              filePath = command.filePath,
            )
            dismiss()
          }

          is ItemOptionViewModel.Command.Done -> {
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

  private val deleteToggle = Toggle(
    isEnable = {
      runCatching {
        binding.menuDeleteConfirm.visibility == View.VISIBLE
      }.getOrElse { false }
    },
    enable = {
      runCatching {
        binding.menuDeleteConfirm.animate(R.anim.fade_out)

        binding.menuDeleteConfirm.visibility = View.VISIBLE
        binding.menuDeleteCancel.visibility = View.VISIBLE
        binding.menuDelete.visibility = View.GONE

        binding.menuDeleteConfirm.animate(R.anim.slide_in_right)
        binding.menuDeleteCancel.animate(R.anim.slide_in_left)
      }
    },
    disable = {
      binding.menuDeleteConfirm.animate(R.anim.slide_out_right)
      binding.menuDeleteCancel.animate(R.anim.slide_out_left)

      binding.menuDeleteConfirm.visibility = View.GONE
      binding.menuDeleteCancel.visibility = View.GONE
      binding.menuDelete.visibility = View.VISIBLE

      binding.menuDelete.animate(R.anim.fade_in)
    }
  )

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
