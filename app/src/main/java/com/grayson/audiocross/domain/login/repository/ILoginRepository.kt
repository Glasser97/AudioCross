package com.grayson.audiocross.domain.login.repository

import com.grayson.audiocross.domain.common.RequestResult
import com.grayson.audiocross.domain.login.model.LoginToken
import com.grayson.audiocross.domain.login.usecase.LoginUseCase

interface ILoginRepository {
    suspend fun login(loginInfo: LoginUseCase.Param): RequestResult<LoginToken>
}