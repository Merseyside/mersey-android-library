@file:OptIn(InternalAdaptersApi::class)
package com.merseyside.adapters.base

import com.merseyside.adapters.interfaces.simple.ISimpleAdapter
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class SimpleAdapter<Item, Model : AdapterViewModel<Item>>(
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : BaseAdapter<Item, Model>(), ISimpleAdapter<Item, Model> {

    override val models: MutableList<Model> = ArrayList()

    final override val mutModels: MutableList<Model> = ArrayList()

    override fun getItemCount() = mutModels.size
}