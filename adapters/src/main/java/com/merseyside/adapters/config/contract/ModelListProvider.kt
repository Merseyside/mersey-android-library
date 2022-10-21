package com.merseyside.adapters.config.contract

import com.merseyside.adapters.modelList.ModelList
import com.merseyside.adapters.model.AdapterParentViewModel

interface ModelListProvider<Parent, Model: AdapterParentViewModel<out Parent, Parent>> {

    val modelList: ModelList<Parent, Model>
}