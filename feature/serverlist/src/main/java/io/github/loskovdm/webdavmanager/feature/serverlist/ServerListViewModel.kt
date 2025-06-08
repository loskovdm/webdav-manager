package io.github.loskovdm.webdavmanager.feature.serverlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.loskovdm.webdavmanager.core.data.repository.ServerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.loskovdm.webdavmanager.feature.serverlist.model.asExternalModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerListViewModel @Inject constructor(
    private val repository: ServerRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ServerListState())
    val state: StateFlow<ServerListState> = _state.asStateFlow()

    init {
        loadServerList()
    }

    fun loadServerList() {
        viewModelScope.launch {
            try {
                val serverList = repository.getServerList().map { it.asExternalModel() }
                _state.value = _state.value.copy(serverList = serverList, isLoaded = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(errorMessage = e.message)
            }
        }
    }

    fun deleteServer(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteServerById(id)
                loadServerList()
            } catch (e: Exception) {
                _state.value = _state.value.copy(errorMessage = e.message)
            }
        }
    }

    fun clearErrorMessage() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}