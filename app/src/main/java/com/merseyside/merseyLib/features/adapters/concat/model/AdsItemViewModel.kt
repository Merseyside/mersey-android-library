package com.merseyside.merseyLib.features.adapters.concat.model

import androidx.databinding.Bindable
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.merseyLib.features.adapters.concat.entity.Ads

class AdsItemViewModel(obj: Ads): AdapterViewModel<Ads>(obj) {

    override fun areItemsTheSame(other: Ads): Boolean {
        return this.item.id == other.id
    }

    override fun notifyUpdate() {}

    @Bindable
    fun getTitle(): String = this.item.title

    @Bindable
    fun getDescription(): String = this.item.description
}