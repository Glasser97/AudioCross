package com.grayson.audiocross.presentation.navigator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grayson.audiocross.data.login.model.GlobalProperties
import com.grayson.audiocross.domain.database.IUserInfoHelper
import com.grayson.audiocross.domain.login.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

data class AudioCrossUiState(
    val user: User? = null
)

class AudioCrossViewModel : ViewModel() {

    // region field

    private val userInfoHelper: IUserInfoHelper by inject(IUserInfoHelper::class.java)

    private val _uiState = MutableStateFlow(AudioCrossUiState())
    val uiState: StateFlow<AudioCrossUiState> = _uiState
        .onStart { getUser() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(10_000),
            initialValue = AudioCrossUiState()
        )

    // endregion

    // region public

    fun onLogout() {
        viewModelScope.launch {
            val user = uiState.value.user ?: return@launch
            userInfoHelper.delete(user)
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            userInfoHelper.get().collect { user ->
                _uiState.update { state ->
                    if (user != null) {
                        GlobalProperties.Config.AccessToken = user.token
                        state.copy(user = user)
                    } else {
                        state.copy(user = null)
                    }
                }
            }
        }
    }

    // endregion
}