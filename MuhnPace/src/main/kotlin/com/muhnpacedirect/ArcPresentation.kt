package com.muhnpacedirect

/** Editorial presentation only. Collection identities and streaming stay in the provider. */
internal data class ArcPresentation(
    val synopsis: String,
    val posterUrl: String? = null,
    val backgroundUrl: String? = null,
    val fallbackPosterUrl: String? = null,
    val originalArcEpisodes: OriginalEpisodeMapping? = null,
    val tags: List<String> = listOf("Anime", "Action", "Adventure", "Fantasy"),
) {
    val poster: String? get() = posterUrl ?: fallbackPosterUrl
    val background: String? get() = backgroundUrl ?: poster
    val episodeFallback: String? get() = background ?: poster

    fun description(availableEdits: Int): String {
        require(availableEdits >= 0)
        val availability = if (availableEdits == 1) "1 Muhn Pace edit available" else "$availableEdits Muhn Pace edits available"
        val coverage = originalArcEpisodes?.let {
            // Arc coverage is context, never a mapping for an individual fan edit.
            "Original One Piece arc: episodes ${it.label}. Individual edit mappings may be unverified."
        }
        return listOfNotNull(
            synopsis,
            "$availability • English dub. Numbers below identify Muhn Pace edits.",
            coverage,
        ).joinToString("\n\n")
    }
}

internal object ArcPresentations {
    // Original thematic fallbacks, pinned to immutable, independently verified PNGs.
    // No external artwork service requests or credentials are needed while browsing.
    private const val ARTWORK_BASE = "https://raw.githubusercontent.com/lincutto/MuhnPaceCloudstream/" +
        "f3e3a8e88abb6d7792daf95a6ef3802e18112a63/assets/artwork"

    private fun presentation(artwork: String, synopsis: String) = ArcPresentation(
        synopsis = synopsis,
        fallbackPosterUrl = "$ARTWORK_BASE/$artwork-poster.png",
        backgroundUrl = "$ARTWORK_BASE/$artwork-background.png",
    )

    // Self-authored, premise-only summaries. No episode-level plot is inferred from them.
    val byCollection: Map<String, ArcPresentation> = mapOf(
        "6VyLVkB2" to presentation("enies-lobby",
            "The Straw Hats pursue Robin to Enies Lobby, the World Government's heavily guarded judicial island. " +
                "A rescue mission puts the crew's resolve and their bonds to the test.",
        ),
        "jVhFqouN" to presentation("post-enies-lobby",
            "Back in Water Seven, the crew catches its breath as familiar faces arrive and news from the wider world " +
                "shapes the next stage of their voyage.",
        ),
        "NwJKTg21" to presentation("thriller-bark",
            "A journey through the fog brings the Straw Hats to Thriller Bark, a vast ship filled with strange creatures " +
                "and disappearing shadows. A mysterious musician becomes part of their eerie adventure.",
        ),
        "KztkSCuY" to presentation("sabaody",
            "The route to the New World takes the crew to Sabaody's enormous mangrove groves. While preparing their ship " +
                "for an underwater passage, they encounter other rising pirates and the island's dangerous social order.",
        ),
        "TuZJgwRL" to presentation("amazon-lily",
            "Separated from his crew, Luffy arrives on Amazon Lily, an isolated island of warriors where men are forbidden. " +
                "He must find his bearings and a way to continue his journey.",
        ),
        "4AN9Xxht" to presentation("impel-down",
            "Luffy enters Impel Down in a desperate attempt to reach Ace. The enormous prison's guarded levels, " +
                "unfamiliar dangers and unexpected encounters stand between him and his goal.",
        ),
        "oa8oZvZx" to presentation("marineford",
            "Ace's scheduled execution draws powerful pirates and the Navy to Marineford. Luffy races into a vast " +
                "conflict where reaching his brother is the only objective that matters to him.",
        ),
        "MH7AQkgr" to presentation("post-marineford",
            "In the aftermath of Marineford, Luffy reflects on his past and the people closest to him. " +
                "The story turns toward what it will take for the crew to continue their voyage.",
        ),
        "ZQhstabS" to presentation("fishman-island",
            "The reunited Straw Hats descend beneath the sea to Fishman Island. An extraordinary underwater kingdom " +
                "welcomes them into tensions shaped by the long relationship between humans and fish-men.",
        ),
        "kuoQYqfR" to presentation("fishman-special",
            "A separately listed English-dubbed version of Fishman Island edit 15, labelled with a corrected group shot. " +
                "It belongs to the same underwater adventure as the main Fishman Island collection.",
        ),
        "A1jFtPvQ" to presentation("punk-hazard",
            "A distress call leads the Straw Hats to Punk Hazard, an island divided between fire and ice. " +
                "An abandoned research site and its inhabitants draw the crew into a dangerous mystery.",
        ),
        "f8HTXmAk" to presentation("dressrosa",
            "The Straw Hats visit Dressrosa, where living toys share the streets and a colosseum attracts fighters " +
                "from across the seas. Their mission brings them into conflict with the kingdom's powerful ruler.",
        ),
        "totP9Xmm" to presentation("zou",
            "The search for their companions brings the crew to Zou, a homeland carried on the back of a giant elephant. " +
                "Meeting its inhabitants reveals new questions about the journey ahead.",
        ),
        "AyvkmFFZ" to presentation("whole-cake",
            "Luffy and a small rescue party enter Big Mom's food-themed territory to bring Sanji back. " +
                "An arranged wedding and the rules of a powerful pirate family complicate their mission.",
        ),
        "QZw3Ejpy" to presentation("wano-acts-1-2",
            "The Straw Hats arrive in Wano, a closed country of samurai under harsh rule. Separated on arrival, " +
                "they explore its towns and countryside while their allies prepare a plan against Kaido.",
        ),
        "sF9stHJo" to presentation("wano-act-3",
            "The alliance's plans converge on Onigashima as Kaido's forces gather for a festival. " +
                "This collection continues the later Muhn Pace edits of Wano's third act.",
        ),
    )
}
