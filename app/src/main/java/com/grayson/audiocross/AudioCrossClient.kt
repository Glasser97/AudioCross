package com.grayson.audiocross

import com.grayson.audiocross.data.albumlist.clients.AppClient
import com.grayson.audiocross.data.albumlist.clients.GlobalProperties
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import java.net.Proxy


/**
 * Ktor Client
 */
class AudioCrossClient(
    token: String? = null,
    proxy: Proxy? = Proxy.NO_PROXY,
    logger: Logger? = Logger.DEFAULT,
) {
    init {
        // setting token
        GlobalProperties.Config.AccessToken = token
        // setting proxy
        GlobalProperties.Config.GlobalProxy = proxy ?: Proxy.NO_PROXY
        // setting logger
        GlobalProperties.Config.GlobalLogger = logger ?: Logger.DEFAULT
    }

    inner class Config {
        fun setProxy(proxy: Proxy) {
            GlobalProperties.Config.GlobalProxy = proxy
        }

        fun setUserAgent(userAgent: String) {
            GlobalProperties.Config.UserAgent = userAgent
        }
    }

    inner class App {
        private val appClient: AppClient = AppClient()
    }

}