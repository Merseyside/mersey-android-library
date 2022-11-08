package com.merseyside.adapters.compose.feature.sorting

import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.base.model.ViewVM
import com.merseyside.adapters.feature.sorting.Comparator
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.utils.reflection.ReflectionUtils

abstract class ViewComparator<Model : ViewVM<out SCV>> : Comparator<SCV, AdapterParentViewModel<out SCV, SCV>>() {

    @Suppress("UNCHECKED_CAST")
    override fun compare(
        model1: AdapterParentViewModel<out SCV, SCV>,
        model2: AdapterParentViewModel<out SCV, SCV>
    ): Int {
        return compareViews(model1 as Model, model2 as Model)
    }

    abstract fun compareViews(model1: Model, model2: Model): Int

    override fun getModelClass(): Class<Model> {
        return return ReflectionUtils.getGenericParameterClass(
            this.javaClass,
            ViewComparator::class.java,
            0
        ) as Class<Model>
    }

//    override fun getModelClass(): Class<Model> {
//        return ReflectionUtils.getGenericParameterClass(
//            this.javaClass,
//            ViewComparator::class.java,
//            0
//        ) as Class<Model>
//    }
}