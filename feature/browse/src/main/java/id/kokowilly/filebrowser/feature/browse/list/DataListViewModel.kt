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
  private val statePath = MutableStateFlow("/")
  val path: StateFlow<String> = statePath

  private val stateUsage = MutableStateFlow(UsageResponse.EMPTY)
  val usage: StateFlow<UsageResponse> = stateUsage

  init {
    viewModelScope.launch {
      stateUsage.emit(repository.getUsage())
    }
  }
}

data class FileEntity(
  val path: String,
  val name: String,
  val thumbnail: String
)

