package com.merseyside.merseyLib.features.adapters.news.model

import com.merseyside.adapters.model.AdapterViewModel

class NewsItemViewModel(item: News): AdapterViewModel<News>(item) {

    override fun areItemsTheSame(other: News) = item.id == other.id
}