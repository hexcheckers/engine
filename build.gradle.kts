plugins {
    kotlin("jvm") version "1.6.21"
    `maven-publish`
}

group = "com.hexcheckers.engine"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
    testImplementation(kotlin("test"))
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
                username = project.findProperty("gpr.user").toString()
                password = project.findProperty("gpr.key").toString()
            }
        }
    }
}
