package com.muhnpacedirect

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class MuhnPacePlugin : Plugin() {
    override fun load(context: Context) {
        registerMainAPI(MuhnPaceProvider())
    }
}
