@file:OptIn(InternalAdaptersApi::class)
@file:Suppress("UNCHECKED_CAST")

package com.merseyside.adapters.ext

import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.SortedAdapterListUtils

/**
 * Updates model in context of Adapter and recalculates item's position.
 * @return [Item] corresponding to updating [Model]. Have to be a new object in order to make payloads efficient.
 */
context(SortedAdapterListUtils<Parent, ParentModel>)
fun <Parent, ParentModel, Model, Item : Parent> Model.update(block: Model.(oldItem: Item) -> Item)
        where ParentModel : ComparableAdapterParentViewModel<out Parent, Parent>,
              Model : ComparableAdapterParentViewModel<Item, Parent> {
    val parentModel = this as ParentModel
    notifyModelChanged(parentModel, payload(block((this as Model).item)))
}