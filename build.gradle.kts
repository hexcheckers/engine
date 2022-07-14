import java.io.File
import java.io.FileInputStream
import java.util.*

val prop = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "local.properties")))
}

plugins {
    kotlin("jvm") version "1.6.21"
    `java-library`
    `maven-publish`
}

group = "com.hexcheckers.engine"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
    testImplementation(kotlin("test"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    disableAutoTargetJvm()
}

tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to project.name,
            "Implementation-Version" to project.version))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.hexcheckers.engine"
            artifactId = "hc-engine"

            from(components["java"])
        }
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/hexcheckers/engine")
            credentials {
                username = prop.getProperty("gpr.user")
                password = prop.getProperty("gpr.key")
            }
        }
    }
}
