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
    val poster: String get() = ResearchDataset.ARTWORK_BASE + "/dressrosa/poster.jpg"
    val background: String get() = ResearchDataset.ARTWORK_BASE + "/dressrosa/background.jpg"
    const val ARTWORK_CREDIT = "Anime identification stills: © Eiichiro Oda/Shueisha, Toei Animation. Muhn Pace edit by Muhny. Limited preview excerpts; no promotional-art licence is claimed."

    fun description(available: Int): String = listOf(
        "Monkey D. Luffy and the Straw Hat crew sail in search of the One Piece, facing new islands, rivals and allies. " +
            "English-dubbed fan adaptation; $available playable videos across 16 collection seasons. This is a partial catalogue, beginning at Enies Lobby.",
        "Season labels follow the available collections. Player episode indices are navigation positions; original anime coverage appears in researched titles. " +
            "The alternate Fishman Island season supplements the preceding arc.",
        arcs.joinToString("\n\n") { "Season ${it.season} • ${it.seasonTitle}\n${ArcPresentations.byCollection.getValue(it.listId).synopsis}" },
        ARTWORK_CREDIT,
    ).joinToString("\n\n")
}
