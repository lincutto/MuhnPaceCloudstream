package com.muhnpacedirect

/** Stable collection order and navigation origins. These are not original-anime numbers. */
internal data class CatalogueArc(
    val title: String,
    val listId: String,
    val season: Int,
    val firstEdit: Int,
    val seasonTitle: String = title,
) {
    fun navigationIndex(editNumber: Int): Int = (editNumber - firstEdit + 1).also {
        require(it > 0) { "A source insertion before the preserved catalogue needs an explicit identity review" }
    }
}

internal object SeriesCatalogue {
    const val TITLE = "One Piece (Muhn Pace)"
    val arcs = listOf(
        CatalogueArc("Enies Lobby", "6VyLVkB2", 1, 11),
        CatalogueArc("Post Enies Lobby", "jVhFqouN", 2, 4),
        CatalogueArc("Thriller Bark", "NwJKTg21", 3, 1),
        CatalogueArc("Sabaody Archipelago", "KztkSCuY", 4, 1),
        CatalogueArc("Amazon Lily", "TuZJgwRL", 5, 1),
        CatalogueArc("Impel Down", "4AN9Xxht", 6, 1),
        CatalogueArc("Marineford", "oa8oZvZx", 7, 1),
        CatalogueArc("Post Marineford", "MH7AQkgr", 8, 7),
        CatalogueArc("Fishman Island", "ZQhstabS", 9, 3),
        // A separately supplied alternate scene, not a new story arc after Fishman Island.
        CatalogueArc("Fishman Island • Episode 15", "kuoQYqfR", 10, 15, "Fishman Island • Alternate scene"),
        CatalogueArc("Punk Hazard", "A1jFtPvQ", 11, 1),
        CatalogueArc("Dressrosa", "f8HTXmAk", 12, 1),
        CatalogueArc("Zou", "totP9Xmm", 13, 1),
        CatalogueArc("Whole Cake Island", "AyvkmFFZ", 14, 1),
        CatalogueArc("Wano • Acts 1 & 2", "QZw3Ejpy", 15, 5),
        CatalogueArc("Wano • Act 3", "sF9stHJo", 16, 71),
    )
    val poster: String get() = ResearchDataset.SERIES_POSTER_URL
    val background: String get() = ResearchDataset.SERIES_ARTWORK_BASE + "/background.jpg"
    const val ARTWORK_CREDIT = "Anime identification stills: © Eiichiro Oda/Shueisha, Toei Animation. Muhn Pace edit by Muhny. Limited preview excerpts; no promotional-art licence is claimed."

    const val DESCRIPTION = "Monkey D. Luffy and the Straw Hat Pirates explore extraordinary islands in search of the legendary One Piece. " +
        "Follow their adventures in an English-dubbed fan edit that streamlines the anime's pacing."
}
