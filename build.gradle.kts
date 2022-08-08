import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.7.10"
  kotlin("plugin.serialization") version "1.7.10"
  id("com.github.johnrengelman.shadow") version "7.1.2"
  application
}

group = "io.tatumalenko"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
  // dependency injection
  val koinVersion = "3.2.0"
  implementation("io.insert-koin:koin-core:$koinVersion")
  // testImplementation("io.insert-koin:koin-test:$koinVersion")
  // image comparison
  implementation("com.github.romankh3:image-comparison:4.4.0")
  // JImageHash (requires javafx dependencies and javafx sdk)
  implementation("dev.brachtendorf:JImageHash:1.0.0")
  implementation("org.openjfx:javafx:18.0.1")
  // discord client
  implementation("dev.kord:kord-core:0.8.0-M15")
  // http client
  val ktorVersion = "2.0.3"
  implementation("io.ktor:ktor-client-core:$ktorVersion")
  implementation("io.ktor:ktor-client-cio:$ktorVersion")
  implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
  // aws sdk
  val awsSdkVersion = "0.17.2-beta"
  implementation("aws.sdk.kotlin:appconfig:$awsSdkVersion")
  implementation("aws.sdk.kotlin:appconfigdata:$awsSdkVersion")
  implementation("aws.sdk.kotlin:s3:$awsSdkVersion")
  implementation("aws.sdk.kotlin:dynamodb:$awsSdkVersion")
  implementation("aws.sdk.kotlin:iam:$awsSdkVersion")
  implementation("aws.sdk.kotlin:cloudwatch:$awsSdkVersion")
  implementation("aws.sdk.kotlin:sns:$awsSdkVersion")
  implementation("aws.sdk.kotlin:pinpoint:$awsSdkVersion")
  // logging
  implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
  implementation("org.slf4j:slf4j-reload4j:1.7.36") // pulls in slf4j-api
  // kotlin test
  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}

application {
  mainClass.set("MainKt")
}
