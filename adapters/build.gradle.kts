plugins {
    id(Plugins.androidConvention)
    id(Plugins.kotlinConvention)
    id(Plugins.kotlinKapt)
    id(Plugins.kotlinSerialization)
    id(Plugins.mavenPublishConfig)
}

val android = listOf(
    androidLibs.appCompat,
    androidLibs.material,
    androidLibs.recyclerView,
    androidLibs.coroutines
)

dependencies {
    android.forEach { lib -> implementation(lib) }
    api(androidLibs.paging)
    api(common.merseyLib.time)

    implementation(projects.utils)
}

