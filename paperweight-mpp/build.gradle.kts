import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `kotlin-dsl`
    `java-gradle-plugin`
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        languageVersion = "1.7"
        jvmTarget = "17"
    }
}

fun Provider<MinimalExternalModuleDependency>.withVersion(version: String): String {
    return "${this.get().module}:$version"
}

dependencies {
    compileOnly(gradleApi())
    compileOnly("org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin:1.7.22")
    compileOnly(libs.gradle.shadow.withVersion("7.0.0"))
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:$embeddedKotlinVersion")
    implementation("org.gradle.kotlin:gradle-kotlin-dsl-plugins:3.2.6")
    implementation("org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:2.1.7")
    implementation("io.papermc.paperweight.userdev:io.papermc.paperweight.userdev.gradle.plugin:1.3.11")
}

gradlePlugin {
    plugins {
        create("paperweight-mpp") {
            id = "dev.racci.paperweight.mpp"
            implementationClass = "dev.racci.paperweight.mpp.PaperweightMppPlugin"
        }
    }
}
