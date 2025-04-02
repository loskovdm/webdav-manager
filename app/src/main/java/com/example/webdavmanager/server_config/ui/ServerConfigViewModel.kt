package com.example.webdavmanager.server_config.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.webdavmanager.server_config.domain.use_cases.GetServerConfigUseCase
import com.example.webdavmanager.server_config.domain.use_cases.SaveServerConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerConfigViewModel @Inject constructor(
    private val getServerConfigUseCase: GetServerConfigUseCase,
    private val saveServerConfigUseCase: SaveServerConfigUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<ServerConfigState>(ServerConfigState.Loading)
    val state: StateFlow<ServerConfigState> = _state.asStateFlow()

    private val serverId: Int = 1 // TODO: Implement navigation
    private val isNewServer: Boolean = (serverId == 0)

    init {
        loadServerConfig()
    }

    fun loadServerConfig() {
        viewModelScope.launch {
            _state.value = ServerConfigState.Loading

            try {
                val serverConfig = getServerConfigUseCase(serverId)
                _state.value = ServerConfigState.Editing(serverConfig, isNewServer)
            } catch (e: Exception) {
                _state.value = ServerConfigState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun saveServerConfig() {
        val currentState = _state.value
        if (currentState is ServerConfigState.Editing) {
            viewModelScope.launch {
                try {
                    saveServerConfigUseCase(currentState.serverConfig)
                } catch (e: Exception) {
                    _state.value = ServerConfigState.Error(e.message ?: "Unknown error occurred")
                }
            }
        }
    }
}