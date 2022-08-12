package com.merseyside.adapters.delegates.composites

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.ext.getAll
import com.merseyside.adapters.feature.filter.FilterFeature
import com.merseyside.adapters.feature.filter.FilterListChangeDelegate
import com.merseyside.adapters.feature.filter.Filterable
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.ISortedAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.utils.list.AdapterListChangeDelegate
import com.merseyside.adapters.utils.list.DefaultListChangeDelegate
import com.merseyside.adapters.utils.list.createSortedListCallback
import com.merseyside.utils.reflection.ReflectionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class SortedCompositeAdapter<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>>(
    delegatesManager: DelegatesManager<Parent, Model> = DelegatesManager(),
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : BaseCompositeAdapter<Parent, Model>(delegatesManager),
    ISortedAdapter<Parent, Model> {

    override val sortedList: SortedList<Model> by lazy {
        SortedList(persistentClass, createSortedListCallback(this) { models })
    }

    override val models: List<Model>
        get() = sortedList.getAll()

    override val delegate: AdapterListChangeDelegate<Parent, Model> by lazy {
        if (this is Filterable<*, *>) FilterListChangeDelegate(
            this,
            filter as FilterFeature<Parent, Model>
        )
        else DefaultListChangeDelegate(this)
    }

    override fun onBindViewHolder(
        holder: TypedBindingHolder<Model>,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val payloadable = payloads[0] as List<AdapterParentViewModel.Payloadable>

            if (isPayloadsValid(payloadable)) {
                onPayloadable(holder, payloadable)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private val persistentClass: Class<Model> =
        ReflectionUtils.getGenericParameterClass(
            this.javaClass,
            SortedCompositeAdapter::class.java,
            1
        ) as Class<Model>
}