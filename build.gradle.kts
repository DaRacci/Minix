plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.6.0"
    id("org.jetbrains.dokka") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "com.sylphmc"
version = "0.3.1"

val transitiveAPI: Configuration by configurations.creating {
    attributes {
        attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    }
    isCanBeResolved = false
    isCanBeConsumed = true
}

configurations.compileOnlyApi {
    extendsFrom(transitiveAPI)
}

val adventureVersion = "4.10.0-SNAPSHOT"

dependencies {

    api("net.kyori:adventure-api:$adventureVersion")
    api("net.kyori:adventure-extra-kotlin:$adventureVersion")
    api("net.kyori:adventure-text-minimessage:$adventureVersion")
    api("co.aikar:acf-paper:0.5.0-SNAPSHOT")
    api("com.github.stefvanschie.inventoryframework:IF:0.10.3")
    transitiveAPI("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:1.5.0")
    transitiveAPI("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:1.5.0")

    transitiveAPI("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")
    transitiveAPI("org.jetbrains.kotlin:kotlin-reflect:1.6.0")

    transitiveAPI("org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.3.1")
    transitiveAPI("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.1")
    transitiveAPI("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt")
    transitiveAPI("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.2-native-mt")

    compileOnly("com.mojang:authlib:2.3.31")
    compileOnly("me.clip:placeholderapi:2.10.10")
    compileOnly("org.purpurmc.purpur:purpur-api:1.18-R0.1-SNAPSHOT")
    compileOnly("org.geysermc.floodgate:api:2.1.1-SNAPSHOT")

    testImplementation("org.jetbrains.kotlin:kotlin-test:1.6.0")

}

tasks {

    test {
        useJUnitPlatform()
    }

    processResources {
        from(sourceSets.main.get().resources.srcDirs) {
            filesMatching("plugin.yml") {
                expand(
                    "version" to project.version,
                    "libraries" to "libraries:\n"
                                + "  - com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:1.5.0\n"
                                + "  - com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:1.5.0\n"
                                + "  - org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0\n"
                                + "  - org.jetbrains.kotlin:kotlin-reflect:1.6.0\n"
                                + "  - org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.3.1\n"
                                + "  - org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.1\n"
                                + "  - org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt\n"
                                + "  - org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.2-native-mt"
                )}
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    compileKotlin {
        kotlinOptions.suppressWarnings = true
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    val devServer by registering(Jar::class) {
        dependsOn(shadowJar)
        destinationDirectory.set(File("${System.getenv("HOME")}/Desktop/Minecraft/DevServer/plugins/"))
        archiveClassifier.set("all")
        from(zipTree(shadowJar.get().outputs.files.singleFile))
    }

    val sourcesJar by registering(Jar::class) {
        dependsOn(JavaPlugin.CLASSES_TASK_NAME)
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    val javadocJar by registering(Jar::class) {
        dependsOn("dokkaJavadoc")
        archiveClassifier.set("javadoc")
        from(dokkaJavadoc.get().outputDirectory)
    }

    dokkaHtml {
        outputDirectory.set(File("$buildDir/../docs"))
    }

    artifacts {
        archives(sourcesJar)
        archives(javadocJar)
    }

    build {
        if(System.getenv("CI") != "true") {
            dependsOn(publishToMavenLocal)
            dependsOn(devServer)
        }
    }

}

configure<PublishingExtension> {
    repositories {

        maven {
            url = uri("https://maven.pkg.github.com/DaRacci/RacciCore")
            credentials(PasswordCredentials::class)
//            credentials {
//                username = System.getenv("USERNAME") ?: findProperty("USERNAME").toString()
//                password = System.getenv("TOKEN") ?: findProperty("TOKEN").toString()
//            }
        }
    }
    publications.create<MavenPublication>("maven") {
        from(components["java"])
        artifact(tasks["sourcesJar"])
        artifact(tasks["javadocJar"])
        groupId = project.group.toString()
        artifactId = project.name.toLowerCase()
        version = project.version.toString()
        pom {
            val projectGitUrl = "http://github.com/DaRacci/RacciCore"
            name.set(project.name)
            description.set(
                "A Spigot library for use with kotlin." +
                        "Providing Coroutines and lots of ASYNC to provide the best performance."
            )
            url.set(projectGitUrl)
            inceptionYear.set("2021")
            developers {
                developer {
                    name.set("Racci")
                    url.set("https://www.github.com/DaRacci")
                }
            }
            licenses {
                license {
                    name.set("GPL-3.0")
                    url.set("https://opensource.org/licenses/GPL-3.0")
                    distribution.set("repo")
                }
            }
            issueManagement {
                system.set("GitHub")
                url.set("$projectGitUrl/issues")
            }
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral {
        mavenContent {
            excludeModule("net.kyori", "adventure-text-minimessage")
        }
    }
    mavenLocal()
    // Kyori
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://jitpack.io")
    // FloodGate
    maven("https://repo.opencollab.dev/maven-snapshots/")
    // AuthLib
    maven("https://libraries.minecraft.net/")
    // Purpur
    maven("https://repo.purpurmc.org")
    // Kotlin
    maven("https://dl.bintray.com/kotlin/kotlin-dev/")
    // ACF
    maven("https://repo.aikar.co/content/groups/aikar/")
    // PlaceholderAPI
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}