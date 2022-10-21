package com.merseyside.adapters.config.feature

import androidx.annotation.CallSuper
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.model.AdapterParentViewModel

abstract class Feature<Parent, Model: AdapterParentViewModel<out Parent, Parent>> {

    @CallSuper
    open fun install(adapter: IBaseAdapter<Parent, Model>) {
        modelClass = adapter.modelClass
    }

    protected lateinit var modelClass: Class<Model>

    abstract val featureKey: String
}
