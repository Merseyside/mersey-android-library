package com.merseyside.adapters.feature.expanding

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.NestedAdapterConfig
import com.merseyside.adapters.config.feature.ConfigurableFeature
import com.merseyside.adapters.config.feature.NestedConfigurableFeature
import com.merseyside.adapters.interfaces.nested.INestedAdapter
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.model.VM

class ExpandFeature<Parent, Model, InnerData, InnerAdapter> :
    NestedConfigurableFeature<Parent, Model, InnerData, InnerAdapter, Config<Parent, Model>>()
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, *> {

    override lateinit var config: Config<Parent, Model>
    lateinit var adapterExpand: AdapterExpand<Parent, Model>

    override fun prepare(configure: Config<Parent, Model>.() -> Unit) {
        config = Config(configure)
    }

    override fun install(
        adapterConfig: NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>,
        adapter: INestedAdapter<Parent, Model, InnerData, InnerAdapter>
    ) {
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

    override val featureKey: String = KEY

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

object Expanding {
    context (NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>) operator fun
            <Parent, Model, InnerData, InnerAdapter, TConfig> invoke(
        config: TConfig.() -> Unit
    ): ExpandFeature<Parent, Model, InnerData, InnerAdapter>
            where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
                  InnerAdapter : BaseAdapter<InnerData, *>,
                  TConfig : Config<Parent, Model> {
        return ExpandFeature<Parent, Model, InnerData, InnerAdapter>().also { feature ->
            feature as ConfigurableFeature<Parent, Model, TConfig>
            install(feature, config)
        }
    }
}