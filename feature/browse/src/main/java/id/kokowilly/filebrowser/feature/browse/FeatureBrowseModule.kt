package id.kokowilly.filebrowser.feature.browse

import android.content.Intent
import id.kokowilly.filebrowser.feature.browse.list.DataListActivity
import id.kokowilly.filebrowser.feature.browse.list.DataListViewModel
import id.kokowilly.filebrowser.feature.browse.list.ResourceRepository
import id.kokowilly.filebrowser.feature.browse.list.ResourceRepositoryImpl
import id.kokowilly.filebrowser.feature.browse.list.menu.download.ListMenuViewModel
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

  viewModel { DataListViewModel(get()) }

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
      Intent(context, DataListActivity::class.java)
    }
  }
