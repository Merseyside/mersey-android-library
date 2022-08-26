package com.merseyside.adapters.listDelegates

import com.merseyside.adapters.interfaces.simple.AdapterPositionListActions
import com.merseyside.adapters.listDelegates.interfaces.AdapterPositionListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.UpdateRequest

class PositionListChangeDelegate<Parent, Model>(
    override val listActions: AdapterPositionListActions<Parent, Model>
) : ListChangeDelegate<Parent, Model>(),
    AdapterPositionListChangeDelegate<Parent, Model>
        where Model : AdapterParentViewModel<out Parent, Parent> {

    override suspend fun add(position: Int, item: Parent) {
        if (isValidPosition(position)) {
            addItemByPosition(position, item)
        } else throw IndexOutOfBoundsException("List size is ${getItemCount()} but adding position is $position")
    }

    override suspend fun add(position: Int, items: List<Parent>) {
        if (isValidPosition(position)) {
            addItemsByPosition(position, items)
        } else throw IndexOutOfBoundsException("List size is ${getItemCount()} but adding position is $position")
    }

    override suspend fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        var isUpdated: Boolean

        with(updateRequest) {
            val modelsToRemove = findOutdatedModels(list, getModels())
            isUpdated = removeModels(modelsToRemove)

            list.forEachIndexed { newPosition, item ->
                val oldModel = getModels().find { it.areItemsTheSame(item) }
                if (oldModel == null) {
                    isUpdated = true
                    add(newPosition, item)
                } else {
                    val oldPosition = getPositionOfItem(item)
                    listActions.changeModelPosition(oldModel, oldPosition, newPosition)
                    if (updateModel(oldModel, item)) isUpdated = true
                }
            }

            return isUpdated
        }
    }

    internal suspend fun addModelByPosition(position: Int, model: Model) {
        listActions.addModelByPosition(position, model)
    }

    internal suspend fun addItemByPosition(position: Int, item: Parent) {
        addModelByPosition(position, createModel(item))
    }

    internal suspend fun addItemsByPosition(position: Int, items: List<Parent>) {
        listActions.addModelsByPosition(position, createModels(items))
    }

    fun isValidPosition(position: Int, models: List<Model> = getModels()): Boolean {
        return models.size >= position
    }
}