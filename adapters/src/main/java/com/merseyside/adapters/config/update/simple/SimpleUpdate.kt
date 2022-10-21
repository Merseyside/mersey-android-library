package com.merseyside.adapters.config.update.simple

import com.merseyside.adapters.config.update.UpdateActions
import com.merseyside.adapters.config.update.UpdateLogic
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.UpdateRequest

class SimpleUpdate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    override var updateActions: UpdateActions<Parent, Model>
) : UpdateLogic<Parent, Model> {

    override suspend fun update(
        updateRequest: UpdateRequest<Parent>,
        models: List<Model>
    ): Boolean {
        var isUpdated: Boolean

        with(updateRequest) {
            val modelsToRemove = findOutdatedModels(list, models)
            isUpdated = updateActions.removeModels(modelsToRemove)

            list.forEachIndexed { newPosition, item ->
                val oldModel = models.find { it.areItemsTheSame(item) }
                if (oldModel == null) {
                    isUpdated = true
                    updateActions.add(newPosition, listOf(item))
                } else {
                    val oldPosition = getPositionOfItem(item, models)
                    updateActions.move(oldModel, oldPosition, newPosition)
                    if (updateActions.updateModel(oldModel, item)) isUpdated = true
                }
            }

            return isUpdated
        }
    }

    override suspend fun update(dest: List<Model>, source: List<Model>) {
        val modelsToRemove = findOutdatedModels(dest.map { it.item }, source)
        updateActions.removeModels(modelsToRemove)

        dest.forEachIndexed { newPosition, model ->
            val oldModel = source.find { it.areItemsTheSame(model.item) }
            if (oldModel == null) {
                updateActions.addModel(newPosition, model)
            } else {
                val oldPosition = getPositionOfModel(model, source)
                updateActions.move(oldModel, oldPosition, newPosition)
                //if (updateActions.updateModel(oldModel, item)) isUpdated = true
            }
        }
    }
}