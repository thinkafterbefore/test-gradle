plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.pinterest.ktlint:ktlint-core:${Versions.ktlint}")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("com.pinterest.ktlint:ktlint-core:${Versions.ktlint}")
    testImplementation("com.pinterest.ktlint:ktlint-test:${Versions.ktlint}")
}
