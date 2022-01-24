package com.merseyside.merseyLib.features.adapters.delegate.model

import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.merseyLib.features.adapters.delegate.entity.Animal

abstract class AnimalItemViewModel<T : Animal>(obj: T)
    : ComparableAdapterParentViewModel<T, Animal>(obj) {

    override fun areParentItemsTheSame(parent: Animal): Boolean {
        return this.item.name == parent.name
    }

    override fun compareTo(other: Animal): Int {
        return this.item.age.compareTo(other.age)
    }

    fun getName() = item.name
    fun getAge() = item.age
}