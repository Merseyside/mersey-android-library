package com.merseyside.adapters.delegates

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.delegates.model.SimpleAdapterViewModel

typealias SimpleDelegateAdapter<Item, Model> = DelegateAdapter<Item, Any, Model>
typealias SimpleNestedDelegateAdapter<Item, Model, Data, InnerAdapter> = NestedDelegateAdapter<Item, Any, Model, Data, InnerAdapter>

typealias SimpleAdapterConfig = AdapterConfig<Any, SimpleAdapterViewModel<out Any>>
