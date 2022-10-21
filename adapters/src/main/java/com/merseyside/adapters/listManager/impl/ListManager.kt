package com.merseyside.adapters.listManager.impl

import com.merseyside.adapters.config.update.UpdateLogic
import com.merseyside.adapters.interfaces.base.AdapterListActions
import com.merseyside.adapters.listManager.AdapterListManager
import com.merseyside.adapters.modelList.ModelList
import com.merseyside.adapters.model.VM

open class ListManager<Parent, Model>(
    override val modelList: ModelList<Parent, Model>,
    override val listActions: AdapterListActions<Parent, Model>
) : AdapterListManager<Parent, Model>
        where Model : VM<Parent> {

    override val hashMap: MutableMap<Any, Model> = mutableMapOf()

    override lateinit var updateLogic: UpdateLogic<Parent, Model>

    override suspend fun removeModels(models: List<Model>): Boolean {
        modelList.removeAll(models)
        return true
    }

    override suspend fun updateModel(model: Model, item: Parent): Boolean {
        val payloads = model.payload(item)
        modelList.onModelUpdated(model, payloads)
        return true
    }
}