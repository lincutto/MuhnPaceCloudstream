import com.android.build.gradle.BaseExtension

version = 8

// Compile the exact v7 implementation under a private package. Only the dev entry point is registered.
val baselineSources = layout.buildDirectory.dir("generated/baseline/kotlin")
val copyBaseline by tasks.registering(Sync::class) {
    from(rootProject.file("MuhnPace/src/main/kotlin/com/muhnpacedirect")) {
        include("*.kt")
        exclude("MuhnPacePlugin.kt")
        filter { line: String ->
            if (line == "package com.muhnpacedirect") "package com.muhnpaceeggheaddev.baseline" else line
        }
    }
    filteringCharset = "UTF-8"
    into(baselineSources)
}

val releaseSources = layout.buildDirectory.dir("generated/egghead/kotlin")
val prepareReleases by tasks.registering(Exec::class) {
    val generator = rootProject.file("scripts/prepare_egghead_releases.py")
    val target = releaseSources.get().file("ReviewedReleases.kt").asFile
    inputs.files(generator, rootProject.file("docs/research/egghead-fan-edits.csv"),
        rootProject.file("metadata/egghead-original-anime-titles.csv"),
        rootProject.file("metadata/wano-act-3-coverage.json"), rootProject.file("metadata/phase4b/season-16.json"))
    outputs.file(target)
    commandLine("python3", generator, target)
}

extensions.configure<BaseExtension> {
    namespace = "com.muhnpaceeggheaddev"
    sourceSets.getByName("main").apply {
        manifest.srcFile(rootProject.file("MuhnPace/src/main/AndroidManifest.xml"))
        java.srcDir(baselineSources)
        java.srcDir(releaseSources)
    }
    sourceSets.getByName("test").resources.srcDir(rootProject.file("MuhnPace/src/test/resources"))
    testOptions.unitTests.isReturnDefaultValues = true
}
tasks.named("preBuild") { dependsOn(copyBaseline, prepareReleases) }

dependencies {
    testImplementation(project(":MuhnPace"))
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

cloudstream {
    description = "Isolated Egghead development build for testing the English-dub catalogue."
    authors = listOf("lincutto")
    status = 3
    tvTypes = listOf("TvSeries")
    language = "en"
}
