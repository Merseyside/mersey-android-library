@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.base

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.model.ComparableAdapterViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.SortedAdapterListUtils
import com.merseyside.merseyLib.kotlin.getMinMax
import com.merseyside.utils.mainThreadIfNeeds
import com.merseyside.utils.reflection.ReflectionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Suppress("UNCHECKED_CAST")
abstract class SortedAdapter<Parent, Model : ComparableAdapterViewModel<Parent>>(
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : BaseAdapter<Parent, Model>(), SortedAdapterListUtils<Parent, Model> {

    override val modelList: MutableList<Model> = ArrayList()

    private val listCallback = object : SortedList.Callback<Model>() {
        override fun onInserted(position: Int, count: Int) {
            for (i in position until sortedList.size()) {
                sortedList[i].onPositionChanged(i)
            }

            mainThreadIfNeeds { notifyItemRangeInserted(position, count) }
        }

        override fun onRemoved(position: Int, count: Int) {
            for (i in position until sortedList.size()) {
                sortedList[i].onPositionChanged(i)
            }

            mainThreadIfNeeds { notifyItemRangeRemoved(position, count) }
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            val minMax = getMinMax(fromPosition, toPosition)

            for (i in minMax.first..minMax.second) {
                sortedList[i].onPositionChanged(i)
            }

            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int) {
            mainThreadIfNeeds { notifyItemRangeChanged(position, count) }
        }

        override fun compare(model1: Model, model2: Model): Int {
            return model1.compareTo(model2.item)
        }

        override fun areContentsTheSame(model1: Model, model2: Model): Boolean {
            return model1.areContentsTheSame(model2.item)
        }

        override fun areItemsTheSame(model1: Model, model2: Model): Boolean {
            return model1.areItemsTheSame(model2.item)
        }
    }

    override val sortedList: SortedList<Model> by lazy { SortedList(persistentClass, listCallback) }

    override fun getItemCount() = sortedList.size()

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
