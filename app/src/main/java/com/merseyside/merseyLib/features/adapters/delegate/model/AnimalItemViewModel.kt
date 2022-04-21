package com.merseyside.merseyLib.features.adapters.delegate.model

import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.merseyLib.features.adapters.delegate.entity.Animal

abstract class AnimalItemViewModel<T : Animal>(item: T)
    : ComparableAdapterParentViewModel<T, Animal>(item) {

    override fun areItemsTheSame(other: T): Boolean {
        return item.name == other.name
    }

    override fun compareTo(other: Animal): Int {
        return this.item.age.compareTo(other.age)
    }

    fun getName() = item.name
    fun getAge() = item.age
}