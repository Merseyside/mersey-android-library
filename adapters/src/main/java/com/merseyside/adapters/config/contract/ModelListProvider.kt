package com.merseyside.adapters.config.contract

import com.merseyside.adapters.modelList.ModelList
import com.merseyside.adapters.model.VM

interface ModelListProvider<Parent, Model: VM<Parent>> {

    val modelList: ModelList<Parent, Model>
}