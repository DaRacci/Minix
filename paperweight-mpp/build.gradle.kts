import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `kotlin-dsl`
    `java-gradle-plugin`
}

kotlin {
    explicitApiWarning()
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
    compileOnly(gradleKotlinDsl())
    compileOnly(libs.gradle.shadow)
    compileOnly(libs.gradle.kotlin.mpp)
    compileOnly(libs.gradle.kotlin.jvm)

    implementation(libs.gradle.minecraft.paperweight)
}

gradlePlugin {
    plugins {
        create("paperweight-mpp") {
            id = "dev.racci.paperweight.mpp"
            implementationClass = "dev.racci.paperweight.mpp.PaperweightMppPlugin"
        }
    }
}
