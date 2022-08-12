package com.merseyside.adapters.feature.filter

import com.merseyside.adapters.interfaces.base.AdapterPositionListActions
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.list.AdapterPositionListChangeDelegate
import com.merseyside.merseyLib.kotlin.logger.ILogger

internal class FilterPositionListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    override val listActions: AdapterPositionListActions<Parent, Model>,
    filterFeature: FilterFeature<Parent, Model>
) : FilterListChangeDelegate<Parent, Model>(listActions, filterFeature),
    AdapterPositionListChangeDelegate<Parent, Model>, ILogger {

    override fun add(position: Int, item: Parent) {
        with(filterFeature) {
            val model = createModel(item)
            if (isFiltered()) {
                if (isValidPosition(position, allModelList)) {
                    mutAllModelList.add(position, model)

                    if (filter(model) != null) {
                        val filteredPosition = calculatePositionInFilteredList(position)
                        listActions.addModelByPosition(filteredPosition, model)
                    }
                }
            } else {
                listActions.addModelByPosition(position, model)
            }
        }
    }

    override fun add(position: Int, items: List<Parent>) {
        items.forEachIndexed { index, parent ->
            add(position + index, parent)
        }
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