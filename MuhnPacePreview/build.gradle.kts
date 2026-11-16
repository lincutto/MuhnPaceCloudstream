import com.android.build.gradle.BaseExtension

version = 4

val previewSources = layout.buildDirectory.dir("generated/preview/kotlin")
val generatePreviewSources by tasks.registering(Exec::class) {
    inputs.dir(rootProject.file("MuhnPace/src/main/kotlin"))
    inputs.file(rootProject.file("scripts/prepare_preview.py"))
    outputs.dir(previewSources)
    commandLine("python3", rootProject.file("scripts/prepare_preview.py"), "--out", previewSources.get().asFile)
}

extensions.configure<BaseExtension> {
    namespace = "com.muhnpacepreview"
    sourceSets.getByName("main").java.srcDir(previewSources)
    sourceSets.getByName("test").resources.srcDir(rootProject.file("MuhnPace/src/test/resources"))
    // BasePlugin registration logs through android.util.Log; the registry/builders stay real.
    testOptions.unitTests.isReturnDefaultValues = true
}
tasks.named("preBuild") { dependsOn(generatePreviewSources) }

dependencies {
    testImplementation(project(":MuhnPace"))
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

cloudstream {
    description = "Muhn Pace v3 Preview — isolated Fire TV visual test; separate temporary watch history"
    authors = listOf("Independent prototype")
    status = 3
    tvTypes = listOf("TvSeries")
    language = "en"
}
