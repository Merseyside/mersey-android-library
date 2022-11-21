package com.merseyside.adapters.listManager.impl

import com.merseyside.adapters.config.update.UpdateLogic
import com.merseyside.adapters.interfaces.base.AdapterActions
import com.merseyside.adapters.listManager.IModelListManager
import com.merseyside.adapters.model.VM
import com.merseyside.adapters.modelList.ModelList
import com.merseyside.adapters.utils.AdapterWorkManager

open class ModelListManager<Parent, Model>(
    override val modelList: ModelList<Parent, Model>,
    override val adapterActions: AdapterActions<Parent, Model>,
    override val workManager: AdapterWorkManager
) : IModelListManager<Parent, Model>
        where Model : VM<Parent> {

    override val hashMap: MutableMap<Any, Model> = mutableMapOf()

    override lateinit var updateLogic: UpdateLogic<Parent, Model>

    override suspend fun removeModels(models: List<Model>): Boolean {
        modelList.removeAll(models)
        return true
    }
}