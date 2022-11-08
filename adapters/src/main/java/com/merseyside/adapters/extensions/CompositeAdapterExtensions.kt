package com.merseyside.adapters.extensions

import com.merseyside.adapters.delegates.composites.CompositeAdapter
import com.merseyside.adapters.model.VM

/**
 * Filters modelList by type of [ViewModel]
 * @return models with type of [ViewModel], empty list otherwise.
 */
inline fun <reified ViewModel, Parent> CompositeAdapter<Parent, *>.filterIsInstance(): List<ViewModel>
        where ViewModel : VM<Parent> {
    return models.filterIsInstance<ViewModel>()
}

/**
* Finds first item with type of [ViewModel]
* @return first found item, null otherwise.
*/
inline fun <reified ViewModel, Parent> CompositeAdapter<Parent, *>.findIsInstance(): ViewModel?
        where ViewModel : VM<Parent> {
    return filterIsInstance<ViewModel, Parent>().firstOrNull()
}