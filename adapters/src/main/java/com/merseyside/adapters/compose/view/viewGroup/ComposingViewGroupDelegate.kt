package com.merseyside.adapters.compose.view.viewGroup

import com.merseyside.adapters.compose.adapter.ViewCompositeAdapter
import com.merseyside.adapters.compose.delegate.NestedViewDelegateAdapter
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.model.VM
import com.merseyside.merseyLib.kotlin.logger.log
import com.merseyside.merseyLib.kotlin.logger.logSimpleTag

abstract class ComposingViewGroupDelegate<View, Style, Model, InnerParent, InnerModel, InnerAdapter>
    : NestedViewDelegateAdapter<View, Style, Model, InnerParent, InnerModel, InnerAdapter>()
        where View : ComposingViewGroup<Style>,
              Style : ComposingViewGroupStyle,
              Model : NestedAdapterParentViewModel<View, SCV, out InnerParent>,
              InnerParent : SCV,
              InnerModel : VM<InnerParent>,
              InnerAdapter : ViewCompositeAdapter<InnerParent, out InnerModel> {

    @Suppress("UNCHECKED_CAST")
    override fun initNestedAdapter(model: Model): InnerAdapter {
        return super.initNestedAdapter(model).also { adapter ->
            model.item.viewGroupComposeContext.relativeAdapter =
                adapter as ViewCompositeAdapter<SCV, VM<SCV>>
        }
    }

}