package com.merseyside.merseyLib.features.adapters.concat.model

import androidx.databinding.Bindable
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.merseyLib.features.adapters.concat.entity.News

class NewsItemViewModel(obj: News): AdapterViewModel<News>(obj) {

    override fun areItemsTheSame(obj: News): Boolean {
        return this.obj.id == obj.id
    }

    override fun notifyUpdate() {}

    @Bindable
    fun getTitle(): String = this.obj.title

    @Bindable
    fun getDescription(): String = this.obj.description
}