enableFeaturePreview("GRADLE_METADATA")

val properties = startParameter.projectProperties
val libraryPublish: Boolean = properties.containsKey("libraryPublish")

include(":animators")
include(":adapters")
include(":utils")
include(":app")
include(":archy")

include(":kmp-clean-mvvm-arch")
include(":kmp-utils")

rootProject.name = "mersey-android-library"