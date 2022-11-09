package com.merseyside.adapters.delegates.composites

import com.merseyside.adapters.delegates.SimpleDelegatesManager
import com.merseyside.adapters.extensions.recalculatePositions
import com.merseyside.adapters.extensions.recalculatePositionsWithAnimation
import com.merseyside.adapters.feature.compare.Comparator
import com.merseyside.adapters.feature.filter.delegate.FilterPrioritizedListChangeDelegate
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.sorted.ISortedAdapter
import com.merseyside.adapters.listDelegates.PrioritizedListChangeDelegate
import com.merseyside.adapters.listDelegates.interfaces.AdapterPrioritizedListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.getFilter
import com.merseyside.adapters.utils.isFilterable
import com.merseyside.adapters.utils.list.SortedList
import com.merseyside.adapters.utils.list.createSortedListCallback
import com.merseyside.utils.reflection.ReflectionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class SortedCompositeAdapter<Parent, Model>(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    delegatesManager: SimpleDelegatesManager<Parent, Model> = SimpleDelegatesManager()
) : BaseCompositeAdapter<Parent, Model>(scope, delegatesManager),
    ISortedAdapter<Parent, Model>
        where Model : ComparableAdapterParentViewModel<out Parent, Parent> {

    var comparator: Comparator<Parent, Model>? = null
        set(value) {
            value?.setOnComparatorUpdateCallback(object : Comparator.OnComparatorUpdateCallback {
                override suspend fun onUpdate(animation: Boolean) {
                    doAsync {
                        with(sortedList) {
                            if (animation) recalculatePositionsWithAnimation()
                            else recalculatePositions()
                        }
                    }
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
            )
        )
    }

    override val models: List<Model>
        get() = sortedList.getAll()

    override val defaultDelegate: PrioritizedListChangeDelegate<Parent, Model> by lazy {
        PrioritizedListChangeDelegate(this)
    }

    override val filterDelegate: FilterPrioritizedListChangeDelegate<Parent, Model> by lazy {
        FilterPrioritizedListChangeDelegate(defaultDelegate, getFilter())
    }

    @InternalAdaptersApi
    override val delegate: AdapterPrioritizedListChangeDelegate<Parent, Model> by lazy {
        if (isFilterable()) filterDelegate else defaultDelegate
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