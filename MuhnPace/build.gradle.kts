version = 2

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

cloudstream {
    description = "English-dubbed Muhn Pace arcs from public Pixeldrain lists (direct HTTPS; subject to host restrictions)"
    authors = listOf("Independent prototype")
    status = 3  // beta: third-party streaming cannot be verified without real-device testing
    tvTypes = listOf("TvSeries")
    language = "en"
}
