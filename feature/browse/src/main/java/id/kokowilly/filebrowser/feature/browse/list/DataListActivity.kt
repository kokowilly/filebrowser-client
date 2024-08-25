package id.kokowilly.filebrowser.feature.browse.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import id.kokowilly.filebrowser.feature.browse.R
import id.kokowilly.filebrowser.feature.browse.databinding.ActivityDataListBinding
import id.kokowilly.filebrowser.feature.browse.databinding.ItemFileThumbnailBinding
import id.kokowilly.filebrowser.foundation.logics.DataFormat
import id.kokowilly.filebrowser.foundation.style.ImmersiveActivity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import id.kokowilly.filebrowser.foundation.R as CoreR

class DataListActivity : ImmersiveActivity() {
  private val binding: ActivityDataListBinding by lazy {
    ActivityDataListBinding.inflate(layoutInflater)
  }

  private val viewModel: DataListViewModel by viewModel()

  private val adapter = DataListAdapter(
    itemClickListener = {
      when (it) {
        is Resource.FolderResource ->
          viewModel.go(it.path)

        else -> Unit
      }
    }
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    ViewCompat.setOnApplyWindowInsetsListener(binding.listData) { view, insets ->
      val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      view.updatePadding(
        bottom = systemBarsInsets.bottom
      )
      insets
    }
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    binding.listData.adapter = adapter
    onBackPressedDispatcher.addCallback(backDispatcher)

    lifecycleScope.launch {
      viewModel.usage.collect {
        runCatching {
          val percentage = ((it.used * 100) / it.total).toInt()

          binding.barUsage.progress = percentage

          binding.textUsage.text =
            "${DataFormat.formatBytes(it.used)} / ${DataFormat.formatBytes(it.total)}"
        }
      }
    }

    lifecycleScope.launch {
      viewModel.path.collect {
        binding.textPath.text = it.ifBlank { "/" }
      }
    }

    lifecycleScope.launch {
      viewModel.files
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
  }

  private fun showExitConfirmationDialog() {
    AlertDialog.Builder(this)
      .setTitle(R.string.title_exit)
      .setMessage(R.string.message_exit)
      .setPositiveButton(R.string.yes) { dialog, _ ->
        dialog.dismiss()
        finishAffinity()
      }
      .setNegativeButton(R.string.no) { dialog, _ ->
        dialog.dismiss()
      }
      .setCancelable(true)
      .show()
  }

  private val backDispatcher = object : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
      if (viewModel.path.value.isNotEmpty()) {
        viewModel.up()
      } else {
        showExitConfirmationDialog()
      }
    }
  }
}

private class DataListAdapter(
  private val itemClickListener: (Resource) -> Unit,
) :
  ListAdapter<Resource, DataListAdapter.ResourceViewHolder>(Callback) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder =
    when (viewType) {
      TYPE_FOLDER -> FolderViewHolder(
        ItemFileThumbnailBinding.inflate(
          LayoutInflater.from(parent.context),
          parent,
          false
        )
      )

      TYPE_IMAGE -> ImageViewHolder(
        ItemFileThumbnailBinding.inflate(
          LayoutInflater.from(parent.context),
          parent,
          false
        )
      )

      TYPE_ICON -> IconViewHolder(
        ItemFileThumbnailBinding.inflate(
          LayoutInflater.from(parent.context),
          parent,
          false
        )
      )

      else -> throw IllegalArgumentException()
    }


  override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
    when (holder) {
      is FolderViewHolder -> holder.bind(getItem(position) as Resource.FolderResource)
      is ImageViewHolder -> holder.bind(getItem(position) as Resource.ImageResource)
      is IconViewHolder -> holder.bind(getItem(position) as Resource.IconResource)
    }
  }

  override fun getItemViewType(position: Int): Int = when (getItem(position)) {
    is Resource.FolderResource -> TYPE_FOLDER
    is Resource.ImageResource -> TYPE_IMAGE
    is Resource.IconResource -> TYPE_ICON
    else -> throw IllegalArgumentException()
  }

  abstract inner class ResourceViewHolder(view: View) : RecyclerView.ViewHolder(view)

  inner class FolderViewHolder(
    private val binding: ItemFileThumbnailBinding,
  ) : ResourceViewHolder(binding.root) {
    private lateinit var resource: Resource.FolderResource

    init {
      binding.root.setOnClickListener { itemClickListener.invoke(resource) }

      binding.itemThumbnail.setImageResource(R.drawable.ic_folder_32)
      binding.itemThumbnail.setColorFilter(itemView.getColor(CoreR.color.light_blue_600))
    }

    fun bind(entity: Resource.FolderResource) {
      this.resource = entity
      binding.itemLabel.text = entity.name
    }
  }

  inner class ImageViewHolder(
    private val binding: ItemFileThumbnailBinding,
  ) : ResourceViewHolder(binding.root) {
    private lateinit var resource: Resource.ImageResource

    init {
      binding.root.setOnClickListener { itemClickListener.invoke(resource) }
    }

    fun bind(entity: Resource.ImageResource) {
      this.resource = entity
//      binding.itemThumbnail.setImageResource(resource.iconResource)
//      binding.itemThumbnail.setColorFilter(itemView.getColor(resource.iconColor))
      binding.itemThumbnail.load(resource.thumbnail)
      binding.itemLabel.text = entity.name
    }
  }

  inner class IconViewHolder(
    private val binding: ItemFileThumbnailBinding,
  ) : ResourceViewHolder(binding.root) {
    private lateinit var resource: Resource.IconResource

    init {
      binding.root.setOnClickListener { itemClickListener.invoke(resource) }
    }

    fun bind(entity: Resource.IconResource) {
      this.resource = entity
      binding.itemThumbnail.setImageResource(resource.iconResource)
      binding.itemThumbnail.setColorFilter(itemView.getColor(resource.iconColor))
      binding.itemLabel.text = entity.name
    }
  }

  object Callback : DiffUtil.ItemCallback<Resource>() {
    override fun areItemsTheSame(oldItem: Resource, newItem: Resource): Boolean =
      oldItem.path == newItem.path

    override fun areContentsTheSame(oldItem: Resource, newItem: Resource): Boolean =
      oldItem == newItem
  }
}

private const val TYPE_FOLDER = 0
private const val TYPE_IMAGE = 1
private const val TYPE_ICON = 2

private fun View.getColor(color: Int): Int = ResourcesCompat.getColor(
  resources,
  color,
  null
)
