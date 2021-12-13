import org.jetbrains.dokka.gradle.DokkaTask
import java.util.*

plugins {
    `maven-publish`
    signing
    id("org.jetbrains.dokka")
}

group = Metadata.groupId
version = Metadata.version

val dokkaHtml by tasks.getting(DokkaTask::class)

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

publishing {
    publications {
        withType<MavenPublication>().all {
            val projectGitUrl = "https://github.com/Merseyside/mersey-kotlin-ext"
            artifact(tasks.named<Jar>("withSourcesJar"))
            artifact(javadocJar)
            pom {
                name.set("Mersey kotlin extensions")
                description.set("Contains some extensions and features on pure kotlin")
                url.set(projectGitUrl)

                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("Merseyside")
                        name.set("Ivan Sablin")
                        email.set("ivanklessablin@gmail.com")
                    }
                }

                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                issueManagement {
                    system.set("GitHub")
                    url.set("$projectGitUrl/issues")
                }

                scm {
                    connection.set("scm:git:$projectGitUrl")
                    developerConnection.set("scm:git:$projectGitUrl")
                    url.set(projectGitUrl)
                }
            }
        }
    }
}

signing {
    val signingKeyId: String? = System.getenv("SIGNING_KEY_ID")
    val signingPassword: String? = System.getenv("SIGNING_PASSWORD")
    val signingKey: String? = System.getenv("SIGNING_KEY")?.let { base64Key ->
        val _base = base64Key.replace("\n", "")
        String(Base64.getDecoder().decode(_base))
    }
    if (signingKeyId != null) {
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        sign(publishing.publications)
    }
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