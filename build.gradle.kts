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
  implementation("com.github.romankh3:image-comparison:4.4.0")
  implementation("dev.brachtendorf:JImageHash:1.0.0")
  implementation("org.openjfx:javafx:18.0.1")
  implementation("dev.kord:kord-core:0.8.0-M15")
  implementation("io.ktor:ktor-client-core:2.0.3")
  implementation("io.ktor:ktor-client-cio:2.0.3")
  implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.3")
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
