package id.kokowilly.filebrowser.lib.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import id.kokowilly.filebrowser.lib.common.DaggerContainer
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    NetworkModule::class
  ]
)
interface NetworkComponent {
  val controller: NetworkController
  val factory: NetworkFactory

  companion object : DaggerContainer<Unit, NetworkComponent>() {
    override fun initialize(dependency: Unit): NetworkComponent {
      return DaggerNetworkComponent.create()
    }
  }
}

@Module
internal object NetworkModule {
  @Provides @Singleton
  fun moshi(): Moshi =
    Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()

  @Provides @Singleton
  fun okhttp(): OkHttpClient =
    OkHttpClient.Builder()
      .build()

  @Provides @Singleton
  fun controller(okHttpClient: OkHttpClient, moshi: Moshi): NetworkController =
    NetworkControllerImpl(okHttpClient, moshi)

  @Provides
  fun factory(controller: NetworkController): NetworkFactory = controller
}
