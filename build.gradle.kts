import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("fabric-loom") version "1.10-SNAPSHOT"
    java
}

group = property("maven_group")!!
version = "${property("mod_version")}${extraVersion()}"

repositories {
    maven("https://maven.noxcrew.com/public")
    maven("https://maven.enginehub.org/repo/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://api.modrinth.com/maven")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    modApi("com.noxcrew.noxesium:fabric:${property("noxesium_version")}")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
}

loom {
    accessWidenerPath = file("src/main/resources/islandutils.accesswidener")
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(getProperties())
            expand(mutableMapOf("version" to project.version))
        }
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${project.base.archivesName.get()}" }
        }
    }

    compileKotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    compileJava {
        options.release = 21
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

fun extraVersion(): String {
    val env = System.getenv()
    return if (env["CI"] == "true") {
        val branch = env["GITHUB_REF"]!!.substringAfterLast("/")
        val runNumber = env["GITHUB_RUN_NUMBER"]!!
        "-pre+$runNumber-$branch"
    } else {
        "+${property("minecraft_version")}"
    }
}