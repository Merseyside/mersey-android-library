package com.merseyside.adapters.delegates.composites

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.extensions.getAll
import com.merseyside.adapters.extensions.recalculatePositions
import com.merseyside.adapters.feature.compare.Comparator
import com.merseyside.adapters.feature.filter.FilterPrioritizedListChangeDelegate
import com.merseyside.adapters.feature.filter.interfaces.FilterFeature
import com.merseyside.adapters.feature.filter.interfaces.Filterable
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.sorted.ISortedAdapter
import com.merseyside.adapters.listDelegates.PrioritizedListChangeDelegate
import com.merseyside.adapters.listDelegates.interfaces.AdapterPrioritizedListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
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

    var comparator: Comparator<Parent, Model>? = null
        set(value) {
            value?.setOnComparatorUpdateCallback(object: Comparator.OnComparatorUpdateCallback {
                override fun onUpdate() {
                    sortedList.recalculatePositions()
                }
            })

            field = value
        }

    private val defaultComparator: (model1: Model, model2: Model) -> Int = { model1, model2 ->
        val priority = comparePriority(model1, model2)
        if (priority == 0) model1.compareTo(model2.item)
        else priority
    }

    private val internalComparator: (model1: Model, model2: Model) -> Int = { model1, model2 ->
        comparator?.compare(model1, model2) ?: defaultComparator(model1, model2)
    }

    override val sortedList: SortedList<Model> by lazy {
        SortedList(
            persistentClass,
            createSortedListCallback(
                adapter = this,
                models = { models },
                comparator = internalComparator
            ))
    }

    override val models: List<Model>
        get() = sortedList.getAll()

    override val delegate: AdapterPrioritizedListChangeDelegate<Parent, Model> by lazy {
        if (this is Filterable<*, *>) FilterPrioritizedListChangeDelegate(
            this,
            filter as FilterFeature<Parent, Model>
        )
        else PrioritizedListChangeDelegate(this)
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

    private fun comparePriority(o1: Model, o2: Model): Int {
        return o1.priority.compareTo(o2.priority)
    }

    private val persistentClass: Class<Model> =
        ReflectionUtils.getGenericParameterClass(
            this.javaClass,
            SortedCompositeAdapter::class.java,
            1
        ) as Class<Model>
}