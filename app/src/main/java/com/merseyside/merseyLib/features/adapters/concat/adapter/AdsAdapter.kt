package com.merseyside.merseyLib.features.adapters.concat.adapter

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.features.adapters.concat.entity.Ads
import com.merseyside.merseyLib.features.adapters.concat.entity.News
import com.merseyside.merseyLib.features.adapters.concat.model.AdsItemViewModel
import com.merseyside.merseyLib.features.adapters.concat.model.NewsItemViewModel

class AdsAdapter: BaseAdapter<Ads, AdsItemViewModel>() {
    override fun getLayoutIdForPosition(position: Int) = R.layout.item_ads
    override fun getBindingVariable() = BR.obj
    override fun createItemViewModel(obj: Ads) = AdsItemViewModel(obj)
}