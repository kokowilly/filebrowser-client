package id.kokowilly.filebrowser.connection.editor

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import id.kokowilly.filebrowser.connection.R
import id.kokowilly.filebrowser.connection.database.entity.Connection
import id.kokowilly.filebrowser.connection.databinding.ActivityEditConnectionBinding
import id.kokowilly.filebrowser.foundation.logics.TextWatchers
import id.kokowilly.filebrowser.foundation.style.ImmersiveActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditConnectionActivity : ImmersiveActivity() {

  companion object {
    const val EXTRA_ID = "edit_connection_id"
  }

  private val binding by lazy { ActivityEditConnectionBinding.inflate(layoutInflater) }

  private val id: Int by lazy { intent.getIntExtra(EXTRA_ID, 0) }

  private val viewModel: EditConnectionViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)

    setupLogic()
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

        finish()
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
              this@EditConnectionActivity,
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
          Toast.makeText(this@EditConnectionActivity, it.message, Toast.LENGTH_SHORT).show()
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
