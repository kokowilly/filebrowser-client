package id.kokowilly.filebrowser.feature.browse.browse.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import id.kokowilly.filebrowser.feature.browse.R
import id.kokowilly.filebrowser.feature.browse.databinding.DialogTextInputBinding
import id.kokowilly.filebrowser.foundation.logics.TextWatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import kotlin.math.min

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

  private val source by lazy {
    File(arguments?.getString(EXTRA_PATH)!!)
  }

  private val vm by viewModel<NameInputViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    vm.initialize(source)

    lifecycleScope.launch {
      vm.originalFile.map { it.name }
        .collect {
          binding.inputText.apply {
            setText(it)
            requestFocus()
            setSelection(0, min(it.length, it.lastIndexOf('.')))
          }
          binding.dialogTitle.text = getString(R.string.format_title_rename, it)
        }
    }

    lifecycleScope.launch {
      vm.command.collect {
        when (it) {
          is NameInputViewModel.Command.Success -> {
            dismiss()
          }

          is NameInputViewModel.Command.Error -> {
            Snackbar.make(
              binding.root,
              it.exception.message.orEmpty(),
              Snackbar.LENGTH_SHORT
            ).show()
          }

          NameInputViewModel.Command.None -> Unit
        }
      }
    }

    binding.buttonConfirm.setOnClickListener {
      vm.rename(binding.inputText.text.toString())
    }

    TextWatchers.validateFilename(binding.inputText)
  }

  companion object {
    fun start(
      fragmentManager: FragmentManager,
      filePath: String,
    ) {
      NameInputDialog().apply {
        arguments = bundleOf(
          EXTRA_PATH to filePath,
        )
      }.show(fragmentManager, "NameInputDialog")
    }
  }
}

private const val EXTRA_PATH = "extra_path"
