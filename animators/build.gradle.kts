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
    androidLibs.appCompat
)

dependencies {
    implementation(projects.utils)
    api(common.merseyLib.time)

    android.forEach { lib -> implementation(lib) }
}