package com.merseyside.adapters.feature.filtering

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.contract.FilterProvider
import com.merseyside.adapters.config.feature.ConfigurableFeature
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import java.lang.NullPointerException
import com.merseyside.adapters.model.VM

class QueryFilterFeature<Parent, Model> :
    ConfigurableFeature<Parent, Model, QueryConfig<Parent, Model>>(),
    FilterProvider<Parent, Model>
        where Model : VM<Parent> {

    override lateinit var adapterFilter: QueryAdapterFilter<Parent, Model>
    override val config: QueryConfig<Parent, Model> = QueryConfig()

    override fun prepare(configure: QueryConfig<Parent, Model>.() -> Unit) {
        config.apply(configure)
        adapterFilter = config.filter ?: throw NullPointerException("Pass filter instance")
    }

    override fun install(
        adapterConfig: AdapterConfig<Parent, Model>,
        adapter: IBaseAdapter<Parent, Model>
    ) {
        super.install(adapterConfig, adapter)
        adapterFilter.workManager = adapter.workManager
    }

    override val featureKey: String = key

    companion object {
        const val key = "FilterFeature"
    }
}

class QueryConfig<Parent, Model>
        where Model : VM<Parent> {

    var filter: QueryAdapterFilter<Parent, Model>? = null
}

@Suppress("UNCHECKED_CAST")
object QueryFiltering {
    context (AdapterConfig<Parent, Model>) operator fun <Parent,
            Model : VM<Parent>, TConfig : QueryConfig<Parent, Model>> invoke(
        config: TConfig.() -> Unit
    ): QueryFilterFeature<Parent, Model> {
        return QueryFilterFeature<Parent, Model>().also { feature ->
            feature as ConfigurableFeature<Parent, Model, TConfig>
            install(feature, config)
        }
    }
}