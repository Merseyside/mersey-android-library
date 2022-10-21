package com.merseyside.adapters.config.feature

import com.merseyside.adapters.model.AdapterParentViewModel

abstract class ConfigurableFeature<Parent, Model, Config> : Feature<Parent, Model>()
    where Model: AdapterParentViewModel<out Parent, Parent> {

    abstract fun prepare(configure: Config.() -> Unit)

    protected abstract val config: Config

}