package com.merseyside.adapters.feature.compositeScreen

import com.merseyside.adapters.delegates.composites.CompositeAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

@OptIn(InternalAdaptersApi::class)
abstract class CompositeScreenBuilder<Parent, Model>(
    val adapter: CompositeAdapter<Parent, Model>
) where Model : AdapterParentViewModel<out Parent, Parent> {

    val viewBuilders: MutableList<CompositeScreenViewBuilder<*, Parent>> = ArrayList()

//    fun addScreenViewBuilder(viewBuilder: CompositeScreenViewBuilder<*, Parent>) {
//        viewBuilders.add(viewBuilder)
//        adapter.delegatesManager.addDelegate(viewBuilder.getDelegate())
//    }

    abstract val compose: ScreenComposer.() -> Unit
    //abstract suspend fun compose(block: ScreenComposer.() -> Unit)

    suspend inline fun <reified Data> setData(data: Data) {
        val viewModels: List<Parent> = getViewModels(data)
        adapter.addOrUpdate(viewModels)
    }

    @InternalAdaptersApi
    inline fun <reified Data> getViewModels(data: Data): List<Parent> {
        val viewModels: MutableList<Parent> = ArrayList()
        viewBuilders.forEach { builder ->
            if (builder.isResponsibleFor(Data::class.java)) {
                val newItem = (builder as CompositeScreenViewBuilder<Data, *>).map(data)
                if (newItem is List<*>) {
                    viewModels.addAll(newItem as List<Parent>)
                } else {
                    viewModels.add(newItem as Parent)
                }
            }
        }
        return viewModels
    }

    inner class ScreenComposer {

        internal val screenBuilderOrder: MutableList<CompositeScreenViewBuilder<*, out Parent>> = ArrayList()


        infix fun ScreenComposer.startsWith(screenBuilder: CompositeScreenViewBuilder<*, out Parent>) {
            screenBuilderOrder.add(0, screenBuilder)
        }

        infix fun ScreenComposer.then(screenBuilder: CompositeScreenViewBuilder<*, Parent>) {
            screenBuilderOrder.add(0, screenBuilder)
        }
    }
}

