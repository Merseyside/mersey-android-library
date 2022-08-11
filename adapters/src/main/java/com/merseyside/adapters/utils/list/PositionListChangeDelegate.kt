package com.merseyside.adapters.utils.list

import com.merseyside.adapters.interfaces.base.AdapterPositionListActions
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.UpdateRequest

class PositionListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    override val listActions: AdapterPositionListActions<Parent, Model>
) : DefaultListChangeDelegate<Parent, Model>(listActions), AdapterPositionListChangeDelegate<Parent, Model> {

    override fun add(position: Int, item: Parent) {
        if (getItemCount() >= position) {
            listActions.addModelByPosition(position, createModel(item))
        } else throw IndexOutOfBoundsException("List size is ${getItemCount()} but adding position is $position")
    }

    override fun add(position: Int, items: List<Parent>) {
        if (getItemCount() >= position) {
            listActions.addModelsByPosition(position, createModels(items))
        } else throw IndexOutOfBoundsException("List size is ${getItemCount()} but adding position is $position")
    }

    override fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        var isUpdated: Boolean

        with(updateRequest) {
            isUpdated = removeOldItems(list)

            list.forEachIndexed { newPosition, item ->
                val oldModel = getModels().find { it.areItemsTheSame(item) }
                if (oldModel == null) {
                    add(newPosition, item)
                } else {
                    val oldPosition = getPositionOfItem(item)
                    listActions.changeModelPosition(oldModel, oldPosition, newPosition)
                    if (update(oldModel, item)) isUpdated = true
                }
            }

            return isUpdated
        }
    }
}