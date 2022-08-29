package com.merseyside.adapters.feature.compositeScreen

import com.merseyside.adapters.delegates.DelegateAdapter
import com.merseyside.utils.reflection.ReflectionUtils

abstract class CompositeScreenViewBuilder<Data, OutData> {

    abstract fun map(data: Data): OutData

    abstract fun getDelegate(): DelegateAdapter<out OutData, OutData, *>

    fun <T> isResponsibleFor(clazz: Class<T>): Boolean {
        return clazz == persistentClass
    }

    @Suppress("UNCHECKED_CAST")
    private val persistentClass: Class<Data> =
        ReflectionUtils.getGenericParameterClass(
            this.javaClass,
            DelegateAdapter::class.java,
            0
        ) as Class<Data>
}