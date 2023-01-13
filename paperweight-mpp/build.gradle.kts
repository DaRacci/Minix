plugins {
    java
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.dsl)
    id(libs.plugins.minix.get().pluginId) version "0.5.0"
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
