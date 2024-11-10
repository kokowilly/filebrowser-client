package id.kokowilly.filebrowser.feature.browse.preview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import coil.load
import id.kokowilly.filebrowser.feature.browse.BrowseNotificationChannel
import id.kokowilly.filebrowser.feature.browse.R
import id.kokowilly.filebrowser.feature.browse.browse.Resource
import id.kokowilly.filebrowser.feature.browse.browse.menu.ItemOptionDialog
import id.kokowilly.filebrowser.feature.browse.databinding.ActivityPreviewBinding
import id.kokowilly.filebrowser.foundation.style.ImmersiveActivity
import id.kokowilly.filebrowser.log.Tag
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.File

class PreviewActivity : ImmersiveActivity() {
  private val tag = Tag("PreviewActivity")

  private val previewRepository: PreviewRepository by inject()

  private val binding by lazy { ActivityPreviewBinding.inflate(layoutInflater) }

  private val notificationChannel: BrowseNotificationChannel by inject()

  private val imageResource by lazy {
    intent.getParcelableExtra<Resource.ImageResource>(EXTRA_RESOURCE)!!
  }

  //  private val path by lazy { intent.getStringExtra(EXTRA_PATH).orEmpty() }
  private val file by lazy { File(imageResource.path) }

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

      binding.toolbarLayout.apply {
        setPadding(
          paddingLeft,
          systemBarsInsets.top,
          paddingRight,
          paddingBottom
        )
      }
      insets
    }

    setSupportActionBar(binding.toolbar)
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)

    tag.d("viewing: ${file.path}")

    binding.imagePreview.load(
      previewRepository.getPreviewUrl(
        imageResource.path
      )
    )

    binding.toolbar.title = file.name

    lifecycleScope.launch {
      notificationChannel.command
        .collect {
          when (it) {
            is BrowseNotificationChannel.Command.Invalidate -> {
              tag.d("changed: $it")
              if (it.path == imageResource.path) {
                finish()
              }
            }
          }
        }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_preview, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.menu_more -> {
        ItemOptionDialog.start(this, imageResource)
        true
      }

      else -> super.onOptionsItemSelected(item)
    }
  }

  companion object {
    const val EXTRA_RESOURCE = "resource"
  }
}

internal interface PreviewRepository {
  fun getPreviewUrl(path: String): String

  fun getHDUrl(path: String): String
}

internal class PreviewRepositoryImpl(
  private val baseUrl: String,
  private val auth: String,
) : PreviewRepository {
  override fun getPreviewUrl(path: String): String =
    "${baseUrl}api/preview/big/${path}?auth=${auth}&inline=true&key=${System.currentTimeMillis()}"

  override fun getHDUrl(path: String): String =
    "${baseUrl}api/raw/${path}?auth=${auth}&inline=true"
}
