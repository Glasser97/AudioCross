package com.grayson.audiocross.presentation.login.model

data class LoginUiState(
    var username: String = "",
    var password: String = "",
    var isLoading: Boolean = false,
    var isLogin: Boolean = false,
    var message: String = "",
)
