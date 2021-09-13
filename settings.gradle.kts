enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }

    versionCatalogs {
        create("androidLibs") {
            from("io.github.merseyside:catalog-version-android:0.1.0")
        }

//        create("common") {
//            from("io.github.merseyside:catalog-version-common:0.1.0")
//        }
    }
}

include(":app")

include(":animators")
include(":adapters")
include(":utils")
include(":archy")

rootProject.name = "mersey-android-library"