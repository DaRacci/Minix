subprojects {

    group = "dev.racci.minix"

    dependencies {
        compileOnly(project(":Minix-API"))
    }

    java.withSourcesJar()
}
