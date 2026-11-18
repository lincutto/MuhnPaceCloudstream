package com.muhnpaceeggheaddev

import com.lagradost.cloudstream3.plugins.BasePlugin
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin

@CloudstreamPlugin
class EggheadDevPlugin : BasePlugin() {
    override fun load() {
        registerMainAPI(EggheadDevProvider())
    }
}
