subprojects {
    dependencies {
        compileOnly(project(":Minix-API"))
    }

    java.withSourcesJar()
}
