package com.merseyside.merseyLib.features.adapters.delegate.adapter

import com.merseyside.adapters.delegates.DelegateAdapter
import com.merseyside.adapters.extensions.onItemClicked
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.delegate.entity.Animal
import com.merseyside.merseyLib.features.adapters.delegate.entity.Cat
import com.merseyside.merseyLib.features.adapters.delegate.model.CatItemViewModel
import com.merseyside.merseyLib.kotlin.logger.log

class CatDelegateAdapter: DelegateAdapter<Cat, Animal, CatItemViewModel>() {

    override fun createItemViewModel(item: Cat) = CatItemViewModel(item)
    override fun getLayoutIdForItem(viewType: Int) = R.layout.item_cat
    override fun getBindingVariable() = BR.model
}