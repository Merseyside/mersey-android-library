package com.merseyside.adapters.config

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.ext.getFeatureByKey
import com.merseyside.adapters.config.ext.hasFeature
import com.merseyside.adapters.config.contract.ModelListProvider
import com.merseyside.adapters.config.contract.OnBindItemListener
import com.merseyside.adapters.config.contract.UpdateLogicProvider
import com.merseyside.adapters.config.feature.ConfigurableFeature
import com.merseyside.adapters.config.feature.Feature
import com.merseyside.adapters.feature.filtering.FilterFeature
import com.merseyside.adapters.config.update.simple.SimpleUpdate
import com.merseyside.adapters.feature.filtering.listManager.FilterListManager
import com.merseyside.adapters.feature.filtering.listManager.FilterINestedModelListManager
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.interfaces.nested.INestedAdapter
import com.merseyside.adapters.listManager.impl.ListManager
import com.merseyside.adapters.listManager.ModelListManager
import com.merseyside.adapters.listManager.INestedModelListManager
import com.merseyside.adapters.listManager.impl.NestedModelListManager
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.model.VM
import com.merseyside.adapters.modelList.ModelList
import com.merseyside.adapters.modelList.ModelListCallback
import com.merseyside.adapters.modelList.SimpleModelList
import com.merseyside.adapters.utils.AdapterWorkManager
import com.merseyside.merseyLib.kotlin.coroutines.CoroutineQueue
import com.merseyside.merseyLib.kotlin.logger.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.properties.ReadOnlyProperty

open class AdapterConfig<Parent, Model> internal constructor(
    config: AdapterConfig<Parent, Model>.() -> Unit = {}
): OnBindItemListener<Parent, Model>
        where Model : VM<Parent> {
    protected lateinit var adapter: BaseAdapter<Parent, Model>

    internal val featureList = ArrayList<Feature<Parent, Model>>()

    private lateinit var _modelListManager: ModelListManager<Parent, Model>
    open val modelListManager: ModelListManager<Parent, Model>
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

    open fun initAdapterWithConfig(adapter: BaseAdapter<Parent, Model>) {
        this.adapter = adapter
        adapter.workManager = workManager
        adapter.onBindItemListener = this

        initModelList(adapter)
        initModelListManager(adapter)

        featureList
            .filter { !it.isInstalled }
            .forEach { feature ->
                feature.install(this, adapter)
            }
    }

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

    fun initWithUpdateLogic(listChangeDelegate: ModelListManager<Parent, Model>) {
        val updateLogic = featureList.filterIsInstance<UpdateLogicProvider<Parent, Model>>()
            .firstOrNull()?.updateLogic(listChangeDelegate) ?: SimpleUpdate(listChangeDelegate)

        listChangeDelegate.updateLogic = updateLogic
    }

    fun initModelListManager(adapter: IBaseAdapter<Parent, Model>): ModelListManager<Parent, Model> {
        if (!this::_modelListManager.isInitialized) {
            _modelListManager = if (hasFeature(FilterFeature.key)) {
                val filterFeature =
                    getFeatureByKey(FilterFeature.key) as FilterFeature<Parent, Model>
                FilterListManager(
                    modelList = initModelList(adapter),
                    adapterActions = adapter,
                    adapterFilter = filterFeature.adapterFilter
                )
            } else {
                ListManager(
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

fun <Parent, Model : VM<Parent>> AdapterConfig<Parent, Model>.listManager() =
    ReadOnlyProperty<IBaseAdapter<Parent, Model>, ModelListManager<Parent, Model>> { thisRef, _ ->
        initModelListManager(thisRef)
    }

class NestedAdapterConfig<Parent, Model, Data, InnerAdapter> internal constructor() :
    AdapterConfig<Parent, Model>()
        where Model : NestedAdapterParentViewModel<out Parent, Parent, Data>,
              InnerAdapter : BaseAdapter<Data, out VM<Data>> {

    private lateinit var _modelList: INestedModelListManager<Parent, Model, Data, InnerAdapter>

    override val modelListManager: ModelListManager<Parent, Model>
        get() = _modelList

    fun initModelListManager(
        adapter: INestedAdapter<Parent, Model, Data, InnerAdapter>
    ): INestedModelListManager<Parent, Model, Data, InnerAdapter> {
        if (!this::_modelList.isInitialized) {
            _modelList = if (hasFeature(FilterFeature.key)) {
                val filterFeature =
                    getFeatureByKey(FilterFeature.key) as FilterFeature<Parent, Model>
                FilterINestedModelListManager(
                    modelList = initModelList(adapter),
                    adapterActions = adapter,
                    adapterFilter = filterFeature.adapterFilter
                )
            } else {
                NestedModelListManager(
                    initModelList(adapter),
                    adapter
                )
            }.also { initWithUpdateLogic(it) }
        }

        return _modelList
    }
}

fun <Parent, Model : NestedAdapterParentViewModel<out Parent, Parent, Data>, Data,
        InnerAdapter : BaseAdapter<Data, out VM<Data>>>
        NestedAdapterConfig<Parent, Model, Data, InnerAdapter>.listManager(
) = ReadOnlyProperty<INestedAdapter<Parent, Model, Data, InnerAdapter>,
        INestedModelListManager<Parent, Model, Data, InnerAdapter>> { thisRef, _ ->
    initModelListManager(thisRef)
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