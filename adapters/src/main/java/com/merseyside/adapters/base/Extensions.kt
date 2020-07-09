package com.merseyside.adapters.base

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.model.BaseAdapterViewModel
import com.merseyside.adapters.model.BaseComparableAdapterViewModel

@Throws(IllegalArgumentException::class)
fun <T : BaseComparableAdapterViewModel<M>, M : Any> SortedList<T>.isEquals(list : MutableList<T>) : Boolean {

    if (this.size() != list.size) {
        return false
    } else {

        var isEquals = true
        list.forEachIndexed { index, t ->
            val value = this.get(index)
            if (!value.areItemsTheSame(t.getItem())) {
                isEquals = false
                return@forEachIndexed
            }
        }

        return isEquals
    }
}

fun <T : BaseComparableAdapterViewModel<M>, M : Any> SortedList<T>.isNotEquals(
    list : MutableList<T>
) : Boolean = !this.isEquals(list)

fun <M> HasOnItemClickListener<M>.onItemClicked(onClick: (M) -> Unit): OnItemClickListener<M> {
    this.listener = object:
        OnItemClickListener<M> {
        override fun onItemClicked(obj: M) {
            onClick.invoke(obj)
        }
    }

    return this.listener!!
}