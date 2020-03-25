package com.merseyside.merseyLib.presentation.model

import com.merseyside.merseyLib.model.BaseAdapterViewModel

abstract class BaseComparableAdapterViewModel<M>(obj: M) : BaseAdapterViewModel<M>(obj) {

    abstract fun areContentsTheSame(obj: M): Boolean

    abstract fun compareTo(obj: M) : Int

}
