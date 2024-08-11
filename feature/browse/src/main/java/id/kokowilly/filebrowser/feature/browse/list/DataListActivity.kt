package id.kokowilly.filebrowser.feature.browse.list

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.kokowilly.filebrowser.feature.browse.databinding.ActivityDataListBinding
import id.kokowilly.filebrowser.feature.browse.databinding.ItemFileThumbnailBinding
import id.kokowilly.filebrowser.foundation.logics.DataFormat
import id.kokowilly.filebrowser.foundation.style.ImmersiveActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DataListActivity : ImmersiveActivity() {
  private val binding: ActivityDataListBinding by lazy {
    ActivityDataListBinding.inflate(layoutInflater)
  }

  private val viewModel: DataListViewModel by viewModel()

  private val adapter = DataListAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    binding.listData.adapter = adapter

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
  }
}

private class DataListAdapter : ListAdapter<FileEntity, DataListAdapter.FileViewHolder>(Callback) {
  object Callback : DiffUtil.ItemCallback<FileEntity>() {
    override fun areItemsTheSame(oldItem: FileEntity, newItem: FileEntity): Boolean =
      oldItem.path == newItem.path

    override fun areContentsTheSame(oldItem: FileEntity, newItem: FileEntity): Boolean =
      oldItem == newItem
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder =
    FileViewHolder(
      ItemFileThumbnailBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )

  override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  class FileViewHolder(private val binding: ItemFileThumbnailBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(entity: FileEntity) {
      binding.itemThumbnail.setImageURI(Uri.parse(entity.path))
      binding.itemLabel.text = entity.name
    }
  }
}
