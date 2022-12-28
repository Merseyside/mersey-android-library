package com.merseyside.adapters.feature.sorting

import com.merseyside.adapters.model.VM

interface ComparatorProvider<Parent, Model>
        where Model : VM<Parent> {

    val comparator: Comparator<Parent, Model>
}