package com.merseyside.adapters.feature.compositeScreen

import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.utils.reflection.ReflectionUtils

abstract class DataBuilder<Data> {

    @InternalAdaptersApi
    open fun <T> isResponsibleForData(clazz: Class<T>): Boolean {
        return clazz == dataClass
    }

    @Suppress("UNCHECKED_CAST")
    private val dataClass: Class<Data> =
        ReflectionUtils.getGenericParameterClass(
            this.javaClass,
            DataBuilder::class.java,
            0
        ) as Class<Data>


}

abstract class ViewDataBuilder<Data, View: CompositeView>: DataBuilder<Data>() {
    abstract fun build(data: Data): View

    @Suppress("UNCHECKED_CAST")
    private val viewClass: Class<View> =
        ReflectionUtils.getGenericParameterClass(
            this.javaClass,
            DataBuilder::class.java,
            1
        ) as Class<View>
}

abstract class ListViewDataBuilder<Data, View : CompositeView>(
    private val builder: ViewDataBuilder<Data, View>
) : DataBuilder<List<Data>>() {

    @InternalAdaptersApi
    override fun <T> isResponsibleForData(clazz: Class<T>): Boolean {
        return builder.isResponsibleForData(clazz)
    }
    fun map(data: List<Data>): List<View> {
        return data.map { builder.build(it) }
    }
}