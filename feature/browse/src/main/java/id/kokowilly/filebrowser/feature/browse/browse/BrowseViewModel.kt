package id.kokowilly.filebrowser.feature.browse.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.kokowilly.filebrowser.lib.network.api.UsageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal open class BrowseViewModel(
  private val repository: ResourceRepository,
) : ViewModel() {
  private val statePath = MutableStateFlow("/")
  val path: StateFlow<String> get() = statePath

  private val stateUsage = MutableStateFlow(UsageResponse.EMPTY)
  val usage: StateFlow<UsageResponse> get() = stateUsage

  private val stateFiles = MutableStateFlow<List<Resource>>(emptyList())
  val files: StateFlow<List<Resource>> get() = stateFiles

  init {
    viewModelScope.launch {
      stateUsage.emit(repository.getUsage())
    }

    viewModelScope.launch {
      path.collect { path ->
        repository.getResource(path).also { resources ->
          stateFiles.emit(resources)
        }
      }
    }
  }

  fun go(path: String) {
    viewModelScope.launch {
      statePath.emit(path)
    }
  }
}
