import com.android.build.gradle.BaseExtension

version = 8

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
    sourceSets.getByName("main").java.srcDir(releaseSources)
    testOptions.unitTests.isReturnDefaultValues = true
}
tasks.named("preBuild") { dependsOn(prepareReleases) }

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

cloudstream {
    description = "Unofficial Cloudstream extension for the Muhn Pace English dub fan edit by Muhny."
    authors = listOf("lincutto")
    status = 1
    tvTypes = listOf("TvSeries")
    language = "en"
}
