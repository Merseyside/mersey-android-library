package com.merseyside.adapters.delegates.composites

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.ext.getAll
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.ISortedAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.merseyLib.kotlin.getMinMax
import com.merseyside.utils.mainThreadIfNeeds
import com.merseyside.utils.reflection.ReflectionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class SortedCompositeAdapter<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>>(
    delegatesManager: DelegatesManager<Parent, Model> = DelegatesManager(),
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : BaseCompositeAdapter<Parent, Model>(delegatesManager),
    ISortedAdapter<Parent, Model> {

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

    override val sortedList: SortedList<Model> by lazy { SortedList(persistentClass, listCallback) }
    override val models: List<Model>
        get() = sortedList.getAll()

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