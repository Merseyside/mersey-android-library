package com.merseyside.adapters.feature.selecting

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.feature.ConfigurableFeature
import com.merseyside.adapters.extensions.onItemSelected
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.model.VM

class SelectFeature<Parent, Model> : ConfigurableFeature<Parent, Model, Config<Parent, Model>>()
        where Model : VM<Parent> {

    override lateinit var config: Config<Parent, Model>
    override val featureKey: String = KEY

    internal lateinit var adapterSelect: AdapterSelect<Parent, Model>

    override fun prepare(configure: Config<Parent, Model>.() -> Unit) {
        config = Config(configure)
    }

    override fun install(
        adapterConfig: AdapterConfig<Parent, Model>,
        adapter: IBaseAdapter<Parent, Model>
    ) {
        super.install(adapterConfig, adapter)

        with(config) {
            adapterSelect = AdapterSelect(
                adapterConfig.modelList,
                variableId,
                selectableMode,
                isSelectEnabled,
                isAllowToCancelSelection
            )

            adapterSelect.onItemSelected(onSelect)
        }

        adapterConfig.addOnBindItemListener(adapterSelect)
    }

    companion object {
        const val KEY = "SelectFeature"
    }
}

class Config<Parent, Model>(
    configure: Config<Parent, Model>.() -> Unit
) where Model : VM<Parent> {
    var variableId: Int = 0
    var selectableMode: SelectableMode = SelectableMode.SINGLE
    var isSelectEnabled: Boolean = true
    private var _isAllowToCancelSelection: Boolean? = null
        get() = field ?: (selectableMode == SelectableMode.MULTIPLE)

    var isAllowToCancelSelection: Boolean
        set(value) {
            _isAllowToCancelSelection = value
        }
        get() = _isAllowToCancelSelection!!

    var onSelect: (
    item: Parent,
    isSelected: Boolean,
    isSelectedByUser: Boolean
    ) -> Unit = { _, _, _ ->  }

    init {
        apply(configure)
        validate()
    }

    private fun validate() {
        if (variableId == 0) throw IllegalArgumentException("Please set binding id")
    }
}

@Suppress("UNCHECKED_CAST")
object Selecting {
    context (AdapterConfig<Parent, Model>) operator fun <Parent,
            Model : VM<Parent>, TConfig : Config<Parent, Model>> invoke(
        config: TConfig.() -> Unit
    ): SelectFeature<Parent, Model> {
        return SelectFeature<Parent, Model>().also { feature ->
            feature as ConfigurableFeature<Parent, Model, TConfig>
            install(feature, config)
        }
    }
}