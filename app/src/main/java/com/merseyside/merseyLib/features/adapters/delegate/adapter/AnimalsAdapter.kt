package com.merseyside.merseyLib.features.adapters.delegate.adapter

import com.merseyside.adapters.delegates.composites.SimpleCompositeAdapter

class AnimalsAdapter: SimpleCompositeAdapter() {

    init {
        delegatesManager.addDelegates(
            CatDelegateAdapter(),
            DogDelegateAdapter(),
            ButtonDelegateAdapter()
        )
    }

}