package com.merseyside.adapters.feature.sorting

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.contract.ModelListProvider
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.modelList.SortedModelList
import com.merseyside.adapters.utils.list.SortedList
import com.merseyside.adapters.config.contract.UpdateLogicProvider
import com.merseyside.adapters.config.feature.ConfigurableFeature
import com.merseyside.adapters.config.update.sorted.SortedUpdate
import com.merseyside.adapters.config.update.UpdateActions
import com.merseyside.adapters.config.update.UpdateLogic
import com.merseyside.adapters.extensions.recalculatePositions
import com.merseyside.adapters.model.VM

open class SortFeature<Parent, Model> : ConfigurableFeature<Parent, Model, Config<Parent, Model>>(),
    ModelListProvider<Parent, Model>, UpdateLogicProvider<Parent, Model>
        where Model : VM<Parent> {

    override val config: Config<Parent, Model> = Config()
    override lateinit var modelList: SortedModelList<Parent, Model>

    lateinit var comparator: Comparator<Parent, Model>

    override fun prepare(configure: Config<Parent, Model>.() -> Unit) {
        config.apply(configure)
        comparator = config.comparator
    }

    override fun install(adapter: IBaseAdapter<Parent, Model>) {
        super.install(adapter)

        val sortedList = SortedList(modelClass)
        modelList = SortedModelList(sortedList, comparator)

        comparator.workManager = adapter.workManager
        comparator.setOnComparatorUpdateCallback(object : Comparator.OnComparatorUpdateCallback {
            override suspend fun onUpdate(animation: Boolean) {
                modelList.sortedList.recalculatePositions()
            }
        })
    }

    override fun updateLogic(updateActions: UpdateActions<Parent, Model>): UpdateLogic<Parent, Model> {
        return SortedUpdate(updateActions)
    }

    override val featureKey: String = "SortFeature"
}

class Config<Parent, Model>
        where Model : VM<Parent> {

    lateinit var comparator: Comparator<Parent, Model>
}

object Sorting {
    context (AdapterConfig<Parent, Model>) operator fun <Parent,
            Model : VM<Parent>, TConfig : Config<Parent, Model>> invoke(
        config: TConfig.() -> Unit
    ): SortFeature<Parent, Model> {
        return SortFeature<Parent, Model>().also { feature ->
            feature as ConfigurableFeature<Parent, Model, TConfig>
            install(feature, config)
        }
    }
}