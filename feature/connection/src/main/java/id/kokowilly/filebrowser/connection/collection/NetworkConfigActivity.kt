package id.kokowilly.filebrowser.connection.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import id.kokowilly.filebrowser.connection.R
import id.kokowilly.filebrowser.connection.collection.ConnectionAdapter.ConnectionViewHolder
import id.kokowilly.filebrowser.connection.collection.NetworkConfigViewModel.ConnectionData
import id.kokowilly.filebrowser.connection.databinding.ActivityNetworkConfigBinding
import id.kokowilly.filebrowser.connection.databinding.ItemConnectionBinding
import id.kokowilly.filebrowser.connection.databinding.PopupConnectionOptionBinding
import id.kokowilly.filebrowser.connection.editor.EditConnectionFragment
import id.kokowilly.filebrowser.connection.errors.ConnectionException
import id.kokowilly.filebrowser.foundation.style.ImmersiveActivity
import id.kokowilly.filebrowser.lib.navigation.Navigation
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NetworkConfigActivity : ImmersiveActivity() {

  private val viewModel: NetworkConfigViewModel by viewModel()

  private val binding by lazy {
    ActivityNetworkConfigBinding.inflate(layoutInflater)
  }

  private val connectionAdapter = ConnectionAdapter(
    clickListener = { connection -> viewModel.chooseConnection(connection.id) },
    menuClickListener = { anchor, connection -> showOptionsPopup(anchor, connection) }
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
      val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      view.updatePadding(
        bottom = systemBarsInsets.bottom,
        top = systemBarsInsets.top
      )
      insets
    }

    binding.listConnection.apply {
      val linearLayoutManager = LinearLayoutManager(this@NetworkConfigActivity)

      layoutManager = linearLayoutManager
      adapter = connectionAdapter
      addItemDecoration(
        DividerItemDecoration(
          this@NetworkConfigActivity,
          linearLayoutManager.orientation
        )
      )
    }

    binding.buttonAdd.setOnClickListener {
      showEditor(0)
    }
  }

  private fun showEditor(id: Int) {
    EditConnectionFragment().apply {
      arguments = Bundle().apply {
        putInt(EditConnectionFragment.EXTRA_ID, id)
      }
    }.show(supportFragmentManager, null)
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)

    lifecycleScope.launch {
      viewModel.allConnections
        .collect {
          connectionAdapter.submitList(it)

          binding.textNoData.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    lifecycleScope.launch {
      viewModel.error
        .filterIsInstance(ConnectionException::class)
        .collect {
          Toast.makeText(
            this@NetworkConfigActivity,
            getString(
              R.string.error_connection,
              it.connection.name
            ),
            Toast.LENGTH_SHORT
          ).show()
        }
    }

    lifecycleScope.launch {
      viewModel.success
        .collect {
          startActivity(Navigation.intentOf(this@NetworkConfigActivity, "connection:success"))

          Toast.makeText(
            this@NetworkConfigActivity,
            "Connected successfully.",
            Toast.LENGTH_SHORT
          ).show()

          finish()
        }
    }
  }

  private fun showOptionsPopup(anchor: View, entity: ConnectionData) {
    val popupBinding = PopupConnectionOptionBinding.inflate(LayoutInflater.from(anchor.context))
    val popupWindow = PopupWindow(
      popupBinding.root,
      LinearLayout.LayoutParams.WRAP_CONTENT,
      LinearLayout.LayoutParams.WRAP_CONTENT,
      true
    )

    popupBinding.buttonEdit.setOnClickListener {
      showEditor(entity.id)
      popupWindow.dismiss()
    }

    popupBinding.buttonDelete.setOnClickListener {
      viewModel.remove(entity.id)
      popupWindow.dismiss()
    }

    popupWindow.showAsDropDown(anchor, 0, 0, GravityCompat.END)
  }
}

private class ConnectionAdapter(
  private val clickListener: (ConnectionData) -> Unit,
  private val menuClickListener: (View, ConnectionData) -> Unit
) : ListAdapter<ConnectionData, ConnectionViewHolder>(ConnectionDifferential) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionViewHolder =
    ConnectionViewHolder(
      ItemConnectionBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )

  override fun onBindViewHolder(holder: ConnectionViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  object ConnectionDifferential : DiffUtil.ItemCallback<ConnectionData>() {
    override fun areItemsTheSame(oldItem: ConnectionData, newItem: ConnectionData): Boolean =
      oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ConnectionData, newItem: ConnectionData): Boolean =
      oldItem == newItem
  }

  inner class ConnectionViewHolder(
    private val binding: ItemConnectionBinding
  ) : ViewHolder(binding.root) {
    private lateinit var entity: ConnectionData

    fun bind(entity: ConnectionData) {
      this.entity = entity

      binding.text.text = entity.name
    }

    init {
      binding.root.setOnClickListener { clickListener.invoke(entity) }
      binding.buttonMore.setOnClickListener { menuClickListener.invoke(binding.buttonMore, entity) }
    }
  }
}
