package com.merseyside.adapters.config

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.contract.ModelListProvider
import com.merseyside.adapters.config.contract.OnBindItemListener
import com.merseyside.adapters.config.contract.UpdateLogicProvider
import com.merseyside.adapters.config.ext.getFeatureByKey
import com.merseyside.adapters.config.ext.hasFeature
import com.merseyside.adapters.config.feature.ConfigurableFeature
import com.merseyside.adapters.config.feature.Feature
import com.merseyside.adapters.config.update.simple.SimpleUpdate
import com.merseyside.adapters.feature.filtering.FilterFeature
import com.merseyside.adapters.feature.filtering.listManager.FilterModelListManager
import com.merseyside.adapters.feature.filtering.listManager.FilterNestedModelListManager
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.interfaces.nested.INestedAdapter
import com.merseyside.adapters.listManager.IModelListManager
import com.merseyside.adapters.listManager.INestedModelListManager
import com.merseyside.adapters.listManager.impl.NestedModelModelListManager
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.model.VM
import com.merseyside.adapters.modelList.ModelList
import com.merseyside.adapters.modelList.ModelListCallback
import com.merseyside.adapters.modelList.SimpleModelList
import com.merseyside.adapters.utils.AdapterWorkManager
import com.merseyside.merseyLib.kotlin.coroutines.CoroutineQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class AdapterConfig<Parent, Model> internal constructor(
    config: AdapterConfig<Parent, Model>.() -> Unit = {}
) : OnBindItemListener<Parent, Model>
        where Model : VM<Parent> {
    protected lateinit var adapter: IBaseAdapter<Parent, Model>

    internal val featureList = ArrayList<Feature<Parent, Model>>()

    private lateinit var _modelListManager: IModelListManager<Parent, Model>
    open val listManager: IModelListManager<Parent, Model>
        get() = _modelListManager

    lateinit var modelList: ModelList<Parent, Model>

    var errorHandler: ((Exception) -> Unit)? = null

    var coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    val workManager: AdapterWorkManager

    private val onBindViewListeners: MutableList<OnBindItemListener<Parent, Model>> = ArrayList()

    init {
        apply(config)
        if (errorHandler == null) errorHandler = { e -> throw e }
        workManager = AdapterWorkManager(
            CoroutineQueue<Any, Unit>(coroutineScope).apply { fallOnException = true },
            errorHandler!!
        )
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
        adapter.workManager = workManager
        adapter.onBindItemListener = this

        initModelListManager(adapter)

        featureList
            .filter { !it.isInstalled }
            .forEach { feature -> feature.install(this, adapter) }
    }

    @Suppress("UNCHECKED_CAST")
    protected fun initModelList(listCallback: ModelListCallback<Model>): ModelList<Parent, Model> {
        val listProviders: List<ModelListProvider<Parent, Model>> =
            featureList.filterIsInstance<ModelListProvider<Parent, Model>>()

        if (listProviders.size > 1) throw IllegalArgumentException(
            "There are few list provider features. Have to be zero or one"
        )

        return if (listProviders.isEmpty()) {
            SimpleModelList()
        } else {
            val listProvider = listProviders.first()
            val listProviderFeature = listProvider as Feature<Parent, Model>
            listProviderFeature.install(this, adapter)

            listProvider.modelList

        }.also { modelList ->
            modelList.addModelListCallback(listCallback)
            this.modelList = modelList
        }
    }

    fun initWithUpdateLogic(listChangeDelegate: IModelListManager<Parent, Model>) {
        val updateLogic = featureList.filterIsInstance<UpdateLogicProvider<Parent, Model>>()
            .firstOrNull()?.updateLogic(listChangeDelegate) ?: SimpleUpdate(listChangeDelegate)

        listChangeDelegate.updateLogic = updateLogic
    }

    @Suppress("UNCHECKED_CAST")
    open fun initModelListManager(adapter: IBaseAdapter<Parent, Model>): IModelListManager<Parent, Model> {
        if (!this::_modelListManager.isInitialized) {
            _modelListManager = if (hasFeature(FilterFeature.key)) {
                val filterFeature =
                    getFeatureByKey(FilterFeature.key) as FilterFeature<Parent, Model>
                FilterModelListManager(
                    modelList = initModelList(adapter),
                    adapterActions = adapter,
                    adapterFilter = filterFeature.adapterFilter
                )
            } else {
                com.merseyside.adapters.listManager.impl.ModelListManager(
                    modelList = initModelList(adapter),
                    adapterActions = adapter
                )
            }.also { initWithUpdateLogic(it) }
        }

        return _modelListManager
    }

    fun addOnBindItemListener(listener: OnBindItemListener<Parent, Model>) {
        onBindViewListeners.add(listener)
    }

    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, model: Model, position: Int) {
        onBindViewListeners.forEach { it.onBindViewHolder(holder, model, position) }
    }
}

class NestedAdapterConfig<Parent, Model, Data, InnerAdapter> internal constructor() :
    AdapterConfig<Parent, Model>()
        where Model : NestedAdapterParentViewModel<out Parent, Parent, Data>,
              InnerAdapter : BaseAdapter<Data, out VM<Data>> {

    private lateinit var _listManager: INestedModelListManager<Parent, Model, Data, InnerAdapter>

    override val listManager: INestedModelListManager<Parent, Model, Data, InnerAdapter>
        get() = _listManager

    @Suppress("UNCHECKED_CAST")
    override fun initModelListManager(
        adapter: IBaseAdapter<Parent, Model>
    ): INestedModelListManager<Parent, Model, Data, InnerAdapter> {
        adapter as INestedAdapter<Parent, Model, Data, InnerAdapter>
        if (!this::_listManager.isInitialized) {
            _listManager = if (hasFeature(FilterFeature.key)) {
                val filterFeature =
                    getFeatureByKey(FilterFeature.key) as FilterFeature<Parent, Model>
                FilterNestedModelListManager(
                    modelList = initModelList(adapter),
                    adapterActions = adapter,
                    adapterFilter = filterFeature.adapterFilter
                )
            } else {
                NestedModelModelListManager(
                    initModelList(adapter),
                    adapter
                )
            }.also { initWithUpdateLogic(it) }
        }

        return _listManager
    }
}

fun <Parent, Model : VM<Parent>> config(
    scope: CoroutineScope
): AdapterConfig<Parent, Model> {
    return AdapterConfig { coroutineScope = scope }
}

@Suppress("UNCHECKED_CAST")
fun <R : AdapterConfig<Parent, Model>, Parent, Model : VM<Parent>> config(
    init: AdapterConfig<Parent, Model>.() -> Unit
): R {
    return AdapterConfig(init) as R
}

@Suppress("UNCHECKED_CAST")
fun <R : NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>, Parent,
        Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
        InnerData, InnerAdapter : BaseAdapter<InnerData, out VM<InnerData>>> config(
    init: NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>.() -> Unit
): R {
    val config = NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>()
    config.apply(init)

    return config as R
}