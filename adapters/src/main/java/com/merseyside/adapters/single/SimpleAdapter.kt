@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.single

import com.merseyside.adapters.feature.filter.FilterPositionListChangeDelegate
import com.merseyside.adapters.interfaces.simple.ISimpleAdapter
import com.merseyside.adapters.listDelegates.PositionListChangeDelegate
import com.merseyside.adapters.listDelegates.interfaces.AdapterPositionListChangeDelegate
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.getFilter
import com.merseyside.adapters.utils.isFilterable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class SimpleAdapter<Item, Model : AdapterViewModel<Item>>(
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : SingleAdapter<Item, Model>(), ISimpleAdapter<Item, Model> {
    final override val mutModels: MutableList<Model> = ArrayList()
    override val models: List<Model> = mutModels

    override val defaultDelegate: PositionListChangeDelegate<Item, Model> by lazy {
        PositionListChangeDelegate(this)
    }

    override val filterDelegate: FilterPositionListChangeDelegate<Item, Model> by lazy {
        FilterPositionListChangeDelegate(defaultDelegate, getFilter())
    }

    override val delegate: AdapterPositionListChangeDelegate<Item, Model> by lazy {
        if (isFilterable()) filterDelegate else defaultDelegate
    }

    override fun getItemCount() = models.size
}