package com.merseyside.adapters.interfaces.ext

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.model.VM
import com.merseyside.adapters.utils.UpdateRequest

fun <Parent, Model : VM<Parent>> BaseAdapter<Parent, Model>.addAsync(
    item: Parent,
    onComplete: (Model?) -> Unit = {}
) {
    workManager.doAsync(onComplete) { add(item) }
}

fun <Parent, Model : VM<Parent>> BaseAdapter<Parent, Model>.addAsync(
    items: List<Parent>,
    onComplete: (Unit) -> Unit = {}
) {
    workManager.doAsync(onComplete) { add(items) }
}

fun <Parent, Model : VM<Parent>> BaseAdapter<Parent, Model>.addOrUpdateAsync(
    items: List<Parent>,
    onComplete: (Unit) -> Unit = {}
) {
    workManager.doAsync(onComplete) { addOrUpdate(items) }
}

fun <Parent, Model : VM<Parent>> BaseAdapter<Parent, Model>.updateAsync(
    updateRequest: UpdateRequest<Parent>,
    provideResult: (Boolean) -> Unit = {}
) {
    workManager.doAsync(provideResult) { update(updateRequest) }
}

fun <Parent, Model : VM<Parent>> BaseAdapter<Parent, Model>.updateAsync(
    items: List<Parent>,
    onComplete: (Boolean) -> Unit = {}
) {
    workManager.doAsync(onComplete) { update(items) }
}

fun <Parent, Model : VM<Parent>> BaseAdapter<Parent, Model>.removeAsync(
    item: Parent,
    onComplete: (Model?) -> Unit = {}
) {
    workManager.doAsync(onComplete) { remove(item) }
}

fun <Parent, Model : VM<Parent>> BaseAdapter<Parent, Model>.removeAsync(
    items: List<Parent>,
    onComplete: (List<Model>) -> Unit = {}
) {
    workManager.doAsync(onComplete) { remove(items) }
}

fun <Parent, Model : VM<Parent>> BaseAdapter<Parent, Model>.getModelByItemAsync(
    item: Parent,
    onComplete: (Model?) -> Unit
) {
    workManager.doAsync(onComplete) { getModelByItem(item) }
}

fun <Parent, Model : VM<Parent>> BaseAdapter<Parent, Model>.clearAsync(
    onComplete: (Unit) -> Unit = {}
) {
    workManager.doAsync(onComplete, work = ::clear)
}


/* Position */

fun <Parent, Model : VM<Parent>> BaseAdapter<Parent, Model>.addAsync(
    position: Int,
    item: Parent,
    onComplete: (Unit) -> Unit
) {
    workManager.doAsync(onComplete) { add(position, item) }
}

fun <Parent, Model : VM<Parent>> BaseAdapter<Parent, Model>.addAsync(
    position: Int,
    items: List<Parent>,
    onComplete: (Unit) -> Unit
) {
    workManager.doAsync(onComplete) { add(position, items) }
}