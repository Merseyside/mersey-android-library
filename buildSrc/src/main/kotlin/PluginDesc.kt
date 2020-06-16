import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.api.artifacts.Dependency
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

data class PluginDesc(
    val id: String,
    val module: String? = null,
    val version: String? = null
)

fun DependencyHandlerScope.plugin(pluginDesc: PluginDesc): Dependency? {
    return pluginDesc.module?.let { "classpath"(it) }
}

fun DependencyHandlerScope.plugins(pluginDescList: List<PluginDesc>) {
    pluginDescList
        .distinctBy { it.module }
        .forEach { plugin(it) }
}

fun PluginDependenciesSpec.plugin(pluginDesc: PluginDesc): PluginDependencySpec {
    val spec = id(pluginDesc.id)
    pluginDesc.version?.also { spec.version(it) }
    return spec
}