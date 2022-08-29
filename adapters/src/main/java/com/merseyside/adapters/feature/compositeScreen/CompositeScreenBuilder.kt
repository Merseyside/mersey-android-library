package com.merseyside.adapters.feature.compositeScreen

import com.merseyside.adapters.delegates.composites.CompositeAdapter
import com.merseyside.adapters.model.AdapterParentViewModel

abstract class CompositeScreenBuilder<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    private val adapter: CompositeAdapter<Parent, Model>
) {

    val viewBuilders: MutableList<CompositeScreenViewBuilder<*, Parent>> = ArrayList()

    fun addScreenViewBuilder(viewBuilder: CompositeScreenViewBuilder<*, Parent>) {
        viewBuilders.add(viewBuilder)
        adapter.delegatesManager.addDelegate(viewBuilder.getDelegate())
    }

    inline fun <reified Data> setData(data: Data) {
        val newItems: MutableList<Parent> = ArrayList()
        viewBuilders.forEach { builder ->
            if (builder.isResponsibleFor(Data::class.java)) {
                val newItem = (builder as CompositeScreenViewBuilder<Data, *>).map(data)
                if (newItem is List<*>) {
                    newItems.addAll(newItem as List<Parent>)
                } else {
                    newItems.add(newItem as Parent)
                }
            }
        }
    }
}