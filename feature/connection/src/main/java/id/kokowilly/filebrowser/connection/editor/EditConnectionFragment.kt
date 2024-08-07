package id.kokowilly.filebrowser.connection.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.kokowilly.filebrowser.connection.R
import id.kokowilly.filebrowser.connection.database.entity.Connection
import id.kokowilly.filebrowser.connection.databinding.FragmentEditConnectionBinding
import id.kokowilly.filebrowser.foundation.logics.TextWatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditConnectionFragment : BottomSheetDialogFragment() {

  companion object {
    const val EXTRA_ID = "edit_connection_id"
  }

  private var _binding: FragmentEditConnectionBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View = FragmentEditConnectionBinding.inflate(
    inflater,
    container,
    false
  ).also {
    _binding = it
  }.root

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private var id = 0

  private val viewModel: EditConnectionViewModel by viewModel()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    extractArguments()

    setupLogic()
  }

  private fun extractArguments() {
    id = arguments?.getInt(EXTRA_ID, 0) ?: 0
  }

  override fun onStart() {
    super.onStart()
    dialog?.also {
      if (it is BottomSheetDialog) {
        it.behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
      }
    }
  }

  private fun setupLogic() {
    val invalidate: () -> Unit = {
      binding.buttonTest.isEnabled = validateInput()
      viewModel.invalidate()
    }

    TextWatchers.validateUrl(binding.inputPrimaryUrl, invalidate)
    TextWatchers.validateUrlOptional(binding.inputSecondaryUrl, invalidate)
    TextWatchers.validateLength(binding.inputConnectionName, 3, invalidate)
    TextWatchers.validateLength(binding.inputUsername, 3, invalidate)
    TextWatchers.validateLength(binding.inputPassword, 3, invalidate)

    binding.buttonTest.setOnClickListener {
      lifecycleScope.launch {
        viewModel.test(getConnection())
      }
    }

    binding.buttonSave.setOnClickListener {
      lifecycleScope.launch {
        viewModel.save(getConnection())

        dismiss()
      }
    }

    viewModel.load(id)

    lifecycleScope.launch {
      viewModel.connection
        .filterNotNull()
        .flowOn(Dispatchers.Main)
        .collect {
          binding.inputConnectionName.setText(it.name)
          binding.inputUsername.setText(it.username)
          binding.inputPassword.setText(it.password)
          binding.inputPrimaryUrl.setText(it.primaryUrl)
          binding.inputSecondaryUrl.setText(it.secondaryUrl)
        }
    }

    lifecycleScope.launch {
      viewModel.connectionValidation
        .flowOn(Dispatchers.Main)
        .collect { isSuccess ->
          binding.buttonSave.isEnabled = isSuccess
          if (isSuccess) {
            Toast.makeText(
              requireContext(),
              R.string.connection_success,
              Toast.LENGTH_SHORT
            ).show()
          }
        }
    }

    lifecycleScope.launch {
      viewModel.error
        .flowOn(Dispatchers.Main)
        .collect {
          Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
    }
  }

  private fun getConnection() = Connection(
    id = id,
    name = binding.inputConnectionName.text.toString(),
    primaryUrl = binding.inputPrimaryUrl.text.toString(),
    secondaryUrl = binding.inputSecondaryUrl.text.toString(),
    username = binding.inputUsername.text.toString(),
    password = binding.inputPassword.text.toString()
  )

  private fun validateInput() = listOf(
    binding.inputConnectionName,
    binding.inputPrimaryUrl,
    binding.inputSecondaryUrl,
    binding.inputUsername,
    binding.inputPassword
  ).mapNotNull { it.error }
    .isEmpty()

}
