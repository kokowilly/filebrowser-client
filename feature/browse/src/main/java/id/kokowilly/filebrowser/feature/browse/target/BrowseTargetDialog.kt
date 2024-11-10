package id.kokowilly.filebrowser.feature.browse.target

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
          origin = BrowseViewModel.PathRequest.Origin.UI(System.currentTimeMillis())
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

  private val action by lazy {
    ((arguments?.getSerializable(EXTRA_ACTION) as Action?) ?: Action.MOVE).also {
      when (it) {
        Action.MOVE -> performAction = { vm.move() }
        Action.COPY -> performAction = { vm.copy() }
      }
    }
  }

  private lateinit var performAction: () -> Unit

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
          val formatId = when (action) {
            Action.MOVE -> R.string.format_move_title
            Action.COPY -> R.string.format_copy_title
          }

          getString(
            formatId,
            source,
            path
          )
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
          is BrowseTargetViewModel.Command.Error -> {
            Snackbar.make(
              binding.root,
              getString(R.string.format_message_move_failed, it.exception.message),
              Snackbar.LENGTH_SHORT
            )
              .setAction(R.string.menu_retry) { performAction.invoke() }
              .show()
          }

          BrowseTargetViewModel.Command.Success -> {
            dismiss()
            Snackbar.make(
              binding.root,
              R.string.message_move_success,
              Snackbar.LENGTH_SHORT
            ).show()
          }

          BrowseTargetViewModel.Command.None -> Unit
        }
      }
    }

    binding.buttonConfirm.setOnClickListener {
      performAction.invoke()
    }
  }

  companion object {
    fun start(
      fragmentManager: FragmentManager,
      filePath: String,
      action: Action,
    ) {
      BrowseTargetDialog().apply {
        arguments = bundleOf(
          EXTRA_PATH to filePath,
          EXTRA_ACTION to action,
        )
      }.show(fragmentManager, "BrowseTargetDialog")
    }
  }

  enum class Action {
    MOVE,
    COPY,
  }
}

private const val EXTRA_PATH = "path"
private const val EXTRA_ACTION = "action"
