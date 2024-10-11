package com.grayson.audiocross.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grayson.audiocross.domain.common.RequestResult
import com.grayson.audiocross.domain.common.io
import com.grayson.audiocross.domain.database.IUserInfoHelper
import com.grayson.audiocross.domain.login.base.LoginInfoCheckResult
import com.grayson.audiocross.domain.login.model.User
import com.grayson.audiocross.domain.login.usecase.LoginInfoCheckUseCase
import com.grayson.audiocross.domain.login.usecase.LoginUseCase
import com.grayson.audiocross.presentation.login.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class LoginViewModel : ViewModel() {

    // region field

    private val useCaseSet = UseCaseSet()

    private val userInfoHelper: IUserInfoHelper by inject(IUserInfoHelper::class.java)

    // endregion

    // region state

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    // endregion

    // region public

    fun login(errorStr: String) {
        val username = _uiState.value.username
        val password = _uiState.value.password
        val checkResult = useCaseSet.loginInfoCheckUseCase.checkLogin(username, password)
        if (checkResult == LoginInfoCheckResult.NAME_INVALID) {
            _uiState.update { it.copy(message = "name is invalid") }
            return
        }
        if (checkResult == LoginInfoCheckResult.PASSWORD_INVALID) {
            _uiState.update { it.copy(message = "password is invalid") }
            return
        }
        if (checkResult == LoginInfoCheckResult.PASSED) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                val result = io {
                    useCaseSet.loginUseCase.login(
                        LoginUseCase.Param(
                            username = username,
                            password = password
                        )
                    )
                }
                when (result) {
                    is RequestResult.Success -> {
                        io {
                            userInfoHelper.insert(
                                User(
                                    username = username, token = result.data.token
                                )
                            )
                        }
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLogin = true
                            )
                        }
                    }

                    is RequestResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                message = errorStr
                            )
                        }
                    }
                }
            }
        }
    }

    fun resetWord() {
        _uiState.update {
            it.copy(
                message = ""
            )
        }
    }

    fun updateUsername(username: String) {
        _uiState.update {
            it.copy(
                username = username
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password
            )
        }
    }

    // endregion

    // region useCaseSet

    private class UseCaseSet {
        val loginInfoCheckUseCase: LoginInfoCheckUseCase by inject(LoginInfoCheckUseCase::class.java)
        val loginUseCase: LoginUseCase by inject(LoginUseCase::class.java)
    }

    // endregion
}