package com.merseyside.adapters.config.feature

import androidx.annotation.CallSuper
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.model.VM

abstract class Feature<Parent, Model: VM<Parent>> {

    @CallSuper
    open fun install(adapter: IBaseAdapter<Parent, Model>) {
        modelClass = adapter.modelClass
    }

    protected lateinit var modelClass: Class<Model>

    abstract val featureKey: String
}
