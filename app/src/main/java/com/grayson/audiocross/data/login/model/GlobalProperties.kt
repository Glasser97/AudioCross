package com.grayson.audiocross.data.login.model

import io.ktor.client.plugins.logging.*
import java.net.Proxy

internal object GlobalProperties {

    object Config {
        var GlobalProxy: Proxy = Proxy.NO_PROXY
        var GlobalLogger: Logger = Logger.DEFAULT
        var AccessToken: String? = null
        var UserAgent: String = "ktor client"
        val Seed: Int
            get() = (1..99).random()
    }

    object AudioCrossApi {
        const val host = "api.asmr-200.com/api"

        object Path {
            // App
            const val Version = "version"

            // Auth
            const val AuthMe = "auth/me"
            const val Register = "auth/reg"

            // WorksRepo
            const val AllWorks = "search/\$age:general\$ 搞笑"

            // WorkDetail
            const val WorkInfo = "work"
            const val WorkTracks = "tracks"

            // SearchRepo
            const val SearchWorks = "search/\$age:general\$ 搞笑"
        }
    }
}