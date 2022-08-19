package com.merseyside.merseyLib.features.adapters.news.adapter

import com.merseyside.adapters.single.SimpleAdapter
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.news.model.News
import com.merseyside.merseyLib.features.adapters.news.model.NewsItemViewModel

class NewsAdapter: SimpleAdapter<News, NewsItemViewModel>() {

    override fun getLayoutIdForPosition(position: Int) = R.layout.item_news1

    override fun getBindingVariable() = BR.model

    override fun createItemViewModel(item: News) = NewsItemViewModel(item)
}