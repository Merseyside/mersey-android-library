package com.merseyside.merseyLib.features.adapters.delegate.adapter

import com.merseyside.adapters.delegates.DelegateAdapter
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.delegate.entity.Animal
import com.merseyside.merseyLib.features.adapters.delegate.entity.Dog
import com.merseyside.merseyLib.features.adapters.delegate.model.DogItemViewModel

class DogDelegateAdapter: DelegateAdapter<Dog, Animal, DogItemViewModel>() {
    override fun createItemViewModel(item: Dog) = DogItemViewModel(item)
    override fun getLayoutIdForItem(viewType: Int) = R.layout.item_dog
    override fun getBindingVariable() = BR.model
}