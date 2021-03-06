package com.merseyside.adapters.model

abstract class BaseComparableAdapterViewModel<M>(obj: M) : BaseAdapterViewModel<M>(obj) {

    abstract fun areContentsTheSame(obj: M): Boolean

    abstract fun compareTo(obj: M) : Int

    open fun payload(newItem: M): List<Payloadable> {
        this.obj = newItem
        notifyUpdate()
        return listOf(Payloadable.None)
    }

    interface Payloadable {
        object None: Payloadable
    }
}
