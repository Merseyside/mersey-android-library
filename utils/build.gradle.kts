plugins {
    id(Plugins.androidConvention)
    id(Plugins.kotlinConvention)
    id(Plugins.kotlinKapt)
    id(Plugins.kotlinSerialization)
    id(Plugins.mavenPublishConfig)
}

android {
    buildFeatures.dataBinding = true
}

val android = listOf(
    common.serialization,
    common.reflect,
    androidLibs.coroutines,
    androidLibs.appCompat,
    androidLibs.material,
    androidLibs.paging,
    androidLibs.billing,
    androidLibs.publisher,
    androidLibs.oauth2,
    androidLibs.playCore,
    androidLibs.coil,
    androidLibs.location
)

dependencies {

    api(common.merseyLib.time)

    if (isLocalKotlinExtLibrary()) {
        api(project(Modules.MultiPlatform.MerseyLibs.kotlinExt))
    } else {
        api(common.merseyLib.kotlin.ext)
    }

    android.forEach { lib -> implementation(lib) }
}