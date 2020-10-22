import org.gradle.api.Project
import org.gradle.internal.impldep.com.amazonaws.services.kms.model.NotFoundException

inline fun <reified T> Project.findProperty(propertyName: String): T {

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