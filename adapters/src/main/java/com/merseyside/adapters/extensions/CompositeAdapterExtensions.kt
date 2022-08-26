package com.merseyside.adapters.extensions

import com.merseyside.adapters.delegates.composites.CompositeAdapter
import com.merseyside.adapters.model.AdapterParentViewModel

/**
 * Filters modelList by type of [VM]
 * @return models with type of [VM], empty list otherwise.
 */
inline fun <reified VM, Parent> CompositeAdapter<Parent, *>.filterIsInstance(): List<VM>
        where VM : AdapterParentViewModel<out Parent, Parent> {
    return models.filterIsInstance<VM>()
}

/**
* Finds first item with type of [VM]
* @return first found item, null otherwise.
*/
inline fun <reified VM, Parent> CompositeAdapter<Parent, *>.findIsInstance(): VM?
        where VM : AdapterParentViewModel<out Parent, Parent> {
    return filterIsInstance<VM, Parent>().firstOrNull()
}