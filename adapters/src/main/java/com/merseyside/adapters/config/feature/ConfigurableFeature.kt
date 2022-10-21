package com.merseyside.adapters.config.feature

import com.merseyside.adapters.model.VM

abstract class ConfigurableFeature<Parent, Model, Config> : Feature<Parent, Model>()
    where Model: VM<Parent> {

    abstract fun prepare(configure: Config.() -> Unit)

    protected abstract val config: Config

}