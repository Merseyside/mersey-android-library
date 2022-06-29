
import com.android.build.gradle.BaseExtension

plugins {
    //id("org.jetbrains.dokka")
    id("maven-publish-config")
}

//val dokkaHtml by tasks.getting(DokkaTask::class)
//
//val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
//    dependsOn(dokkaHtml)
//    archiveClassifier.set("javadoc")
//    from(dokkaHtml.outputDirectory)
//}

afterEvaluate {
    val androidSourcesJar = tasks.create("androidSourcesJar", Jar::class) {
        archiveClassifier.set("sources")
        val androidExtension: BaseExtension =
            this.project.extensions.getByType(BaseExtension::class)
        from(androidExtension.sourceSets.getByName("main").java.srcDirs)
    }

    publishing.publications {
        create("release", MavenPublication::class.java) {
            from(components.getByName("release"))

            artifact(androidSourcesJar)
        }
    }

//    publishing.publications {
//        create<MavenPublication>("release") {
//            groupId = Metadata.groupId
//            artifactId = project.name
//            version = Metadata.version
//            from(components["release"])
//        }
//    }

//    repositories {
//        mavenCentral()
//    }
}