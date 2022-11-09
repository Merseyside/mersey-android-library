package com.merseyside.adapters.extensions

import com.merseyside.adapters.interfaces.base.IBaseAdapter

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
//context(BaseAdapter<Parent, ParentModel>)
//suspend fun <Parent, ParentModel, Model, Item : Parent> Model.update(block: Model.(oldItem: Item) -> Item)
//        where ParentModel : AdapterParentViewModel<out Parent, Parent>,
//              Model : AdapterParentViewModel<Item, Parent> {
//    val parentModel = this as ParentModel
//    notifyModelUpdated(parentModel, payload(block((this as Model).item)))
//}