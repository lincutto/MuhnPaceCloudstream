import com.android.build.gradle.BaseExtension

version = 2

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

extensions.configure<BaseExtension> {
    namespace = "com.muhnpaceeggheaddev"
    sourceSets.getByName("main").apply {
        manifest.srcFile(rootProject.file("MuhnPace/src/main/AndroidManifest.xml"))
        java.srcDir(baselineSources)
    }
    sourceSets.getByName("test").resources.srcDir(rootProject.file("MuhnPace/src/test/resources"))
    testOptions.unitTests.isReturnDefaultValues = true
}
tasks.named("preBuild") { dependsOn(copyBaseline) }

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
