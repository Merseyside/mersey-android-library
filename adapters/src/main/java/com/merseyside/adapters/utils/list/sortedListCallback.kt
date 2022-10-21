@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.utils.list

import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.merseyLib.kotlin.utils.getMinMax
import com.merseyside.adapters.model.VM

internal fun <Parent, Model : VM<Parent>> createSortedListCallback(
    adapter: RecyclerView.Adapter<*>,
    models: () -> List<Model>,
    comparator: (model1: Model, model2: Model) -> Int
): SortedList.Callback<Model> = object : SortedList.Callback<Model>() {
    override fun onInserted(position: Int, count: Int) {
        adapter.notifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        for (i in position until models().size) {
            models()[i].onPositionChanged(i)
        }

        adapter.notifyItemRangeRemoved(position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        val minMax = getMinMax(fromPosition, toPosition)

        for (i in minMax.first..minMax.second) {
            models()[i].onPositionChanged(i)
        }

        adapter.notifyItemMoved(fromPosition, toPosition)
    }

    override fun onChanged(position: Int, count: Int) {
        adapter.notifyItemRangeChanged(position, count)
    }

    override fun compare(item1: Model, item2: Model): Int {
        return comparator(item1, item2)
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem.areContentsTheSame(newItem.item)
    }

    override fun areItemsTheSame(item1: Model, item2: Model): Boolean {
        return item1.areItemsTheSame(item2.item)
    }
}