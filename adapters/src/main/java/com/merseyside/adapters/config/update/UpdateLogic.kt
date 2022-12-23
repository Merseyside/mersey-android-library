package com.merseyside.adapters.config.update

import com.merseyside.adapters.model.VM
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.merseyLib.kotlin.extensions.subtractBy

interface UpdateLogic<Parent, Model : VM<Parent>> {

    var updateActions: UpdateActions<Parent, Model>

    suspend fun update(updateRequest: UpdateRequest<Parent>, models: List<Model>): Boolean

    suspend fun update(dest: List<Model>, source: List<Model>)

    suspend fun findOutdatedModels(
        newItems: List<Parent>,
        models: List<Model>,
    ): List<Model>  {
        return models.subtractBy(newItems) { oldModel, newItem ->
            oldModel.isDeletable && oldModel.areItemsTheSame(newItem)
        }.toList()
    }

    fun getModelByItem(item: Parent, models: List<Model>): Model? {
        return models.find { model ->
            model.areItemsTheSame(item)
        }
    }

    fun getPositionOfItem(item: Parent, models: List<Model>): Int {
        return models.indexOfFirst { model -> model.areItemsTheSame(item) }
    }

    fun getPositionOfModel(model: Model, models: List<Model>): Int {
        return models.indexOf(model)
    }
}