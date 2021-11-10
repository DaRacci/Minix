plugins {
    java
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.6.0-RC2"
    id("org.jetbrains.dokka") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = findProperty("group")!!
version = findProperty("version")!!

dependencies {

    api("net.kyori:adventure-api:4.10.0-SNAPSHOT")
    api("net.kyori:adventure-text-minimessage:4.2.0-SNAPSHOT")
    api("co.aikar:acf-paper:0.5.0-SNAPSHOT")
    api("com.github.stefvanschie.inventoryframework:IF:0.10.3")
    api("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:1.5.0")
    api("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:1.5.0")

    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0-RC2")
    api("org.jetbrains.kotlin:kotlin-reflect:1.6.0-RC2")

    api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.2-native-mt")

    compileOnly("com.mojang:authlib:1.5.21")
    compileOnly("me.clip:placeholderapi:2.10.10")
    compileOnly("net.pl3x.purpur:purpur-api:1.17.1-R0.1-SNAPSHOT")
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
    // Minecraft AuthLib
    maven("https://libraries.minecraft.net/")
    // Purpur
    maven("https://repo.pl3x.net/")
    // Kotlin
    maven("https://dl.bintray.com/kotlin/kotlin-dev/")
    // Aikar Commands API
    maven("https://repo.aikar.co/content/groups/aikar/")
    // NBT API
    maven("https://repo.codemc.org/repository/maven-public/")
    // ProtocolLib
    maven("https://repo.dmulloy2.net/repository/public/")
    // Adventure
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    // PlaceholderAPI
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

java {
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17
}

tasks.processResources {
    from(sourceSets.main.get().resources.srcDirs) {
        filesMatching("plugin.yml") { //            val var1    : String ; rootProject.libs.kotlin.stdLib.get().apply               {var1   = "$module:$versionConstraint"}
            //            val var2    : String ; rootProject.libs.kotlin.reflect.get().apply              {var2   = "$module:$versionConstraint"}
            //            val var3    : String ; rootProject.libs.kotlinX.coroutinesCore.get().apply      {var3   = "$module:$versionConstraint"}
            //            val var4    : String ; rootProject.libs.kotlinX.coroutinesJvm.get().apply       {var4   = "$module:$versionConstraint"}
            //            val var5    : String ; rootProject.libs.kotlinX.serialization.get().apply       {var5   = "$module:$versionConstraint"}
            //            val var6    : String ; rootProject.libs.kotlinX.datetime.get().apply            {var6   = "$module:$versionConstraint"}
            //            val var7    : String ; rootProject.libs.mcCoroutineCore.get().apply             {var7   = "$module:$versionConstraint"}
            //            val var8    : String ; rootProject.libs.mcCoroutineAPI.get().apply              {var8   = "$module:$versionConstraint"}
            expand(
                "version" to project.version,
                "kotlinstdlib" to "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0-RC2",
                "kotlinreflect" to "org.jetbrains.kotlin:kotlin-reflect:1.6.0-RC2", /*"kotlinXcoroutinesCore" to "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt",
                "kotlinXcoroutinesJvm" to "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.2-native-mt",*/
                "kotlinXserializationJson" to "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0", /*"kotlinXdateTime" to "org.jetbrains.kotlinx:kotlinx-datetime:0.3.1",
                "mcCoroutineCore" to "com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:1.5.0",
                "mcCoroutineAPI" to "com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:1.5.0",*/
            )
        }
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

tasks {

    shadowJar {
        archiveClassifier.set("full")
        // Hacky way of adding dependencies that are downloaded
        // On launch of the server.
//        dependencies {
//            exclude(dependency(libs.mcCoroutineAPI.get()))
//            exclude(dependency(libs.mcCoroutineCore.get()))
//            exclude(dependency(libs.mcCoroutineAPI.get()))
//            exclude(dependency(libs.mcCoroutineCore.get()))
//            exclude(dependency(libs.kotlin.stdLib.get()))
//            exclude(dependency(libs.kotlin.reflect.get()))
//            exclude(dependency(libs.kotlinX.datetime.get()))
//            exclude(dependency(libs.kotlinX.coroutinesJvm.get()))
//            exclude(dependency(libs.kotlinX.coroutinesCore.get()))
//            exclude(dependency(libs.kotlinX.serialization.get()))
//        }
    }

    compileKotlin {
        kotlinOptions.suppressWarnings = true
        kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
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
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/DaRacci/RacciCore")
            credentials {
                username = System.getenv("USERNAME") ?: findProperty("USERNAME").toString()
                password = System.getenv("TOKEN") ?: findProperty("TOKEN").toString()
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
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
                scm {
                    connection.set("scm:git:$projectGitUrl")
                    developerConnection.set("scm:git:$projectGitUrl")
                    url.set(projectGitUrl)
                }
            }

            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"]) // artifactId = project.name.toLowerCase()
        }
    }
}
