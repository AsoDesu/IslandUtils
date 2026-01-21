plugins {
    id("fabric-loom") version "1.10-SNAPSHOT"
    id("maven-publish")
}

version = "${properties["mod_version"]!!}${extraVersion()}"
group = properties["maven_group"]!!
val targetJavaVersion = 21

repositories {
    mavenCentral()
    maven("https://maven.shedaniel.me/")
    maven("https://jitpack.io")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.isxander.dev/snapshots")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven(uri("https://maven.noxcrew.com/public"))
    maven(uri("https://maven.enginehub.org/repo/"))
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = uri("https://api.modrinth.com/maven")
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    // Minecraft
    minecraft("com.mojang:minecraft:${properties["minecraft_version"]!!}")
    mappings(loom.officialMojangMappings())

    // Fabric
    modImplementation("net.fabricmc:fabric-loader:${properties["loader_version"]!!}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${properties["fabric_version"]!!}")

    // Other mods
    modApi("dev.isxander:yet-another-config-lib:${properties["yacl_version"]!!}") {
        exclude(group = "com.twelvemonkeys.common")
        exclude(group = "com.twelvemonkeys.imageio")
    }
    modApi("com.terraformersmc:modmenu:${properties["mod_menu_version"]!!}")
    modApi("com.noxcrew.noxesium:fabric:${properties["noxesium_version"]!!}")

    // Other libraries
    include("com.github.JnCrMx:discord-game-sdk4j:v1.0.0")
    implementation("com.github.JnCrMx:discord-game-sdk4j:v1.0.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${properties["archives_base_name"]!!}" }
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

loom {
    accessWidenerPath.set(file("src/main/resources/islandutils.accesswidener"))

    mixin {
        defaultRefmapName.set("islandutils.refmap.json")
    }
}

fun extraVersion(): String {
    val env = System.getenv()
    return if (env["CI"] == "true") {
        val branch = env["GITHUB_REF"]!!.substringAfterLast("/")
        val runNumber = env["GITHUB_RUN_NUMBER"]!!
        "-pre+$runNumber-$branch"
    } else {
        "+${properties["minecraft_version"]}"
    }
}