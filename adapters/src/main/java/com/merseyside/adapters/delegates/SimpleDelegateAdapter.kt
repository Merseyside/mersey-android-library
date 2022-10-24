package com.merseyside.adapters.delegates

typealias SimpleDelegateAdapter<Item, Model> = DelegateAdapter<Item, Any, Model>
typealias SimpleNestedDelegateAdapter<Item, Model, Data, InnerAdapter> = NestedDelegateAdapter<Item, Any, Model, Data, InnerAdapter>
