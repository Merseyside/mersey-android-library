enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }

    val group = "io.github.merseyside"
    val catalogVersions = "1.0.4"
    versionCatalogs {
        create("androidLibs") {
            from("$group:catalog-version-android:$catalogVersions")
        }

        create("common") {
            from("$group:catalog-version-common:$catalogVersions")
        }
    }
}

include(":app")

include(":animators")
include(":adapters")
include(":utils")
include(":archy")

rootProject.name = "mersey-android-library"