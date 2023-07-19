plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
    kotlin("plugin.serialization") version Versions.kotlin
    id("androidx.navigation.safeargs.kotlin")
    id("org.jlleitschuh.gradle.ktlint") version Versions.ktlintGradle
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.app"

    compileSdk = Apps.compileSdk
    buildToolsVersion = Apps.buildToolsVersion

    // This is to suppress NDK-related warnings for local builds. Those warnings do not occur on CI.

    defaultConfig {
        applicationId = Apps.applicationId
        minSdk = Apps.minSdk
        targetSdk = Apps.targetSdk

        versionName = "1"
        versionCode = 1

        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"

        javaCompileOptions.annotationProcessorOptions.arguments(
            mapOf(
                "room.schemaLocation" to "$projectDir/schemas",
                "room.incremental" to "true"
            )
        )

        vectorDrawables {
            useSupportLibrary = true
        }

        testBuildType = BuildType.DEBUG
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        animationsDisabled = true
    }

    buildTypes {
        getByName(BuildType.RELEASE) {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), file("proguard-rules.pro"))
            ndk {
                abiFilters.add("armeabi-v7a")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlin.time.ExperimentalTime",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlin.ExperimentalUnsignedTypes"
        )
        allWarningsAsErrors = true
    }

    packagingOptions {
        resources.pickFirsts += "META-INF/INDEX.LIST"
        resources.pickFirsts += "META-INF/kotlinx-io.kotlin_module"
        resources.pickFirsts += "META-INF/kotlinx-serialization-runtime.kotlin_module"
        resources.pickFirsts += "META-INF/kotlinx-coroutines-io.kotlin_module"
        resources.pickFirsts += "META-INF/io.netty.versions.properties"

        // For instrumented tests
        resources.excludes += "META-INF/AL2.0"
        resources.excludes += "META-INF/LGPL2.1"
        resources.excludes += "META-INF/LICENSE.md"
        resources.excludes += "META-INF/LICENSE-notice.md"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    testOptions {
        managedDevices {
            devices {
                maybeCreate<com.android.build.api.dsl.ManagedVirtualDevice>("pixel2api30").apply {
                    // Use device profiles you typically see in Android Studio.
                    device = "Pixel 2"
                    // Use only API levels 27 and higher.
                    apiLevel = 30
                    // To include Google services, use "google".
                    systemImageSource = "google-atd"
                }
            }
        }
    }

    androidResources {
        noCompress(".unity3d", ".ress", ".resource", ".obb")
        ignoreAssetsPattern = "!.git:!.ds_store:!thumbs.db:!*~"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    maxHeapSize = "768m"
}

kapt {
    correctErrorTypes = true
    useBuildCache = false
}

dependencies {
    implementation("androidx.work:work-testing:2.8.1")
    // Local files
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    // Kotlin
    implementation(kotlin("stdlib", Versions.kotlin))
    implementation(kotlin("reflect", Versions.kotlin))

    // KotlinX Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // KotlinX Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.3")

    // Dependency injection
    implementation("com.google.dagger:hilt-android:${Versions.hilt}")
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.hilt}")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    // Android
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.2.2")
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // AndroidX
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation("androidx.exifinterface:exifinterface:1.3.6")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.camera:camera-view:1.2.2")
    implementation("androidx.camera:camera-lifecycle:1.2.2")
    implementation("androidx.camera:camera-camera2:1.2.2")

    // AndroidX Jetpack Compose
    implementation("androidx.compose.ui:ui:${Versions.compose}")
    implementation("androidx.activity:activity-compose:1.7.1")
    implementation("androidx.compose.material:material:${Versions.compose}")
    implementation("androidx.compose.material3:material3:1.1.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.compose.runtime:runtime-livedata:${Versions.compose}")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.compose}")
    implementation("androidx.compose.material:material-icons-extended:${Versions.compose}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.compose}")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.compose}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Versions.compose}")

    // AndroidX Room
    implementation("androidx.room:room-runtime:${Versions.room}")
    implementation("androidx.room:room-ktx:${Versions.room}")
    kapt("androidx.room:room-compiler:${Versions.room}")
    androidTestImplementation("androidx.room:room-testing:${Versions.room}")
    implementation("androidx.sqlite:sqlite:2.3.1")

    // AndroidX ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.1")
    kapt("androidx.lifecycle:lifecycle-common-java8:2.6.1")

    // AndroidX Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    // Markdown support
    implementation("io.noties.markwon:core:4.6.2")
    implementation("io.noties.markwon:image:4.6.2")
    implementation("io.noties.markwon:ext-tables:4.6.2")

    // Lottie animations
    implementation("com.airbnb.android:lottie:5.2.0")

    // Ktor Client
    implementation("io.ktor:ktor-client-android:2.0.3")
    implementation("io.ktor:ktor-client-logging-jvm:2.2.2")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
    implementation("io.ktor:ktor-client-encoding-jvm:2.2.2")

    // Ktor Server
    implementation("io.ktor:ktor:2.0.3")
    implementation("io.ktor:ktor-server-netty:2.0.3")

    // Mixpanel
    implementation(project(":mixpanel-android"))

    // Personalization
    implementation("org.tensorflow:tensorflow-lite:2.9.0")

    // QR codes
    implementation("com.google.mlkit:barcode-scanning:17.1.0")
    implementation("com.google.zxing:core:3.4.1")

    // Unit Tests
    testImplementation("org.junit.platform:junit-platform-runner:1.9.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("com.willowtreeapps.assertk:assertk:0.25")
    testImplementation("org.json:json:20220924")
    testImplementation("pro.felixo:csv-kotlin:1.0.0")
    testImplementation("io.ktor:ktor-client-mock:2.0.3")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("io.ktor:ktor-server-content-negotiation:2.0.3")
    testImplementation("io.ktor:ktor-server-compression:2.0.3")

    // Android Tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("io.mockk:mockk-android:1.13.5")
    androidTestImplementation("com.willowtreeapps.assertk:assertk:0.25")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.3")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestUtil("androidx.test:orchestrator:1.4.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
}
