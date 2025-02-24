plugins {
    id("fabric-loom") version "1.8.10"
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
    implementation("com.github.JnCrMx:discord-game-sdk4j:v0.5.5")
}

tasks.processResources {
    inputs.property("version", project.version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release = targetJavaVersion
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
    withSourcesJar()
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${properties["archives_base_name"]!!}" }
    }
}

loom {
    accessWidenerPath.set(file("src/main/resources/islandutils.accesswidener"))
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