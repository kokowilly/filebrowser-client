package id.kokowilly.filebrowser.lib.network

import dagger.Component
import dagger.Module
import dagger.Provides
import id.kokowilly.filebrowser.lib.common.DaggerContainer
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class
    ]
)
interface NetworkComponent {
    val retrofitFactory: RetrofitFactory

    companion object : DaggerContainer<Unit, NetworkComponent>() {
        override fun initialize(dependency: Unit): NetworkComponent {
            return DaggerNetworkComponent.create()
        }
    }
}

@Module
internal object NetworkModule {
    @Provides @Singleton
    fun factory(): RetrofitFactory =
        RetrofitFactory()
}
