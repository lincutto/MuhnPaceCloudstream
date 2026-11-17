version = 7

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

cloudstream {
    description = "Unofficial Cloudstream extension for the Muhn Pace English dub fan edit by Muhny."
    authors = listOf("lincutto")
    status = 1  // working; promoted from the Fire TV-tested preview v6
    tvTypes = listOf("TvSeries")
    language = "en"
}
