rootProject.name = "RacciCore"
val USERNAME: String by settings
val TOKEN   : String by settings

enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/DaRacci/LibraryCatalog")
            credentials {
                username = System.getProperty("USERNAME") ?: USERNAME
                password = System.getProperty("TOKEN")    ?: TOKEN
            }
        }
    }
    versionCatalogs {
        create("libs") {
            from("me.racci:librarycatalog:1.3")
        }
    }
}