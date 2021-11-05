plugins {
    java
    `java-library`
    `maven-publish`
    kotlin("jvm")                       version "1.6.0-RC"
    id("org.jetbrains.dokka")               version "1.5.31"
    kotlin("plugin.serialization")      version "1.5.31"
    id("com.github.johnrengelman.shadow")   version "7.1.0"
}

group = findProperty("group")!!
version = findProperty("version")!!

dependencies {

    api(libs.adventure.api)
    api(libs.adventure.miniMessage)
    api(libs.itemNBTAPI)
    api(libs.acfPaper)
    api(libs.inventoryFramework)
    api(libs.bundles.mcCoroutine)

    compileOnly(libs.authLib)
    compileOnly(libs.purpurAPI)
    compileOnly(libs.placeholderAPI)
    compileOnly(libs.bundles.kotlinLibs)
    compileOnly(libs.bundles.kotlinXLibs)

}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
    // Minecraft AuthLib
    maven("https://libraries.minecraft.net/")
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
    withSourcesJar()
    withJavadocJar()
}

tasks.shadowJar {

    // Hacky way of adding dependencies that are downloaded
    // On launch of the server.
    dependencies {
        exclude(dependency(rootProject.libs.mcCoroutineAPI.get()))
        exclude(dependency(rootProject.libs.mcCoroutineCore.get()))
    }

//    relocate("org.jetbrains", "libraries.jetbrains")
//    relocate("org.intellij", "libraries.intellij")
//    relocate("fonts", "libraries.Fonts")
//    relocate("de.tr7zw.changeme", "libraries.tr7zw.NbtAPI")
//    relocate("de.tr7zw.annotations", "libraries.tr7zw.Annotations")
//    relocate("com.github.stefvanschie", "libraries.stefvanschie.InventoryFramework")
//    relocate("co.aikar.locales", "libraries.aikar.Locales")
//    relocate("co.aikar.commands", "libraries.aikar.Commands")

}

tasks.processResources {
    from(sourceSets.main.get().resources.srcDirs) {
        filesMatching("plugin.yml") {
            val var1    : String ; rootProject.libs.kotlin.stdLib.get().apply               {var1   = "$module:$versionConstraint"}
            val var2    : String ; rootProject.libs.kotlin.reflect.get().apply              {var2   = "$module:$versionConstraint"}
            val var3    : String ; rootProject.libs.kotlinX.coroutinesCore.get().apply      {var3   = "$module:$versionConstraint"}
            val var4    : String ; rootProject.libs.kotlinX.coroutinesJvm.get().apply       {var4   = "$module:$versionConstraint"}
            val var5    : String ; rootProject.libs.kotlinX.serializationJson.get().apply   {var5   = "$module:$versionConstraint"}
            val var6    : String ; rootProject.libs.kotlinX.dateTime.get().apply            {var6   = "$module:$versionConstraint"}
            val var7    : String ; rootProject.libs.mcCoroutineCore.get().apply             {var7   = "$module:$versionConstraint"}
            val var8    : String ; rootProject.libs.mcCoroutineAPI.get().apply              {var8   = "$module:$versionConstraint"}
            expand(
                "version" to project.version,
                "kotlinstdlib" to var1,
                "kotlinreflect" to var2,
                "kotlinXcoroutinesCore" to var3,
                "kotlinXcoroutinesJvm" to var4,
                "kotlinXserializationJson" to var5,
                "kotlinXdateTime" to var6,
                "mcCoroutineCore" to var7,
                "mcCoroutineAPI" to var8,
            )}
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/DaRacci/RacciLib")
            credentials {
                username = rootProject.properties["name"].toString()
                password = rootProject.properties["token"].toString()
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = project.name.toLowerCase()
        }
    }
}

