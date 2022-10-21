package com.merseyside.adapters.feature.filter

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.contract.FilterProvider
import com.merseyside.adapters.config.feature.ConfigurableFeature
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import java.lang.NullPointerException

class QueryFilterFeature<Parent, Model> :
    ConfigurableFeature<Parent, Model, QueryConfig<Parent, Model>>(),
    FilterProvider<Parent, Model>
        where Model : AdapterParentViewModel<out Parent, Parent> {

    override lateinit var adapterFilter: AdapterFilter<Parent, Model>
    override val config: QueryConfig<Parent, Model> = QueryConfig()

    override fun prepare(configure: QueryConfig<Parent, Model>.() -> Unit) {
        config.apply(configure)
        adapterFilter = config.filter ?: throw NullPointerException("Pass filter instance")
    }

    override fun install(adapter: IBaseAdapter<Parent, Model>) {
        super.install(adapter)
        adapterFilter.workManager = adapter.workManager
    }

    override val featureKey: String = key

    companion object {
        const val key = "FilterFeature"
    }
}

class QueryConfig<Parent, Model>
        where Model : AdapterParentViewModel<out Parent, Parent> {

    var filter: QueryAdapterFilter<Parent, Model>? = null
}

object QueryFiltering {
    context (AdapterConfig<Parent, Model>) operator fun <Parent,
            Model : AdapterParentViewModel<out Parent, Parent>, TConfig : QueryConfig<Parent, Model>> invoke(
        config: TConfig.() -> Unit
    ): QueryFilterFeature<Parent, Model> {
        return QueryFilterFeature<Parent, Model>().also { feature ->
            feature as ConfigurableFeature<Parent, Model, TConfig>
            install(feature, config)
        }
    }
}