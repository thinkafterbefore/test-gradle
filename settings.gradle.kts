pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

include(":mixpanel-android")
include(":custom-ktlint-rules")
include(":app")
include(":rules")

rootProject.name = "Main app"
