plugins {
    id(Plugins.androidConvention)
    id(Plugins.kotlinConvention)
    id(Plugins.kotlinKapt)
    id(Plugins.kotlinSerialization)
    id(Plugins.mavenPublish)
}

group = Application.groupId
version = Application.version

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

afterEvaluate {
    publishing.publications {
        create<MavenPublication>("release") {
            groupId = group.toString()
            artifactId = project.name
            version = rootProject.version.toString()
            from(components["release"])
        }
    }

    repositories {
        mavenCentral()
    }
}

