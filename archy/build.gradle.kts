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
    androidLibs.coroutines,
    common.serialization,
    androidLibs.appCompat,
    androidLibs.material,
    androidLibs.rxjava2,
    androidLibs.dagger,
    androidLibs.worker,
    androidLibs.gson,
    androidLibs.keyboard,
    androidLibs.room
)

val androidBundles = listOf(
    androidLibs.bundles.lifecycle,
    androidLibs.bundles.navigation
)

val modulez = listOf(
    projects.utils,
    projects.adapters,
    projects.animators
)

dependencies {
    api(common.merseyLib.time)
    modulez.forEach { module -> implementation(module) }
    android.forEach { lib -> implementation(lib) }
    androidBundles.forEach { bundle -> implementation(bundle) }

    kapt(androidLibs.daggerCompiler)
    kapt(androidLibs.roomCompiler)
}