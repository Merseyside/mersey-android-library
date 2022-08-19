package com.merseyside.adapters.feature.filter

import com.merseyside.adapters.feature.filter.interfaces.FilterFeature
import com.merseyside.adapters.interfaces.simple.AdapterPositionListActions
import com.merseyside.adapters.listDelegates.PositionListChangeDelegate
import com.merseyside.adapters.listDelegates.interfaces.AdapterPositionListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.merseyLib.kotlin.logger.ILogger

internal class FilterPositionListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    override val listChangeDelegate: PositionListChangeDelegate<Parent, Model>,
    override val filterFeature: FilterFeature<Parent, Model>
) : FilterListChangeDelegate<Parent, Model>(filterFeature),
    AdapterPositionListChangeDelegate<Parent, Model>, ILogger {

    override val listActions: AdapterPositionListActions<Parent, Model>
        get() = listChangeDelegate.listActions

    override fun add(position: Int, item: Parent) {
        with(filterFeature) {
            val model = createModel(item)
            if (isFiltered()) {
                if (listChangeDelegate.isValidPosition(position, allModelList)) {
                    mutAllModelList.add(position, model)

                    if (filter(model)) {
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