package com.merseyside.adapters.config.init

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.NestedAdapterConfig
import com.merseyside.adapters.config.config
import com.merseyside.adapters.interfaces.nested.INestedAdapter
import com.merseyside.adapters.model.NestedAdapterParentViewModel

inline fun <reified Adapter : INestedAdapter<Parent, Model, InnerData, InnerAdapter>, Parent,
        Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
        InnerData, InnerAdapter : BaseAdapter<InnerData, *>,
        Config : NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>>
        initNestedAdapter(
    crossinline constructor: (Config) -> Adapter,
    noinline configure: NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>.() -> Unit = {}
): Adapter {
    val adapterConfig: Config = config(configure)
    val adapter = constructor(adapterConfig)
    adapterConfig.initAdapterWithConfig(adapter)
    return adapter
}

inline fun <reified Adapter : INestedAdapter<Parent, Model, InnerData, InnerAdapter>, Parent,
        Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
        InnerData, InnerAdapter : BaseAdapter<InnerData, *>,
        Config : NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>,
        reified T1>
        initNestedAdapter(
    crossinline constructor: (Config, T1) -> Adapter,
    argT1: T1,
    noinline configure: NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>.() -> Unit = {}
): Adapter {
    val adapterConfig: Config = config(configure)
    val adapter = constructor(adapterConfig, argT1)
    adapterConfig.initAdapterWithConfig(adapter)
    return adapter
}

inline fun <reified Adapter : INestedAdapter<Parent, Model, InnerData, InnerAdapter>, Parent,
        Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
        InnerData, InnerAdapter : BaseAdapter<InnerData, *>,
        Config : NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>,
        reified T1, reified T2>
        initNestedAdapter(
    crossinline constructor: (Config, T1, T2) -> Adapter,
    argT1: T1, argT2: T2,
    noinline configure: NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>.() -> Unit = {}
): Adapter {
    val adapterConfig: Config = config(configure)
    val adapter = constructor(adapterConfig, argT1, argT2)
    adapterConfig.initAdapterWithConfig(adapter)
    return adapter
}

inline fun <reified Adapter : INestedAdapter<Parent, Model, InnerData, InnerAdapter>, Parent,
        Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
        InnerData, InnerAdapter : BaseAdapter<InnerData, *>,
        Config : NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>,
        reified T1, reified T2, reified T3>
        initNestedAdapter(
    crossinline constructor: (Config, T1, T2, T3) -> Adapter,
    argT1: T1, argT2: T2, argT3: T3,
    noinline configure: NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>.() -> Unit = {}
): Adapter {
    val adapterConfig: Config = config(configure)
    val adapter = constructor(adapterConfig, argT1, argT2, argT3)
    adapterConfig.initAdapterWithConfig(adapter)
    return adapter
}

inline fun <reified Adapter : INestedAdapter<Parent, Model, InnerData, InnerAdapter>, Parent,
        Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
        InnerData, InnerAdapter : BaseAdapter<InnerData, *>,
        Config : NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>,
        reified T1, reified T2, reified T3, reified T4>
        initNestedAdapter(
    crossinline constructor: (Config, T1, T2, T3, T4) -> Adapter,
    argT1: T1, argT2: T2, argT3: T3, argT4: T4,
    noinline configure: NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>.() -> Unit = {}
): Adapter {
    val adapterConfig: Config = config(configure)
    val adapter = constructor(adapterConfig, argT1, argT2, argT3, argT4)
    adapterConfig.initAdapterWithConfig(adapter)
    return adapter
}

inline fun <reified Adapter : INestedAdapter<Parent, Model, InnerData, InnerAdapter>, Parent,
        Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
        InnerData, InnerAdapter : BaseAdapter<InnerData, *>,
        Config : NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>,
        reified T1, reified T2, reified T3, reified T4, reified T5>
        initNestedAdapter(
    crossinline constructor: (Config, T1, T2, T3, T4, T5) -> Adapter,
    argT1: T1, argT2: T2, argT3: T3, argT4: T4, argT5: T5,
    noinline configure: NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>.() -> Unit = {}
): Adapter {
    val adapterConfig: Config = config(configure)
    val adapter = constructor(adapterConfig, argT1, argT2, argT3, argT4, argT5)
    adapterConfig.initAdapterWithConfig(adapter)
    return adapter
}

inline fun <reified Adapter : INestedAdapter<Parent, Model, InnerData, InnerAdapter>, Parent,
        Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
        InnerData, InnerAdapter : BaseAdapter<InnerData, *>,
        Config : NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>,
        reified T1, reified T2, reified T3, reified T4, reified T5, reified T6>
        initNestedAdapter(
    crossinline constructor: (Config, T1, T2, T3, T4, T5, T6) -> Adapter,
    argT1: T1, argT2: T2, argT3: T3, argT4: T4, argT5: T5, argT6: T6,
    noinline configure: NestedAdapterConfig<Parent, Model, InnerData, InnerAdapter>.() -> Unit = {}
): Adapter {
    val adapterConfig: Config = config(configure)
    val adapter = constructor(adapterConfig, argT1, argT2, argT3, argT4, argT5, argT6)
    adapterConfig.initAdapterWithConfig(adapter)
    return adapter
}