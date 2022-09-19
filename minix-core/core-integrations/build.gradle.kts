dependencies {
    compileOnly(libs.minecraft.api.landsAPI)
    compileOnly(libs.minecraft.api.placeholderAPI)
    compileOnly("net.frankheijden.serverutils:ServerUtils:3.5.3")
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.fvdh.dev/releases")
}
