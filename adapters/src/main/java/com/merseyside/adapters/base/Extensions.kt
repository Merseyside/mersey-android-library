package com.merseyside.adapters.base

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.model.BaseComparableAdapterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.NoSuchElementException
import kotlin.jvm.Throws

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

inline fun <T> SortedList<T>.forEach(onValue: (T) -> Unit) {
    forEachIndexed { _, item -> onValue(item) }
}

inline fun <T> SortedList<T>.forEachIndexed(onValue: (Int, T) -> Unit) {
    for (i in 0 until size()) {
        onValue(i, get(i))
    }
}

inline fun <T> SortedList<T>.find(predecate: (T) -> Boolean): T? {
    forEach { if (predecate(it)) return it }

    return null
}

@Throws(NoSuchElementException::class)
inline fun <T> SortedList<T>.indexOf(predecate: (T) -> Boolean): Int {
    forEachIndexed { index, obj -> if (predecate(obj)) return index }

    throw NoSuchElementException()
}

fun <T> SortedList<T>.removeAll(list: List<T>) {
    list.forEach { remove(it) }
}

fun <M> HasOnItemClickListener<M>.onItemClicked(onClick: (M) -> Unit): OnItemClickListener<M> {
    this.listener = object:
        OnItemClickListener<M> {
        override fun onItemClicked(obj: M) {
            onClick.invoke(obj)
        }
    }

    return this.listener!!
}

internal fun CoroutineScope.asynchronously(block: suspend CoroutineScope.() -> Unit) =
    launch(context = Dispatchers.Default, block = block)