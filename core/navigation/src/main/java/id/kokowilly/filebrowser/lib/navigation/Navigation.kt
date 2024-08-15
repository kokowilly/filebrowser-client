package id.kokowilly.filebrowser.lib.navigation

import android.content.Context
import android.content.Intent
import id.kokowilly.filebrowser.log.d

object Navigation {
  private val registry = mutableMapOf<String, NavigationRegistry>()

  fun register(path: String, registry: NavigationRegistry) {
    this.registry[path] = registry
  }

  fun intentOf(context: Context, path: String, data: Map<String, Any> = emptyMap()): Intent {
    d("navigate to: $path")

    return requireNotNull(registry[path]).makeIntent(context, data)
  }

}

fun interface NavigationRegistry {
  fun makeIntent(context: Context, data: Map<String, Any>): Intent
}

fun alias(target: String): NavigationRegistry = NavigationRegistry { context, data ->
  Navigation.intentOf(context, target, data)
}
