plugins {
    kotlin("jvm")
    id("java-library")
    id("com.github.johnrengelman.shadow")
    id("maven-publish")
    id("java")
}

group = "${gradle.rootProject.group}"
version = "0.0.3"

dependencies {

    api("co.aikar:acf-paper:0.5.0-SNAPSHOT")
    api("com.github.Archy-X:SmartInvs:v1.2.8")
    api("dev.triumphteam:triumph-gui:3.0.3")

}