package com.grayson.audiocross.domain.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> io(block: suspend (CoroutineScope.() -> T)): T {
    return withContext(Dispatchers.IO, block)
}