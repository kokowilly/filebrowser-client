package id.kokowilly.filebrowser.feature.browse.browse

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import id.kokowilly.filebrowser.feature.browse.BrowseNotificationChannel
import id.kokowilly.filebrowser.feature.browse.R
import id.kokowilly.filebrowser.feature.browse.browse.menu.ItemOptionDialog
import id.kokowilly.filebrowser.feature.browse.databinding.ActivityBrowseBinding
import id.kokowilly.filebrowser.feature.browse.databinding.ItemFileThumbnailBinding
import id.kokowilly.filebrowser.feature.browse.preview.PreviewActivity
import id.kokowilly.filebrowser.foundation.logics.DataFormat
import id.kokowilly.filebrowser.foundation.style.ImmersiveActivity
import id.kokowilly.filebrowser.foundation.style.getColor
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import id.kokowilly.filebrowser.foundation.R as CoreR

class BrowseActivity : ImmersiveActivity() {
  private val binding: ActivityBrowseBinding by lazy {
    ActivityBrowseBinding.inflate(layoutInflater)
  }

  private val vm: BrowseViewModel by viewModel()

  private val channel: BrowseNotificationChannel by inject()

  private val itemClickListener: (Resource) -> Unit = {
    when (it) {
      is Resource.FolderResource ->
        vm.go(BrowseViewModel.PathRequest(it.path, BrowseViewModel.PathRequest.Origin.UI))

      is Resource.ImageResource -> {
        startActivity(
          Intent(this, PreviewActivity::class.java)
            .putExtra(PreviewActivity.EXTRA_PATH, it.path)
        )
      }

      else -> openMenu(it)
    }
  }

  private val adapter = DataListAdapter(
    itemClickListener = itemClickListener
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
      val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      view.updatePadding(
        bottom = systemBarsInsets.bottom,
        top = 0,
        left = systemBarsInsets.left,
        right = systemBarsInsets.right,
      )
      insets
    }
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    binding.listData.adapter = adapter
    onBackPressedDispatcher.addCallback(backDispatcher)

    lifecycleScope.launch {
      vm.usage.collect {
        runCatching {
          val percentage = ((it.used * 100) / it.total).toInt()

          binding.barUsage.progress = percentage

          binding.textUsage.text =
            getString(
              R.string.format_usage,
              DataFormat.formatBytes(it.used),
              DataFormat.formatBytes(it.total)
            )
        }
      }
    }

    lifecycleScope.launch {
      vm.path.collect {
        binding.textPath.text = it.path.ifBlank { "/" }
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
      channel.command
        .collect { command ->
          when (command) {
            is BrowseNotificationChannel.Command.Invalidate -> {
              if (command.path == vm.path.value.path) {
                vm.go(
                  BrowseViewModel.PathRequest(
                    command.path,
                    BrowseViewModel.PathRequest.Origin.SYSTEM
                  )
                )
              }
            }
          }
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

  private fun openMenu(resource: Resource) {
    ItemOptionDialog.start(this, resource)
  }

  private val backDispatcher = object : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
      val firstItem = vm.files.value.firstOrNull()
      if (firstItem is Resource.FolderResource && firstItem.name == "..") {
        itemClickListener.invoke(firstItem)
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
      binding.itemThumbnail.setColorFilter(itemView.getColor(resource.iconColor))
      binding.itemThumbnail.load(resource.thumbnail) {
        placeholder(resource.iconResource)
        fallback(resource.iconResource)
        crossfade(true)
        listener(
          onSuccess = { _, _ ->
            binding.itemThumbnail.clearColorFilter()
          },
          onStart = { _ ->
            binding.itemThumbnail.setColorFilter(itemView.getColor(resource.iconColor))
          })

      }
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
