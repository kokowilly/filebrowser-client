package id.kokowilly.filebrowser.feature.browse.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.kokowilly.filebrowser.lib.network.api.UsageResponse
import id.kokowilly.filebrowser.log.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal open class BrowseViewModel(
  tagName: String = "BrowseViewModel",
  private val repository: ResourceRepository,
) : ViewModel() {
  protected val tag = Tag(tagName)

  private val statePath = MutableStateFlow(PathRequest("/", PathRequest.Origin.System))
  val path: StateFlow<PathRequest> get() = statePath

  private val stateUsage = MutableStateFlow(UsageResponse.EMPTY)
  val usage: StateFlow<UsageResponse> get() = stateUsage

  private val stateFiles = MutableStateFlow<List<Resource>>(emptyList())
  val files: StateFlow<List<Resource>> get() = stateFiles

  private val stateLoading = MutableStateFlow(false)
  val loading: StateFlow<Boolean> get() = stateLoading

  init {
    viewModelScope.launch {
      stateUsage.emit(repository.getUsage())
    }

    viewModelScope.launch {
      path
        .onEach { tag.d("path: $it") }
        .collect { path ->
          stateLoading.emit(true)
          repository.getResource(path.path).also { resources ->
            stateFiles.emit(resources)
          }
          stateLoading.emit(false)
        }
    }
  }

  fun go(request: PathRequest) {
    viewModelScope.launch {
      statePath.emit(request)
    }
  }

  data class PathRequest(val path: String, val origin: Origin) {
    sealed interface Origin {
      data class UI(val timestamp: Long) : Origin
      data object System : Origin
    }
  }
}
