package com.merseyside.adapters.feature.compositeScreen

import com.merseyside.adapters.delegates.DelegateAdapter
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.utils.reflection.ReflectionUtils

abstract class CompositeScreenViewBuilder<Data, View : CompositeView> {

    abstract fun map(data: Data): View

    abstract fun getDelegate(): DelegateAdapter<out View, View, *>

    @InternalAdaptersApi
    fun <T> isResponsibleForData(clazz: Class<T>): Boolean {
        return clazz == dataClass
    }

    @Suppress("UNCHECKED_CAST")
    private val dataClass: Class<Data> =
        ReflectionUtils.getGenericParameterClass(
            this.javaClass,
            CompositeScreenViewBuilder::class.java,
            0
        ) as Class<Data>

    @Suppress("UNCHECKED_CAST")
    private val viewClass: Class<View> =
        ReflectionUtils.getGenericParameterClass(
            this.javaClass,
            CompositeScreenViewBuilder::class.java,
            1
        ) as Class<View>
}