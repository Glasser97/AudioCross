package com.grayson.audiocross.domain.login.usecase

import com.grayson.audiocross.domain.login.base.LoginInfoCheckResult

class LoginInfoCheckUseCase {
    fun checkLogin(username: String, password: String): LoginInfoCheckResult {
        if (username.isBlank() || username.length < 5) {
            return LoginInfoCheckResult.NAME_INVALID
        }
        if (password.isBlank() || password.length < 5) {
            return LoginInfoCheckResult.PASSWORD_INVALID
        }
        return LoginInfoCheckResult.PASSED
    }
}