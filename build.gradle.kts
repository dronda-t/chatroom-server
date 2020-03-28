buildscript {
    val ktorVersion by extra { "1.3.2" }
    val exposedVersion by extra { "0.22.1" }
}

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.70"

    // Apply the application plugin to add support for building a CLI application.
    application
    idea
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Ktor
    implementation("io.ktor:ktor-server-core:${rootProject.extra["ktorVersion"]}")
    implementation("io.ktor:ktor-server-netty:${rootProject.extra["ktorVersion"]}")
    implementation("io.ktor", "ktor-jackson", rootProject.extra["ktorVersion"].toString())

    // Database
    implementation("org.jetbrains.exposed", "exposed-core", rootProject.extra["exposedVersion"].toString())
    implementation("org.jetbrains.exposed", "exposed-dao", rootProject.extra["exposedVersion"].toString())
    implementation("org.jetbrains.exposed", "exposed-jdbc", rootProject.extra["exposedVersion"].toString())
    implementation("org.postgresql", "postgresql", "42.2.2")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.2.3")

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    // Define the main class for the application.
    mainClassName = "io.ktor.server.netty.EngineMain"
}
