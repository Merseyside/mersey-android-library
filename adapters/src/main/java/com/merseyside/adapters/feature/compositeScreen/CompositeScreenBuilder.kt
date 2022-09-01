package com.merseyside.adapters.feature.compositeScreen

import com.merseyside.adapters.delegates.composites.CompositeAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

@OptIn(InternalAdaptersApi::class)
abstract class CompositeScreenBuilder(
    val adapter: CompositeAdapter<CompositeView, AdapterParentViewModel<out CompositeView, CompositeView>>
) {
    val viewBuilders: MutableList<CompositeScreenViewBuilder<*, CompositeView>> = ArrayList()

    abstract val compose: ScreenComposer.() -> Unit

    suspend inline fun <reified Data> setData(data: Data) {
        val viewModels: List<CompositeView> = getViewModels(data)
        adapter.addOrUpdate(viewModels)
    }

    @InternalAdaptersApi
    inline fun <reified Data> getViewModels(data: Data): List<CompositeView> {
        val viewModels: MutableList<CompositeView> = ArrayList()
        viewBuilders.forEach { builder ->
            if (builder.isResponsibleForData(Data::class.java)) {
                val newItem = (builder as CompositeScreenViewBuilder<Data, *>).map(data)
                if (newItem is List<*>) {
                    viewModels.addAll(newItem as List<CompositeView>)
                } else {
                    viewModels.add(newItem)
                }
            }
        }
        return viewModels
    }

    inner class ScreenComposer {

        internal val screenBuilderOrder: MutableList<CompositeScreenViewBuilder<*, out CompositeView>> = ArrayList()


        infix fun ScreenComposer.startsWith(screenBuilder: CompositeScreenViewBuilder<*, out CompositeView>) {
            screenBuilderOrder.add(0, screenBuilder)
        }

        infix fun ScreenComposer.then(screenBuilder: CompositeScreenViewBuilder<*, CompositeView>) {
            screenBuilderOrder.add(0, screenBuilder)
        }
    }
}

