rootProject.name = "MuhnPaceCloudstream"
include(":MuhnPace")
// Preview is opt-in; the production build graph remains unchanged.
if (providers.gradleProperty("preview").orNull == "true") include(":MuhnPacePreview")
// Egghead development uses its own plugin, provider and history routes.
if (providers.gradleProperty("eggheadDev").orNull == "true") include(":MuhnPaceEggheadDev")
