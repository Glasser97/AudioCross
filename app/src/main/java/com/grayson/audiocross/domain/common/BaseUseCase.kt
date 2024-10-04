package com.grayson.audiocross.domain.common


/**
 * Basic Use Case
 */
abstract class BaseUseCase<TParam, TResult>() {
    abstract suspend fun execute(param: TParam): TResult
}