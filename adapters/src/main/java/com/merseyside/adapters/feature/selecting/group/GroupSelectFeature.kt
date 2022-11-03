package com.merseyside.adapters.feature.selecting.group

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.NestedAdapterConfig
import com.merseyside.adapters.config.ext.getAdapterSelect
import com.merseyside.adapters.config.feature.NestedConfigurableFeature
import com.merseyside.adapters.interfaces.nested.INestedAdapter
import com.merseyside.adapters.interfaces.nested.OnInitNestedAdapterListener
import com.merseyside.adapters.feature.selecting.SelectableMode
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.model.VM

class GroupSelectFeature<Parent, Model, InnerData, InnerAdapter> :
    NestedConfigurableFeature<Parent, Model, InnerData, InnerAdapter, SelectingGroupConfig<Parent, Model>>()
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, *> {

    override lateinit var config: SelectingGroupConfig<Parent, Model>
    private lateinit var selectAdapterGroup: SelectableAdapterGroup<InnerData>

    override fun prepare(configure: SelectingGroupConfig<Parent, Model>.() -> Unit) {
        config = SelectingGroupConfig(configure)
        with(config) {
            selectAdapterGroup = SelectableAdapterGroup(selectableMode, isAllowToCancelSelection)
        }
    }

    private val onInitNestedAdapterListener = object : OnInitNestedAdapterListener<InnerData> {
        override fun onInitNestedAdapter(adapter: BaseAdapter<InnerData, *>) {
            val innerAdapterConfig = adapter.adapterConfig
            innerAdapterConfig.getAdapterSelect()?.apply {
                selectableMode = config.selectableMode
                selectAdapterGroup.addAsync(this)
            }
        }
    }

    override fun install(
        adapterConfig: NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>,
        adapter: INestedAdapter<Parent, Model, InnerData, InnerAdapter>
    ) {
        selectAdapterGroup.workManager = adapterConfig.workManager
        adapter.setInitNestedAdapterListener(onInitNestedAdapterListener)
    }

    override val featureKey: String = KEY

    companion object {
        const val KEY = "SelectingGroupFeature"
    }
}

class SelectingGroupConfig<Parent, Model : VM<Parent>>(
    configure: SelectingGroupConfig<Parent, Model>.() -> Unit
) {

    var selectableMode: SelectableMode = SelectableMode.SINGLE
    var isAllowToCancelSelection: Boolean = selectableMode == SelectableMode.MULTIPLE

    init {
        apply(configure)
    }
}

object SelectingGroup {
    context (NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>) operator fun <Parent,
            Model, InnerData, InnerAdapter, TConfig> invoke(
        config: TConfig.() -> Unit
    ): GroupSelectFeature<Parent, Model, InnerData, InnerAdapter>
            where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
                  InnerAdapter : BaseAdapter<InnerData, *>,
                  TConfig : SelectingGroupConfig<Parent, Model> {
        return GroupSelectFeature<Parent, Model, InnerData, InnerAdapter>().also { feature ->
            feature as NestedConfigurableFeature<Parent, Model, InnerData, InnerAdapter, TConfig>
            install(feature, config)
        }
    }
}