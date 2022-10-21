package com.merseyside.adapters.modelList

import com.merseyside.adapters.model.AdapterParentViewModel

interface ModelListCallback<Model> {

    fun onInserted(models: List<Model>, position: Int, count: Int = 1)

    fun onRemoved(models: List<Model>, position: Int, count: Int = 1)

    fun onChanged(model: Model, position: Int, payloads: List<AdapterParentViewModel.Payloadable>)

    fun onMoved(fromPosition: Int, toPosition: Int)
}