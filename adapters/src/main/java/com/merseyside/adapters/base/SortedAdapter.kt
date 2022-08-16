@file:OptIn(InternalAdaptersApi::class)
package com.merseyside.adapters.base

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.extensions.getAll
import com.merseyside.adapters.extensions.recalculatePositions
import com.merseyside.adapters.feature.compare.Comparator
import com.merseyside.adapters.feature.filter.FilterPrioritizedListChangeDelegate
import com.merseyside.adapters.feature.filter.interfaces.FilterFeature
import com.merseyside.adapters.feature.filter.interfaces.Filterable
import com.merseyside.adapters.interfaces.sorted.ISortedAdapter
import com.merseyside.adapters.listDelegates.PrioritizedListChangeDelegate
import com.merseyside.adapters.listDelegates.interfaces.AdapterPrioritizedListChangeDelegate
import com.merseyside.adapters.model.ComparableAdapterViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.list.createSortedListCallback
import com.merseyside.merseyLib.kotlin.extensions.isZero
import com.merseyside.utils.reflection.ReflectionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Suppress("UNCHECKED_CAST")
abstract class SortedAdapter<Item, Model : ComparableAdapterViewModel<Item>>(
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : BaseAdapter<Item, Model>(), ISortedAdapter<Item, Model> {

    var comparator: Comparator<Item, Model>? = null
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
        if (priority.isZero()) model1.compareTo(model2.item)
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

    override val delegate: AdapterPrioritizedListChangeDelegate<Item, Model> by lazy {
        if (this is Filterable<*, *>) FilterPrioritizedListChangeDelegate(this, filter as FilterFeature<Item, Model>)
        else PrioritizedListChangeDelegate(this)
    }

    override fun getItemCount() = sortedList.size()

    private fun comparePriority(o1: Model, o2: Model): Int {
        return o1.priority.compareTo(o2.priority)
    }

    /**
     * Any children of this class have to pass Item and Model types. Otherwise, this cast throws CastException
     */
    private val persistentClass: Class<Model> =
        ReflectionUtils.getGenericParameterClass(
            this.javaClass,
            SortedAdapter::class.java,
            1
        ) as Class<Model>
}
