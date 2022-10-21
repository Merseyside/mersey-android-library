package com.merseyside.adapters.config

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.base.config.ext.getFeatureByKey
import com.merseyside.adapters.base.config.ext.hasFeature
import com.merseyside.adapters.config.contract.ModelListProvider
import com.merseyside.adapters.config.contract.UpdateLogicProvider
import com.merseyside.adapters.config.feature.ConfigurableFeature
import com.merseyside.adapters.config.feature.Feature
import com.merseyside.adapters.feature.filter.FilterFeature
import com.merseyside.adapters.config.update.simple.SimpleUpdate
import com.merseyside.adapters.feature.filter.listManager.FilterListManager
import com.merseyside.adapters.feature.filter.listManager.FilterNestedListManager
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.interfaces.nested.INestedAdapter
import com.merseyside.adapters.listManager.impl.ListManager
import com.merseyside.adapters.listManager.impl.NestedListManager
import com.merseyside.adapters.listManager.AdapterListManager
import com.merseyside.adapters.listManager.AdapterNestedListManager
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.model.VM
import com.merseyside.adapters.modelList.ModelList
import com.merseyside.adapters.modelList.ModelListCallback
import com.merseyside.adapters.modelList.SimpleModelList
import com.merseyside.merseyLib.kotlin.coroutines.CoroutineQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

open class AdapterConfig<Parent, Model> internal constructor(
    config: AdapterConfig<Parent, Model>.() -> Unit = {}
) where Model : VM<Parent> {
    protected lateinit var adapter: IBaseAdapter<Parent, Model>

    internal val featureList = ArrayList<Feature<Parent, Model>>()

    init {
        apply(config)
    }

    var coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    val workManager: CoroutineQueue<Any, Unit> by lazy {
        CoroutineQueue(coroutineScope)
    }

    fun install(feature: Feature<Parent, Model>) {
        featureList.add(feature)
    }

    fun <TConfig : Any> install(
        feature: ConfigurableFeature<Parent, Model, TConfig>,
        config: TConfig.() -> Unit
    ) {
        feature.prepare(config)
        featureList.add(feature)
    }

    open fun initAdapterWithConfig(adapter: IBaseAdapter<Parent, Model>) {
        this.adapter = adapter
        featureList.forEach { feature ->
            feature.install(adapter)
        }
    }

    open fun getModelList(listCallback: ModelListCallback<Model>): ModelList<Parent, Model> {
        val listProviders: List<ModelListProvider<Parent, Model>> =
            featureList.filterIsInstance<ModelListProvider<Parent, Model>>()

        if (listProviders.size > 1) throw IllegalArgumentException(
            "There are few list" +
                    " provider features. Have to be zero or one"
        )

        return if (listProviders.isEmpty()) {
            SimpleModelList()
        } else {
            listProviders.first().modelList
        }.also { modelList -> modelList.listCallback = listCallback }
    }

    fun initWithUpdateLogic(listChangeDelegate: AdapterListManager<Parent, Model>) {
        val updateLogic = featureList.filterIsInstance<UpdateLogicProvider<Parent, Model>>()
            .firstOrNull()?.updateLogic(listChangeDelegate) ?: SimpleUpdate(listChangeDelegate)

        listChangeDelegate.updateLogic = updateLogic
    }

    fun getDelegate(adapter: IBaseAdapter<Parent, Model>): AdapterListManager<Parent, Model> {
        return if (hasFeature(FilterFeature.key)) {
            val filterFeature = getFeatureByKey(FilterFeature.key) as FilterFeature<Parent, Model>
            FilterListManager(
                modelList = getModelList(adapter),
                listActions = adapter,
                adapterFilter = filterFeature.adapterFilter
            )
        } else {
            ListManager(
                modelList = getModelList(adapter),
                listActions = adapter
            )
        }.also { initWithUpdateLogic(it) }
    }
}

fun <Parent, Model : VM<Parent>>
        AdapterConfig<Parent, Model>.workManager(
) = object : ReadOnlyProperty<IBaseAdapter<Parent, Model>, CoroutineQueue<Any, Unit>> {

    var value: CoroutineQueue<Any, Unit>? = null

    override fun getValue(
        thisRef: IBaseAdapter<Parent, Model>,
        property: KProperty<*>
    ): CoroutineQueue<Any, Unit> {
        if (value == null) {
            value = workManager
        }

        return value!!
    }

}


fun <Parent, Model : VM<Parent>>
        AdapterConfig<Parent, Model>.delegate(
) = object :
    ReadOnlyProperty<IBaseAdapter<Parent, Model>, AdapterListManager<Parent, Model>> {

    var value: AdapterListManager<Parent, Model>? = null

    override fun getValue(
        thisRef: IBaseAdapter<Parent, Model>,
        property: KProperty<*>
    ): AdapterListManager<Parent, Model> {
        if (value == null) {
            value = getDelegate(thisRef)
        }

        return value!!
    }

}

class NestedAdapterConfig<Parent, Model, Data, InnerAdapter> internal constructor() :
    AdapterConfig<Parent, Model>()
        where Model : NestedAdapterParentViewModel<out Parent, Parent, Data>,
              InnerAdapter : BaseAdapter<Data, out VM<Data>> {

    fun getDelegate(
        adapter: INestedAdapter<Parent, Model, Data, InnerAdapter>
    ): AdapterNestedListManager<Parent, Model, Data, InnerAdapter> {
        return if (hasFeature(FilterFeature.key)) {
            val filterFeature = getFeatureByKey(FilterFeature.key) as FilterFeature<Parent, Model>
            FilterNestedListManager(
                modelList = getModelList(adapter),
                listActions = adapter,
                adapterFilter = filterFeature.adapterFilter
            )
        } else {
            NestedListManager(
                getModelList(adapter),
                adapter
            )
        }.also { initWithUpdateLogic(it) }
    }
}

fun <Parent, Model : NestedAdapterParentViewModel<out Parent, Parent, Data>, Data,
        InnerAdapter : BaseAdapter<Data, out VM<Data>>>
        NestedAdapterConfig<Parent, Model, Data, InnerAdapter>.delegate(
) = object : ReadOnlyProperty<INestedAdapter<Parent, Model, Data, InnerAdapter>,
        AdapterNestedListManager<Parent, Model, Data, InnerAdapter>> {

    var value: AdapterNestedListManager<Parent, Model, Data, InnerAdapter>? = null

    override fun getValue(
        thisRef: INestedAdapter<Parent, Model, Data, InnerAdapter>,
        property: KProperty<*>
    ): AdapterNestedListManager<Parent, Model, Data, InnerAdapter> {
        if (value == null) {
            value = getDelegate(thisRef)
        }

        return value!!
    }
}

fun <Parent, Model : VM<Parent>> config(
    scope: CoroutineScope
): AdapterConfig<Parent, Model> {
    return AdapterConfig { coroutineScope = scope }
}

fun <Parent, Model : VM<Parent>> config(
    init: AdapterConfig<Parent, Model>.() -> Unit
): AdapterConfig<Parent, Model> {
    return AdapterConfig(init)
}

fun <Parent, Model : NestedAdapterParentViewModel<out Parent, Parent, Data>, Data,
        InnerAdapter : BaseAdapter<Data, out VM<Data>>> config(
    init: NestedAdapterConfig<Parent, Model, Data, InnerAdapter>.() -> Unit
): NestedAdapterConfig<Parent, Model, Data, InnerAdapter> {
    val config = NestedAdapterConfig<Parent, Model, Data, InnerAdapter>()
    config.apply(init)

    return config
}