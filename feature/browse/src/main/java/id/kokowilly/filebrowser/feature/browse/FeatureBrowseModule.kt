package id.kokowilly.filebrowser.feature.browse

import android.content.Intent
import id.kokowilly.filebrowser.feature.browse.list.DataListActivity
import id.kokowilly.filebrowser.feature.browse.list.DataListViewModel
import id.kokowilly.filebrowser.feature.browse.list.ResourceRepository
import id.kokowilly.filebrowser.feature.browse.list.ResourceRepositoryImpl
import id.kokowilly.filebrowser.lib.navigation.Navigation
import id.kokowilly.filebrowser.lib.navigation.NavigationLibrary
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureBrowseModule = module {
  factory<ResourceRepository> {
    ResourceRepositoryImpl(
      dataService = get(),
      dispatcher = Dispatchers.IO
    )
  }

  viewModel { DataListViewModel(get()) }
}

val featureBrowseLibrary
  get() = NavigationLibrary {
    Navigation.register("browse:list") { context, _ ->
      Intent(context, DataListActivity::class.java)
    }
  }
