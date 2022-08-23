package com.merseyside.adapters.feature.filter

import com.merseyside.adapters.listDelegates.PositionListChangeDelegate
import com.merseyside.adapters.listDelegates.interfaces.AdapterPositionListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.merseyLib.kotlin.logger.ILogger

internal class FilterPositionListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    override val listChangeDelegate: PositionListChangeDelegate<Parent, Model>,
    override val filterFeature: FilterFeature<Parent, Model>
) : FilterListChangeDelegate<Parent, Model>(filterFeature),
    AdapterPositionListChangeDelegate<Parent, Model>, ILogger {

    override fun add(position: Int, item: Parent) {
        with(filterFeature) {
            val model = createModel(item)
            if (isFiltered()) {
                if (listChangeDelegate.isValidPosition(position, allModelList)) {
                    mutAllModelList.add(position, model)

                    if (filter(model)) {
                        val filteredPosition = calculatePositionInFilteredList(position)
                        listChangeDelegate.addModelByPosition(filteredPosition, model)
                    }
                }
            } else {
                listChangeDelegate.addModelByPosition(position, model)
            }
        }
    }

    override fun add(position: Int, items: List<Parent>) {
        items.forEachIndexed { index, parent ->
            add(position + index, parent)
        }
    }

    override fun update(models: List<Model>) {
        update(UpdateRequest(models.map { it.item }))
    }

    private fun calculatePositionInFilteredList(desiredPosition: Int): Int {
        fun getNearestFilteredPosition(): Int {
            for (index in desiredPosition until allModelList.size) {
                if (filteredList.contains(allModelList[index])) return index
            }

            for (index in desiredPosition downTo 0) {
                if (filteredList.contains(allModelList[index])) return index
            }

            return 0
        }

        return if (areListsEquals()) {
            desiredPosition
        } else {
            val pos = getNearestFilteredPosition()
            if (pos < desiredPosition) pos + 1
            else pos
        }
    }

}