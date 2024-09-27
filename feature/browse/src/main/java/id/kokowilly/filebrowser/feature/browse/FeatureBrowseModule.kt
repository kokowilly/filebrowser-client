package id.kokowilly.filebrowser.feature.browse

import android.content.Intent
import id.kokowilly.filebrowser.feature.browse.browse.BrowseActivity
import id.kokowilly.filebrowser.feature.browse.browse.BrowseViewModel
import id.kokowilly.filebrowser.feature.browse.browse.ResourceRepository
import id.kokowilly.filebrowser.feature.browse.browse.ResourceRepositoryImpl
import id.kokowilly.filebrowser.feature.browse.browse.menu.download.ListMenuViewModel
import id.kokowilly.filebrowser.feature.browse.preview.PreviewRepository
import id.kokowilly.filebrowser.feature.browse.preview.PreviewRepositoryImpl
import id.kokowilly.filebrowser.lib.navigation.Navigation
import id.kokowilly.filebrowser.lib.navigation.NavigationLibrary
import id.kokowilly.filebrowser.lib.network.NetworkController
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureBrowseModule = module {
  factory<ResourceRepository> {
    val networkController: NetworkController = get()
    ResourceRepositoryImpl(
      dataService = get(),
      dispatcher = Dispatchers.IO,
      baseUrl = networkController.baseUrl,
      auth = networkController.accessToken,
    )
  }

  viewModel { BrowseViewModel(get()) }

  factory<PreviewRepository> {
    val networkController: NetworkController = get()
    PreviewRepositoryImpl(
      baseUrl = networkController.baseUrl,
      auth = networkController.accessToken,
    )
  }

  viewModel { ListMenuViewModel(get()) }
}

val featureBrowseLibrary
  get() = NavigationLibrary {
    Navigation.register("browse:list") { context, _ ->
      Intent(context, BrowseActivity::class.java)
    }
  }
