package com.grayson.audiocross.data.login.repository

import com.grayson.audiocross.data.login.client.AuthClient
import com.grayson.audiocross.domain.common.RequestResult
import com.grayson.audiocross.domain.login.model.LoginToken
import com.grayson.audiocross.domain.login.repository.ILoginRepository
import com.grayson.audiocross.domain.login.usecase.LoginUseCase

class LoginRepository : ILoginRepository {
    override suspend fun login(loginInfo: LoginUseCase.Param): RequestResult<LoginToken> {
        return when (val dataResult = AuthClient().login(loginInfo.username, loginInfo.password)) {
            is RequestResult.Success -> {
                RequestResult.Success(LoginToken(dataResult.data.token))
            }
            is RequestResult.Error -> dataResult
        }
    }
}