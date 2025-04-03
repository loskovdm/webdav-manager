package com.example.webdavmanager.server_list.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.webdavmanager.server_list.domain.use_cases.DeleteServerUseCase
import com.example.webdavmanager.server_list.domain.use_cases.GetServerListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerListViewModel @Inject constructor(
    private val getServersUseCase: GetServerListUseCase,
    private val deleteServerUseCase: DeleteServerUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ServerListState())
    val state: StateFlow<ServerListState> = _state.asStateFlow()

    init {
        loadServerList()
    }

    fun loadServerList() {
        viewModelScope.launch {
            try {
                val serverList = getServersUseCase()
                _state.value = _state.value.copy(serverList = serverList, isLoaded = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(errorMessage = e.message)
            }
        }
    }

    fun deleteServer(id: Int) {
        viewModelScope.launch {
            try {
                deleteServerUseCase(id)
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