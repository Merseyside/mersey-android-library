//@file:OptIn(InternalAdaptersApi::class)
//
//package com.merseyside.adapters.single
//
//import com.merseyside.adapters.base.config.PositionAdapterConfig
//import com.merseyside.adapters.feature.filter.delegate.FilterPositionListChangeDelegate
//import com.merseyside.adapters.interfaces.simple.IPositionAdapter
//import com.merseyside.adapters.listDelegates.PositionListChangeDelegate
//import com.merseyside.adapters.listDelegates.interfaces.AdapterPositionListChangeDelegate
//import com.merseyside.adapters.model.AdapterViewModel
//import com.merseyside.adapters.utils.InternalAdaptersApi
//import com.merseyside.adapters.utils.getFilter
//import com.merseyside.adapters.utils.isFilterable
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//
//abstract class PositionAdapter<Item, Model : AdapterViewModel<Item>>(
//    adapterConfig: PositionAdapterConfig<Item, Model>,
//    scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
//) : SimpleAdapter<Item, Model>(adapterConfig, scope) {
////    final override val mutModels: MutableList<Model> = ArrayList()
////    override val modelList: List<Model> = mutModels
//
//    override val defaultDelegate: PositionListChangeDelegate<Item, Model> by lazy {
//        PositionListChangeDelegate(this)
//    }
//
//    override val filterDelegate: FilterPositionListChangeDelegate<Item, Model> by lazy {
//        FilterPositionListChangeDelegate(defaultDelegate, getFilter())
//    }
//
//    override val delegate: AdapterPositionListChangeDelegate<Item, Model> by lazy {
//        if (isFilterable()) filterDelegate else defaultDelegate
//    }
//
//    override fun getItemCount() = modelList.size
//}