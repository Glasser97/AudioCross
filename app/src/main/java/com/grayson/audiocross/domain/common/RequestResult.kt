package com.grayson.audiocross.domain.common

/**
 * Common Result Sealed Class
 */
sealed class RequestResult<out R> {
    data class Success<out T>(val data: T) : RequestResult<T>()

    data class Error(val exception: Exception) : RequestResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}