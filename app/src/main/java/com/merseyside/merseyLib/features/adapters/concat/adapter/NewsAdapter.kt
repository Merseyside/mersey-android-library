package com.merseyside.merseyLib.features.adapters.concat.adapter

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.init.initAdapter
import com.merseyside.adapters.single.SimpleAdapter
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.concat.entity.News
import com.merseyside.merseyLib.features.adapters.concat.model.NewsItemViewModel

class NewsAdapter private constructor(
    override val adapterConfig: AdapterConfig<News, NewsItemViewModel>
) : SimpleAdapter<News, NewsItemViewModel>(adapterConfig) {
    override fun getLayoutIdForPosition(position: Int) = R.layout.item_news
    override fun getBindingVariable() = BR.obj
    override fun createItemViewModel(item: News) = NewsItemViewModel(item)

    companion object {
        operator fun invoke(): NewsAdapter {
            return initAdapter(::NewsAdapter)
        }
    }
}