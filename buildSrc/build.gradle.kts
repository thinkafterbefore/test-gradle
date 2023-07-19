import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("stdlib", "1.5.21")) // TODO why can't I upgrade this?
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.3")
    implementation("com.asana:asana:1.0.0")
    constraints {
        implementation("com.google.guava:guava") {
            version {
                strictly("31.1-jre")
            }
        }
    }
}
