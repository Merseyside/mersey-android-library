package com.merseyside.merseyLib.features.adapters.news.adapter

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.init.initAdapter
import com.merseyside.adapters.single.SimpleAdapter
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.news.model.News
import com.merseyside.merseyLib.features.adapters.news.model.NewsItemViewModel

class NewsAdapter private constructor(config: AdapterConfig<News, NewsItemViewModel>): SimpleAdapter<News, NewsItemViewModel>(config) {

    override fun getLayoutIdForPosition(position: Int) = R.layout.item_news1

    override fun getBindingVariable() = BR.model

    override fun createItemViewModel(item: News) = NewsItemViewModel(item)

    companion object {
        operator fun invoke(): NewsAdapter {
            return initAdapter(::NewsAdapter)
        }
    }

}