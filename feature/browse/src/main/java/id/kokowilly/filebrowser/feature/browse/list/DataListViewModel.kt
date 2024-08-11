package id.kokowilly.filebrowser.feature.browse.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.kokowilly.filebrowser.lib.network.api.UsageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class DataListViewModel(
  private val repository: ResourceRepository
) : ViewModel() {
  private val statePath = MutableStateFlow("")
  val path: StateFlow<String> = statePath

  private val stateUsage = MutableStateFlow(UsageResponse.EMPTY)
  val usage: StateFlow<UsageResponse> = stateUsage

  private val stateFiles = MutableStateFlow<List<Resource>>(emptyList())
  val files: StateFlow<List<Resource>> = stateFiles

  init {
    viewModelScope.launch {
      stateUsage.emit(repository.getUsage())
    }

    viewModelScope.launch {
      path.collect {
        repository.getResource(it).also {
          stateFiles.emit(it)
        }
      }
    }
  }
}

