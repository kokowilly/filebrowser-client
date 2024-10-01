package id.kokowilly.filebrowser.feature.browse.target

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.kokowilly.filebrowser.feature.browse.R
import id.kokowilly.filebrowser.feature.browse.browse.BrowseViewModel
import id.kokowilly.filebrowser.feature.browse.browse.Resource
import id.kokowilly.filebrowser.feature.browse.databinding.DialogTargetBinding
import id.kokowilly.filebrowser.feature.browse.databinding.ItemFileListBinding
import id.kokowilly.filebrowser.foundation.style.getColor
import id.kokowilly.filebrowser.foundation.style.halfExpand
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import id.kokowilly.filebrowser.foundation.R as CoreR

class BrowseTargetDialog : BottomSheetDialogFragment() {
  private var _binding: DialogTargetBinding? = null

  private val binding get() = _binding!!

  private val vm: BrowseViewModel by viewModel()

  private val adapter = ListItemAdapter(
    itemClickListener = {
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

  private val fileName get() = source.name

  private val parentPath get() = source.parent.orEmpty()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.listData.adapter = adapter
    halfExpand()

    lifecycleScope.launch {
      vm.path.collect {
        binding.textPath.text = it.ifBlank { "/" }
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

    vm.go(parentPath)
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

private class ListItemAdapter(
  private val itemClickListener: (Resource) -> Unit,
) :
  ListAdapter<Resource, ListItemAdapter.ResourceViewHolder>(Callback) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder =
    when (viewType) {
      TYPE_FOLDER -> FolderViewHolder(
        ItemFileListBinding.inflate(
          LayoutInflater.from(parent.context),
          parent,
          false
        )
      )

      TYPE_IMAGE -> ImageViewHolder(
        ItemFileListBinding.inflate(
          LayoutInflater.from(parent.context),
          parent,
          false
        )
      )

      TYPE_ICON -> IconViewHolder(
        ItemFileListBinding.inflate(
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
    private val binding: ItemFileListBinding,
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
    private val binding: ItemFileListBinding,
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
    private val binding: ItemFileListBinding,
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
