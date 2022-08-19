package com.merseyside.adapters.delegates.composites

import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.feature.filter.FilterNestedListChangeDelegate
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.interfaces.nested.INestedAdapter
import com.merseyside.adapters.listDelegates.interfaces.AdapterNestedListChangeDelegate
import com.merseyside.adapters.listDelegates.interfaces.NestedListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.utils.getFilter
import com.merseyside.adapters.utils.isFilterable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class NestedCompositeAdapter<Parent, Model, Data, InnerAdapter>(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : SortedCompositeAdapter<Parent, Model>(scope = scope),
    INestedAdapter<Parent, Model, Data, InnerAdapter>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, Data>,
              InnerAdapter : RecyclerView.Adapter<out TypedBindingHolder<out AdapterParentViewModel<out Data, Data>>>,
              InnerAdapter : IBaseAdapter<Data, out AdapterParentViewModel<out Data, Data>> {

    override var adapterList: MutableList<Pair<Model, InnerAdapter>> = ArrayList()

    override val defaultDelegate: NestedListChangeDelegate<Parent, Model, InnerAdapter> by lazy {
        NestedListChangeDelegate(this)
    }

    override val filterDelegate: FilterNestedListChangeDelegate<Parent, Model, InnerAdapter> by lazy {
        FilterNestedListChangeDelegate(defaultDelegate, getFilter())
    }

    override val delegate: AdapterNestedListChangeDelegate<Parent, Model, InnerAdapter> by lazy {
        if (isFilterable()) filterDelegate else defaultDelegate
    }

    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, position: Int) {
        super.onBindViewHolder(holder, position)
        val model = getModelByPosition(position)

        getNestedView(holder.binding)?.apply {
            val adapter = getNestedAdapterByModel(model)
            if (this.adapter != adapter) {
                this.adapter = adapter
            }
        }
    }
}