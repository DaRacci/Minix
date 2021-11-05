plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("com.github.johnrengelman.shadow")
}

group = rootProject.group
version = "0.1.7"

dependencies {

    api(rootProject.libs.bundles.kyoriAdventure)
    api(rootProject.libs.itemNBTAPI)
    api(rootProject.libs.acfPaper)
    api(rootProject.libs.inventoryFramework)
    api(rootProject.libs.bundles.apacheCommons)
    api(rootProject.libs.bundles.mcCoroutine)
    compileOnly(rootProject.libs.placeholderAPI)
    compileOnly(rootProject.libs.authLib)

}

tasks.shadowJar {

    dependencies {
        exclude(dependency(rootProject.libs.commonsLang3.get()))
        exclude(dependency(rootProject.libs.commonsCollections4.get()))
        exclude(dependency(rootProject.libs.commonsText.get()))
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
            val var7    : String ; rootProject.libs.commonsLang3.get().apply                {var7   = "$module:$versionConstraint"}
            val var8    : String ; rootProject.libs.commonsCollections4.get().apply         {var8   = "$module:$versionConstraint"}
            val var9    : String ; rootProject.libs.commonsText.get().apply                 {var9   = "$module:$versionConstraint"}
            val var10   : String ; rootProject.libs.mcCoroutineCore.get().apply             {var10  = "$module:$versionConstraint"}
            val var11   : String ; rootProject.libs.mcCoroutineAPI.get().apply              {var11  = "$module:$versionConstraint"}
            expand(
                "version" to project.version,
                "kotlinstdlib" to var1,
                "kotlinreflect" to var2,
                "kotlinXcoroutinesCore" to var3,
                "kotlinXcoroutinesJvm" to var4,
                "kotlinXserializationJson" to var5,
                "kotlinXdateTime" to var6,
                "commonslang3" to var7,
                "commonscollections4" to var8,
                "commonstext" to var9,
                "mcCoroutineCore" to var10,
                "mcCoroutineAPI" to var11,
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

