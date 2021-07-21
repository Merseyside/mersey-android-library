package com.merseyside.merseyLib.features.adapters.concat.model

import androidx.databinding.Bindable
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.merseyLib.features.adapters.concat.entity.Ads

class AdsItemViewModel(obj: Ads): AdapterViewModel<Ads>(obj) {

    override fun areItemsTheSame(obj: Ads): Boolean {
        return this.obj.id == obj.id
    }

    override fun notifyUpdate() {}

    @Bindable
    fun getTitle(): String = this.obj.title

    @Bindable
    fun getDescription(): String = this.obj.description
}