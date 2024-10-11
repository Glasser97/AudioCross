package com.grayson.audiocross.domain.login.usecase

import com.grayson.audiocross.domain.common.RequestResult
import com.grayson.audiocross.domain.login.model.LoginToken
import com.grayson.audiocross.domain.login.repository.ILoginRepository
import org.koin.java.KoinJavaComponent.inject

/**
 * Login Use Case
 */
class LoginUseCase {
    private val repository: ILoginRepository by inject(ILoginRepository::class.java)

    suspend fun login(param: Param): RequestResult<LoginToken> {
        return repository.login(param)
    }

    data class Param(
        val username: String,
        val password: String
    )
}