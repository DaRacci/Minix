plugins {
    java
    idea
    `java-library`
    `maven-publish`
    kotlin("jvm")                       version "1.6.0-RC2"
    id("org.jetbrains.dokka")               version "1.5.31"
    kotlin("plugin.serialization")      version "1.5.31"
    id("com.github.johnrengelman.shadow")   version "7.1.0"
}

val apiAndDocs: Configuration by configurations.creating {
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
        attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named(DocsType.SOURCES))
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    }
}

configurations.api {
    extendsFrom(apiAndDocs)
}

dependencies {

    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
    apiAndDocs("net.kyori:adventure-api:4.10.0-SNAPSHOT")
    apiAndDocs("net.kyori:adventure-text-minimessage:4.2.0-SNAPSHOT")
    apiAndDocs("co.aikar:acf-paper:0.5.0-SNAPSHOT")
    apiAndDocs("com.github.stefvanschie.inventoryframework:IF:0.10.3")
    compileOnlyApi("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:1.5.0")
    compileOnlyApi("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:1.5.0")

    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0-RC2")
    compileOnly("org.jetbrains.kotlin:kotlin-reflect:1.6.0-RC2")

    compileOnly("org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.3.1")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.0")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.2-native-mt")

    compileOnly("com.mojang:authlib:2.3.31")
    compileOnly("me.clip:placeholderapi:2.10.10")
    compileOnly("net.pl3x.purpur:purpur-api:1.17.1-R0.1-SNAPSHOT")

    testAnnotationProcessor("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation(kotlin("test"))

}

idea.module {
    isDownloadJavadoc = true
    isDownloadSources = true
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
                                + "  - org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0-RC2\n"
                                + "  - org.jetbrains.kotlin:kotlin-reflect:1.6.0-RC2\n"
                                + "  - org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.3.1\n"
                                + "  - org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.0\n"
                                + "  - org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt\n"
                                + "  - org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.2-native-mt"
                )}
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
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
        destinationDirectory.set(File("${System.getProperty("user.home")}/Desktop/Minecraft/Sylph/Development/plugins/"))
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

    dokkaGfm {
        outputDirectory.set(File("$buildDir/../docs"))
    }

    artifacts {
        archives(sourcesJar)
        archives(javadocJar)
    }

    build {
        dependsOn(publishToMavenLocal)
        dependsOn(devServer)
    }

}

configure<PublishingExtension> {
    repositories.maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/DaRacci/RacciCore")
        //credentials(PasswordCredentials::class)
        credentials {
            username = System.getenv("USERNAME") ?: findProperty("USERNAME").toString()
            password = System.getenv("TOKEN") ?: findProperty("TOKEN").toString()
        }
    }
    publications.create<MavenPublication>("maven") {
        from(components["java"])
        artifact(tasks["sourcesJar"])
        artifact(tasks["javadocJar"])
        pom {
            val projectGitUrl = "http://github.com/DaRacci/RacciCore"
            name.set(rootProject.name)
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
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
    maven("https://libraries.minecraft.net/")
    maven("https://repo.pl3x.net/")
    maven("https://dl.bintray.com/kotlin/kotlin-dev/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

group = findProperty("group")!!
version = findProperty("version")!!