plugins {
    id(Plugins.androidConvention)
    id(Plugins.kotlinConvention)
    id(Plugins.kotlinKapt)
    id(Plugins.kotlinSerialization)
    `maven-publish-config`
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

afterEvaluate {
    publishing.publications {
        create<MavenPublication>("release") {
            groupId = Metadata.groupId
            artifactId = project.name
            version = Metadata.version
            from(components["release"])
        }
    }

    repositories {
        mavenCentral()
    }
}

