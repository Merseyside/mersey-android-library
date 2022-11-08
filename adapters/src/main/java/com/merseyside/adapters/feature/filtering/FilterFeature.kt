package com.merseyside.adapters.feature.filtering

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.contract.FilterProvider
import com.merseyside.adapters.config.feature.ConfigurableFeature
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import java.lang.NullPointerException
import com.merseyside.adapters.model.VM

open class FilterFeature<Parent, Model> :
    ConfigurableFeature<Parent, Model, Config<Parent, Model>>(), FilterProvider<Parent, Model>
        where Model : VM<Parent> {

    override lateinit var adapterFilter: AdapterFilter<Parent, Model>
    override val config: Config<Parent, Model> = Config()

    override fun prepare(configure: Config<Parent, Model>.() -> Unit) {
        config.apply(configure)
        adapterFilter = config.filter ?: throw NullPointerException("Pass filter instance")
    }

    override fun install(
        adapterConfig: AdapterConfig<Parent, Model>,
        adapter: BaseAdapter<Parent, Model>
    ) {
        super.install(adapterConfig, adapter)
        adapterFilter.workManager = adapter.workManager
    }

    override val featureKey: String = key

    companion object {
        const val key = "FilterFeature"
    }
}

open class Config<Parent, Model> where Model : VM<Parent> {
    open var filter: AdapterFilter<Parent, Model>? = null
}

object Filtering {
    context (AdapterConfig<Parent, Model>) operator fun <Parent,
            Model : VM<Parent>, TConfig : Config<Parent, Model>> invoke(
        config: TConfig.() -> Unit
    ): FilterFeature<Parent, Model> {
        return FilterFeature<Parent, Model>().also { feature ->
            feature as ConfigurableFeature<Parent, Model, TConfig>
            install(feature, config)
        }
    }
}