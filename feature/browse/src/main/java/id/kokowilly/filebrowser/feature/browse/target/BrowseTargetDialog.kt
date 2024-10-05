package id.kokowilly.filebrowser.feature.browse.target

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.kokowilly.filebrowser.feature.browse.browse.BrowseViewModel
import id.kokowilly.filebrowser.feature.browse.browse.Resource
import id.kokowilly.filebrowser.feature.browse.databinding.DialogTargetBinding
import id.kokowilly.filebrowser.foundation.style.halfExpand
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class BrowseTargetDialog : BottomSheetDialogFragment() {
  private var _binding: DialogTargetBinding? = null

  private val binding get() = _binding!!

  private val vm: BrowseTargetViewModel by viewModel()

  private val adapter = ListItemAdapter(
    itemClickListener = {
      if (it is Resource.FolderResource) vm.go(
        BrowseViewModel.PathRequest(
          path = it.path,
          origin = BrowseViewModel.PathRequest.Origin.UI
        )
      )
    }
  )

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View = DialogTargetBinding.inflate(
    LayoutInflater.from(context),
    container,
    false
  ).also {
    _binding = it
  }.root

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private val source by lazy {
    File(arguments?.getString(EXTRA_PATH).orEmpty())
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.listData.adapter = adapter
    halfExpand()

    vm.initialize(source)

    lifecycleScope.launch {
      vm.originalFile.map { it.name }
        .combine(
          vm.path.map { it.path.ifEmpty { "/" } }
        ) { source, path ->
          "$source => $path"
        }.collect {
          binding.textPath.text = it
        }
    }

    lifecycleScope.launch {
      vm.files
        .map { items ->
          items.sortedWith(
            compareBy<Resource> { it !is Resource.FolderResource }
              .thenBy { it.name }
          )
        }
        .collect {
          adapter.submitList(it)
        }
    }

    lifecycleScope.launch {
      vm.command.collect {
        when (it) {
          BrowseTargetViewModel.Command.Error -> Toast.makeText(
            requireContext(),
            "Fail to move",
            Toast.LENGTH_SHORT
          )

          BrowseTargetViewModel.Command.Success -> {
            Toast.makeText(
              requireContext(),
              "Success to move",
              Toast.LENGTH_SHORT
            ).show()
            dismiss()
          }

          BrowseTargetViewModel.Command.None -> Unit
        }
      }
    }

    binding.buttonConfirm.setOnClickListener {
      vm.move()
    }
  }

  companion object {
    fun start(fragmentManager: FragmentManager, filePath: String) {
      BrowseTargetDialog().apply {
        arguments = bundleOf(
          EXTRA_PATH to filePath
        )
      }.show(fragmentManager, "BrowseTargetDialog")
    }
  }
}

private const val EXTRA_PATH = "path"
