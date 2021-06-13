package com.merseyside.merseyLib.features.adapters.concat.model

import androidx.databinding.Bindable
import com.merseyside.adapters.model.BaseAdapterViewModel
import com.merseyside.merseyLib.features.adapters.concat.entity.Ads
import com.merseyside.merseyLib.features.adapters.concat.entity.News

class AdsItemViewModel(obj: Ads): BaseAdapterViewModel<Ads>(obj) {

    override fun areItemsTheSame(obj: Ads): Boolean {
        return this.obj.id == obj.id
    }

    override fun notifyUpdate() {}

    @Bindable
    fun getTitle(): String = this.obj.title

    @Bindable
    fun getDescription(): String = this.obj.description
}