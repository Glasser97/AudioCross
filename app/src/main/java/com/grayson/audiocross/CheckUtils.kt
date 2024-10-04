package com.grayson.audiocross

import com.grayson.audiocross.data.albumlist.clients.GlobalProperties
import com.grayson.audiocross.domain.exceptions.AccessTokenNotFoundException

fun <T> isNullRun(obj: T?, block: () -> Unit = {}) {
    obj ?: block()
}


fun <T> checkAccessTokenAndRun(block: () -> T): T {
    GlobalProperties.Config.AccessToken ?: throw AccessTokenNotFoundException()
    return block()
}