package com.merseyside.merseyLib.features.adapters.concat.adapter

import com.merseyside.adapters.base.SimpleAdapter
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.concat.entity.News
import com.merseyside.merseyLib.features.adapters.concat.model.NewsItemViewModel

class NewsAdapter: SimpleAdapter<News, NewsItemViewModel>() {
    override fun getLayoutIdForPosition(position: Int) = R.layout.item_news
    override fun getBindingVariable() = BR.obj
    override fun createItemViewModel(item: News) = NewsItemViewModel(item)
}