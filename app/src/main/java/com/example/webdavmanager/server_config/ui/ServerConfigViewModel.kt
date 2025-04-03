package com.example.webdavmanager.server_config.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.webdavmanager.server_config.domain.model.ServerConfig
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
    private val _state = MutableStateFlow(ServerConfigState())
    val state: StateFlow<ServerConfigState> = _state.asStateFlow()

    val serverId = 1

    init {
        loadConfig()
    }

    fun loadConfig() {
        viewModelScope.launch {
            try {
                val config = getServerConfigUseCase(serverId)
                _state.value = ServerConfigState(
                    name = config.name,
                    url = config.url,
                    user = config.user,
                    password = config.password,
                    isNewConfig = config.id == 0
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(errorMessage = e.message)
            }
        }
    }

    fun saveConfig() {
        viewModelScope.launch {
            try {
                val currentState = _state.value
                val currentConfig = ServerConfig(
                    id = serverId,
                    name = currentState.name,
                    url = currentState.url,
                    user = currentState.user,
                    password = currentState.password
                )
                saveServerConfigUseCase(currentConfig)
                _state.value = _state.value.copy(isSaved = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(errorMessage = e.message)
            }
        }
    }

    fun updateName(newName: String) {
        _state.value = _state.value.copy(name = newName, isSaved = false)
    }

    fun updateUrl(newUrl: String) {
        _state.value = _state.value.copy(url = newUrl, isSaved = false)
    }

    fun updateUser(newUser: String) {
        _state.value = _state.value.copy(user = newUser, isSaved = false)
    }

    fun updatePassword(newPassword: String) {
        _state.value = _state.value.copy(password = newPassword, isSaved = false)
    }

    fun clearErrorMessage() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}