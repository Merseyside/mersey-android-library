package com.merseyside.adapters.utils.list

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.merseyLib.kotlin.getMinMax
import com.merseyside.utils.mainThreadIfNeeds

internal fun <Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>>
        createSortedListCallback(
    adapter: RecyclerView.Adapter<*>,
    modelListProvider: () -> List<Model>
): SortedList.Callback<Model> {

    fun comparePriority(o1: Model, o2: Model): Int {
        return o1.priority.compareTo(o2.priority)
    }

    return object : SortedList.Callback<Model>() {
        override fun onInserted(position: Int, count: Int) {
            for (i in position until modelListProvider().size) {
                modelListProvider()[i].onPositionChanged(i)
            }

            mainThreadIfNeeds { adapter.notifyItemRangeInserted(position, count) }
        }

        override fun onRemoved(position: Int, count: Int) {
            for (i in position until modelListProvider().size) {
                modelListProvider()[i].onPositionChanged(i)
            }

            mainThreadIfNeeds { adapter.notifyItemRangeRemoved(position, count) }
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            val minMax = getMinMax(fromPosition, toPosition)

            for (i in minMax.first..minMax.second) {
                modelListProvider()[i].onPositionChanged(i)
            }

            adapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int) {
            mainThreadIfNeeds { adapter.notifyItemRangeChanged(position, count) }
        }

        override fun compare(model1: Model, model2: Model): Int {
            val priority = comparePriority(model1, model2)
            return if (priority == 0) model1.compareTo(model2.item)
            else priority
        }

        override fun areContentsTheSame(model1: Model, model2: Model): Boolean {
            return model1.areContentsTheSame(model2.item)
        }

        override fun areItemsTheSame(model1: Model, model2: Model): Boolean {
            return model1.areItemsTheSame(model2.item)
        }
    }
}