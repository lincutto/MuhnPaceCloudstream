package com.muhnpacedirect

// Independently stated acceptance expectations, not production-formatter outputs.
internal fun expectedEpisodeName(fileId: String): String = mapOf(
    "edkjkrVg" to "Secret Plan - original range unresolved",
    "8GdJ6uQg" to "The Operation Fruit - 699-700",
    "Q5RqeqED" to "White Town - 701-702",
    "h795H5E2" to "The Island of Fate: Minion - 703-704",
    "xxL4CmKQ" to "Cora-san - original range unresolved",
    "vSkNFYho" to "Bellamy the Pirate - original range unresolved",
    "U642iqmh" to "The Spear of Elbaf and Don Sai - original range unresolved",
    "TPncrp8t" to "Cabbage and Lomeo - original range unresolved",
)[fileId] ?: "Title unavailable - original episodes unknown"
