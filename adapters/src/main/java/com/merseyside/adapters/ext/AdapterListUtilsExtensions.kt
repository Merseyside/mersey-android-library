@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.ext

import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

inline fun <reified R: Parent, Parent> IBaseAdapter<Parent, *>.filterItemsIsInstance(): List<R> {
    return getAll().filterIsInstance<R>()
}

inline fun <reified R: Parent, Parent> IBaseAdapter<Parent, *>.findItemsIsInstance(): R {
    return filterItemsIsInstance<R, Parent>().first()
}

/**
 * Updates model in context of Adapter and recalculates item's position.
 * @return [Item] corresponding to updating [Model]. Have to be a new object in order to make payloads efficient.
 */
context(IBaseAdapter<Parent, ParentModel>)
fun <Parent, ParentModel, Model, Item : Parent> Model.update(block: Model.(oldItem: Item) -> Item)
        where ParentModel : AdapterParentViewModel<out Parent, Parent>,
              Model : AdapterParentViewModel<Item, Parent> {
    val parentModel = this as ParentModel
    notifyModelUpdated(parentModel, payload(block((this as Model).item)))
}