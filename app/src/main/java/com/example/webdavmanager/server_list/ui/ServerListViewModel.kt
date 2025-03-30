package com.example.webdavmanager.server_list.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.webdavmanager.server_list.domain.use_cases.DeleteServerUseCase
import com.example.webdavmanager.server_list.domain.use_cases.GetServersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ServerListViewModel @Inject constructor(
    private val getServersUseCase: GetServersUseCase,
    private val deleteServerUseCase: DeleteServerUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<ServerListState>(ServerListState.Loading)
    val state: StateFlow<ServerListState> = _state.asStateFlow()

    init {
        loadServers()
    }

    fun loadServers() {
        viewModelScope.launch {
            _state.value = ServerListState.Loading

            try {
                val servers = getServersUseCase()
                _state.value = if (servers.isEmpty()) {
                    ServerListState.Empty
                } else{
                    ServerListState.Success(servers)
                }
            } catch (e: Exception) {
                _state.value = ServerListState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun deleteServer(id: Int) {
        viewModelScope.launch {
            try {
                deleteServerUseCase(id)
                loadServers()
            } catch (e: Exception) {
                _state.value = ServerListState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}