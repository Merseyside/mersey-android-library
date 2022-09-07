package com.merseyside.adapters.feature.compositeScreen

import com.merseyside.adapters.delegates.PrioritizedDelegateAdapter
import com.merseyside.adapters.delegates.composites.SortedCompositeAdapter
import com.merseyside.adapters.feature.compare.Priority
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

@OptIn(InternalAdaptersApi::class)
abstract class CompositeScreenBuilder(
    val adapter: SortedCompositeAdapter<CompositeView,
            ComparableAdapterParentViewModel<out CompositeView, CompositeView>>
//    =
//        object : SortedCompositeAdapter<CompositeView,
//                ComparableAdapterParentViewModel<out CompositeView, CompositeView>>()
) {
    val screenComposer = ScreenComposer<CompositeView>()

    val viewBuilders: MutableList<DataBuilder<CompositeView>> = ArrayList()

    abstract val compose: ScreenComposer<CompositeView>.() -> Unit

    suspend inline fun <reified Data> setData(data: Data) {
        val viewModels: List<CompositeView> = getViewModels(data)
        adapter.addOrUpdate(viewModels)
    }

    @InternalAdaptersApi
    inline fun <reified Data> getViewModels(data: Data): List<CompositeView> {
        val viewModels: MutableList<CompositeView> = ArrayList()

        if (adapter.delegatesManager.isEmpty()) {
            screenComposer.compose()
            adapter.delegatesManager.addDelegates(*screenComposer.delegates.toTypedArray())
        }





//        val viewModels: MutableList<CompositeView> = ArrayList()
//        viewBuilders.forEach { builder ->
//            if (builder.isResponsibleForData(Data::class.java)) {
//                val newItem = (builder as DataBuilder<Data, *>).map(data)
//                if (newItem is List<*>) {
//                    viewModels.addAll(newItem as List<CompositeView>)
//                } else {
//                    viewModels.add(newItem)
//                }
//            }
//        }
//        return viewModels
        TODO()
    }


    fun invalidate() {
        screenComposer.invalidate()
        adapter.delegatesManager.clear()
    }

    inner class ScreenComposer<Parent> {

        val delegates: MutableList<PrioritizedDelegateAdapter<out Parent, Parent, *>> = ArrayList()
        var lastPriority: Int = 0


        infix fun ScreenComposer<Parent>.startsWith(delegate: PrioritizedDelegateAdapter<out Parent, Parent, *>) {
            delegate.priority = Priority.ALWAYS_FIRST_PRIORITY
            delegates.add(delegate)
        }

        infix fun ScreenComposer<Parent>.then(delegate: PrioritizedDelegateAdapter<out Parent, Parent, *>) {
            delegate.priority = lastPriority++
            delegates.add(delegate)
        }

        infix fun ScreenComposer<Parent>.endsWith(delegate: PrioritizedDelegateAdapter<out Parent, Parent, *>) {
            delegate.priority = Priority.ALWAYS_LAST_PRIORITY
            delegates.add(delegate)
        }

        fun invalidate() {
            delegates.clear()
            lastPriority = 0
        }
    }
}

