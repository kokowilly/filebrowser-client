package id.kokowilly.filebrowser.feature.browse.preview

import android.os.Bundle
import coil.load
import id.kokowilly.filebrowser.feature.browse.databinding.ActivityPreviewBinding
import id.kokowilly.filebrowser.foundation.style.ImmersiveActivity
import org.koin.android.ext.android.inject

class PreviewActivity : ImmersiveActivity() {
  private val previewRepository: PreviewRepository by inject()

  private val binding by lazy { ActivityPreviewBinding.inflate(layoutInflater) }

  private val path by lazy { intent.getStringExtra(EXTRA_PATH).orEmpty() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(binding.root)
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)

    binding.imagePreview.load(previewRepository.getPreviewUrl(path))
  }

  companion object {
    const val EXTRA_PATH = "path"
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
