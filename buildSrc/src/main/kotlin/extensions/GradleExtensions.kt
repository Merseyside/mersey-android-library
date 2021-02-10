package extensions

import AndroidLibrary
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.internal.impldep.com.amazonaws.services.kms.model.NotFoundException
import org.gradle.kotlin.dsl.accessors.runtime.addDependencyTo

inline fun <reified T> Project.findTypedProperty(propertyName: String): T {

    val stringProperty = findProperty(propertyName) as? String

    return stringProperty?.let {
        when (T::class) {
            Boolean::class -> stringProperty.toBoolean()
            Int::class -> stringProperty.toInt()
            Float::class -> stringProperty
            else -> it
        }
    } as? T ?: throw NotFoundException("Property $propertyName not found")
}

fun Project.isLocalDependencies(): Boolean =
    findTypedProperty("build.localDependencies")

fun DependencyHandler.androidImplementation(dependencyNotation: AndroidLibrary): Dependency? =
    add("implementation", dependencyNotation.name)

fun DependencyHandler.androidImplementation(
    dependencyNotation: AndroidLibrary,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "implementation", dependencyNotation.name, dependencyConfiguration
) as ExternalModuleDependency