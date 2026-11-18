package com.muhnpaceeggheaddev

internal enum class SourceType(val label: String) {
    MUHN_PACE("Muhn Pace"), ONE_PACE("One Pace"), COMMUNITY_EDIT("Community edit"), ORIGINAL("Original anime")
}

internal data class ExpansionEpisode(
    val season: Int,
    val number: Int,
    val title: String,
    val originalEpisodes: List<Int>,
    val preferred: SourceType,
) {
    val key: String get() = "$season/$number"
}

/** Release facts only. Source approvals and availability are evaluated separately. */
internal object ExpansionMetadata {
    val seasonNames = mapOf(17 to "Egghead")

    private fun onePace(number: Int, title: String, vararg originals: Int) =
        ExpansionEpisode(17, number, title, originals.toList(), SourceType.ONE_PACE)

    // Release order and original-anime coverage from onepace.net/en/watch#egghead.
    // Edits can use different scenes from the same original episode.
    val episodes = listOf(
        onePace(1, "New Emperors", 1086, 1087, 1088),
        onePace(2, "The Matter Involving Captain Koby", 1087, 1088),
        onePace(3, "Future Island Egghead", 1089, 1090),
        onePace(4, "Adventure in the Land of Science", 1091, 1092, 1093),
        onePace(5, "The Six Vegapunks", 1093, 1094, 1095),
        onePace(6, "The Will of Ohara", 1096, 1097),
        onePace(7, "Punk Records", 1097, 1098, 1099),
        onePace(8, "The Strongest Form of Humanity", 1099, 1100, 1101, 1102),
        onePace(9, "The Weight of Memory", 1102, 1103, 1104),
        onePace(10, "Miss Buckingham Stussy", 1104, 1105, 1106, 1107),
        onePace(11, "Labophase Death Game", 1107, 1108, 1109),
        onePace(12, "Escape Limit", 1108, 1110, 1111),
        onePace(13, "The Emperor's Crew, The Red Hair Pirates", 1112),
        onePace(14, "The Legendary Hero", 1113, 1114, 1115),
        onePace(15, "Let's Go and Take It!!", 1116, 1117),
        onePace(16, "The Attempted Murder of a Celestial Dragon", 1118, 1119),
        onePace(17, "The Five Elders", 1119, 1120),
        onePace(18, "The Final Lesson", 1121, 1122),
        onePace(19, "Hostage Situation", 1123, 1124),
        onePace(20, "Sentomaru", 1125, 1126),
        onePace(21, "Luffy vs. Kizaru", 1127, 1128),
        onePace(22, "Kuma Bear", 1129, 1130),
    ) + (1131..1136).map { original ->
        ExpansionEpisode(17, 23 + original - 1131, "Kuma Cut $original", listOf(original), SourceType.COMMUNITY_EDIT)
    } + listOf(
        ExpansionEpisode(17, 29, "Shaved Egghead 1137–1138", listOf(1137, 1138), SourceType.COMMUNITY_EDIT),
        ExpansionEpisode(17, 30, "Shaved Egghead 1139–1140", listOf(1139, 1140), SourceType.COMMUNITY_EDIT),
    ) + (1141..1155).map { original ->
        ExpansionEpisode(17, 31 + original - 1141, "One Piece $original", listOf(original), SourceType.ORIGINAL)
    }
}
