@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.base

import com.merseyside.adapters.feature.filter.FilterFeature
import com.merseyside.adapters.feature.filter.FilterPositionListChangeDelegate
import com.merseyside.adapters.feature.filter.Filterable
import com.merseyside.adapters.interfaces.simple.ISimpleAdapter
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.list.AdapterPositionListChangeDelegate
import com.merseyside.adapters.utils.list.PositionListChangeDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class SimpleAdapter<Item, Model : AdapterViewModel<Item>>(
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : BaseAdapter<Item, Model>(), ISimpleAdapter<Item, Model> {
    final override val mutModels: MutableList<Model> = ArrayList()
    override val models: List<Model> = mutModels

    override val delegate: AdapterPositionListChangeDelegate<Item, Model> by lazy {
        if (this is Filterable<*, *>) FilterPositionListChangeDelegate(
            this,
            filter as FilterFeature<Item, Model>
        )
        else PositionListChangeDelegate(this)
    }

    override fun getItemCount() = models.size
}