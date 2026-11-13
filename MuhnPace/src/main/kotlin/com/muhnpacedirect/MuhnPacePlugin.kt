package com.muhnpacedirect

import com.lagradost.cloudstream3.plugins.BasePlugin
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin

@CloudstreamPlugin
class MuhnPacePlugin : BasePlugin() {
    override fun load() {
        registerMainAPI(MuhnPaceProvider())
    }
}
