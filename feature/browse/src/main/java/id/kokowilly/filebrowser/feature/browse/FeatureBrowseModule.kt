package id.kokowilly.filebrowser.feature.browse

import android.content.Intent
import id.kokowilly.filebrowser.feature.browse.list.DataBrowseActivity
import id.kokowilly.filebrowser.lib.navigation.Navigation
import id.kokowilly.filebrowser.lib.navigation.NavigationLibrary
import org.koin.dsl.module

val featureBrowseModule = module {
}

val featureBrowseLibrary
  get() = NavigationLibrary {
    Navigation.register("browse:list") { context, _ ->
      Intent(context, DataBrowseActivity::class.java)
    }
  }