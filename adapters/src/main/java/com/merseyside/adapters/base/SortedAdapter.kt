@file:OptIn(InternalAdaptersApi::class)
package com.merseyside.adapters.base

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.ext.getAll
import com.merseyside.adapters.interfaces.ISortedAdapter
import com.merseyside.adapters.model.ComparableAdapterViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.list.createSortedListCallback
import com.merseyside.utils.reflection.ReflectionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Suppress("UNCHECKED_CAST")
abstract class SortedAdapter<Parent, Model : ComparableAdapterViewModel<Parent>>(
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : BaseAdapter<Parent, Model>(), ISortedAdapter<Parent, Model> {

    override val sortedList: SortedList<Model> by lazy {
        SortedList(persistentClass, createSortedListCallback(this) { models })
    }

    override val models: List<Model>
        get() = sortedList.getAll()

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
