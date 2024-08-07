package id.kokowilly.filebrowser.connection

import androidx.room.Room
import id.kokowilly.filebrowser.connection.collection.NetworkConfigViewModel
import id.kokowilly.filebrowser.connection.collection.ShowNetworkRepository
import id.kokowilly.filebrowser.connection.collection.ShowNetworkRepositoryImpl
import id.kokowilly.filebrowser.connection.database.ConnectionDatabase
import id.kokowilly.filebrowser.connection.database.DaoProvider
import id.kokowilly.filebrowser.connection.editor.EditConnectionRepository
import id.kokowilly.filebrowser.connection.editor.EditConnectionRepositoryImpl
import id.kokowilly.filebrowser.connection.editor.EditConnectionViewModel
import id.kokowilly.filebrowser.connection.editor.NetworkTestRepository
import id.kokowilly.filebrowser.connection.editor.NetworkTestRepositoryImpl
import id.kokowilly.filebrowser.lib.network.logics.Patterns
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureConnectionModule = module {

  single<DaoProvider> {
    Room.databaseBuilder(androidContext(), ConnectionDatabase::class.java, "connections.sqlite")
      .build()
  }

  factory { get<DaoProvider>().connectionDao }

  factory<ShowNetworkRepository> { ShowNetworkRepositoryImpl(get(), Dispatchers.IO) }

  viewModel {
    NetworkConfigViewModel(
      get(),
      Dispatchers.Default
    )
  }

  factory<EditConnectionRepository> { EditConnectionRepositoryImpl(get(), Dispatchers.IO) }

  factory<NetworkTestRepository> {
    NetworkTestRepositoryImpl(
      get(),
      Regex(Patterns.TOKEN_PATTERN),
      Dispatchers.IO
    )
  }

  viewModel {
    EditConnectionViewModel(get(), get())
  }
}
