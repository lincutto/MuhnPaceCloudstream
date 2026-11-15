rootProject.name = "MuhnPaceCloudstream"
include(":MuhnPace")
// Preview is opt-in; the production build graph remains unchanged.
if (providers.gradleProperty("preview").orNull == "true") include(":MuhnPacePreview")
