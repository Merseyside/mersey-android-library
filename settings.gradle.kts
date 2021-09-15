enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }

    versionCatalogs {
        create("androidLibs") {
            from("io.github.merseyside:catalog-version-android:1.0.0")
        }

        create("common") {
            from("io.github.merseyside:catalog-version-common:1.0.0")
        }
    }
}

include(":app")

include(":animators")
include(":adapters")
include(":utils")
include(":archy")

rootProject.name = "mersey-android-library"