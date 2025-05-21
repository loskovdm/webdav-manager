package io.github.loskovdm.webdavmanager.server_config

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.github.loskovdm.webdavmanager.R
import io.github.loskovdm.webdavmanager.core.data.repository.ServerRepository
import io.github.loskovdm.webdavmanager.navigation.NavDestination.ServerConfigDestination
import io.github.loskovdm.webdavmanager.server_config.model.ServerConfig
import io.github.loskovdm.webdavmanager.server_config.util.UiText
import io.github.loskovdm.webdavmanager.server_config.util.toServerConfig
import io.github.loskovdm.webdavmanager.server_config.util.toServerEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerConfigViewModel @Inject constructor(
    private val repository: ServerRepository,
    @ApplicationContext private val context: Context,
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
                val config = if (serverId == 0) {
                    ServerConfig(0, "", "", "", "")
                } else {
                    repository.getServerById(serverId)
                        ?.toServerConfig() ?: ServerConfig(0, "", "", "", "")
                }

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

    fun saveConfig(): Boolean {
        val errorName = validateInput(_state.value.name)
        val errorUrl = validateInput(_state.value.url)
        val errorUser = validateInput(_state.value.user)
        val errorPassword = validateInput(_state.value.password)

        if (errorName == null && errorUrl == null && errorUser == null && errorPassword == null) {
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

                    if (currentConfig.id == 0) {
                        repository.insertServer(currentConfig.toServerEntity())
                    } else {
                        repository.updateServer(currentConfig.toServerEntity())
                    }

                    _state.value = _state.value.copy(isSaved = true)
                } catch (e: Exception) {
                    _state.value = _state.value.copy(errorMessage = e.message)
                }
            }
            return true
        } else {
            _state.value = _state.value.copy(
                errorMessage = UiText.StringResource(R.string.error_invalid_input).asString(context)
            )
            return false
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

    fun validateInput(value: String): String? {
        return when {
            value.length > 100 ->
                UiText.StringResource(R.string.error_too_long).asString(context)
            value.contains(Regex("[\\u0000-\\u001F<>\"'`$|*]")) ->
                UiText.StringResource(R.string.error_invalid_chars).asString(context)
            else -> null
        }
    }
}