package com.merseyside.adapters.feature.expanding

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.feature.ConfigurableFeature
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.model.VM

class ExpandFeature<Parent, Model> : ConfigurableFeature<Parent, Model, Config<Parent, Model>>()
        where Model : VM<Parent> {

    override lateinit var config: Config<Parent, Model>
    override val featureKey: String = KEY

    internal lateinit var adapterExpand: AdapterExpand<Parent, Model>

    override fun prepare(configure: Config<Parent, Model>.() -> Unit) {
        config = Config(configure)
    }

    override fun install(
        adapterConfig: AdapterConfig<Parent, Model>,
        adapter: BaseAdapter<Parent, Model>
    ) {
        super.install(adapterConfig, adapter)

        with(config) {
            adapterExpand = AdapterExpand(
                variableId,
                adapterConfig.modelList,
                expandableMode,
                isExpandedEnabled
            )
        }

        adapterConfig.addOnBindItemListener(adapterExpand)
    }

    companion object {
        const val KEY = "ExpandingFeature"
    }
}

class Config<Parent, Model>(
    configure: Config<Parent, Model>.() -> Unit
) where Model : VM<Parent> {

    var variableId: Int = 0
    var expandableMode: ExpandableMode = ExpandableMode.MULTIPLE
    var isExpandedEnabled: Boolean = true

    init {
        apply(configure)
        validate()
    }

    private fun validate() {
        if (variableId == 0) throw IllegalArgumentException("Please set binding id")
    }

}

object Expanding { //TODO find the way how to use NestedAdapterConfig in context. Got an error!
    context (AdapterConfig<Parent, Model>) operator fun
            <Parent, Model : NestedAdapterParentViewModel<out Parent, Parent, *>,
            TConfig : Config<Parent, Model>> invoke(
        config: TConfig.() -> Unit
    ): ExpandFeature<Parent, Model> {
        return ExpandFeature<Parent, Model>().also { feature ->
            feature as ConfigurableFeature<Parent, Model, TConfig>
            install(feature, config)
        }
    }
}