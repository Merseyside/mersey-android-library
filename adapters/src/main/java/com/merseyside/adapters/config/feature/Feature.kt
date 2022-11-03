package com.merseyside.adapters.config.feature

import androidx.annotation.CallSuper
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.model.VM

abstract class Feature<Parent, Model : VM<Parent>> {
    var isInstalled: Boolean = false

    @CallSuper
    open fun install(
        adapterConfig: AdapterConfig<Parent, Model>,
        adapter: BaseAdapter<Parent, Model>
    ) {
        isInstalled = true
    }

    abstract val featureKey: String
}
