package com.example.webdavmanager.server_config.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.webdavmanager.navigation.NavDestination.ServerConfigDestination
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
    private val saveServerConfigUseCase: SaveServerConfigUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(ServerConfigState())
    val state: StateFlow<ServerConfigState> = _state.asStateFlow()

    private val serverId: Int = savedStateHandle
        .toRoute<ServerConfigDestination>()
        .serverId

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

    fun validateName(name: String): String? {
        return when {
            name.isEmpty() -> "Cannot be empty"
            name.length < 3 -> "At least 3 characters required"
            name.length > 30 -> "Maximum 30 characters allowed"
            !name.matches(Regex("^[a-zA-Z0-9 ._\\-\\p{IsCyrillic}]+$")) -> "Contains invalid characters"
            else -> null
        }
    }

    fun validateUrl(url: String): String? {
        val protocolPattern = "^https?://.*$"
        val urlPattern = "^https?://[\\w\\.-]+(:\\d+)?(/[\\w\\.-]*)*/?$"

        return when {
            url.isEmpty() -> "Cannot be empty"
            !url.matches(Regex(protocolPattern)) -> "URL must start with http:// or https://"
            !url.matches(Regex(urlPattern)) -> "Invalid URL format"
            else -> null
        }
    }

    fun validateUser(user: String): String? {
        return when {
            user.isEmpty() -> "Cannot be empty"
            user.length > 50 -> "Maximum 50 characters allowed"
            !user.matches(Regex("^[a-zA-Z0-9._@]+$")) -> "Contains invalid characters"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.length > 30 -> "Maximum 30 characters allowed"
            else -> null
        }
    }
}