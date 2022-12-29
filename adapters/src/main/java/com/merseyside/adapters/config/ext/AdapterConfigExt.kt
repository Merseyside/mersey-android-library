package com.merseyside.adapters.config.ext

import com.merseyside.adapters.config.feature.Feature
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.contract.FilterProvider
import com.merseyside.adapters.feature.filtering.AdapterFilter
import com.merseyside.adapters.feature.filtering.FilterFeature
import com.merseyside.adapters.feature.selecting.AdapterSelect
import com.merseyside.adapters.feature.selecting.SelectFeature
import com.merseyside.adapters.feature.selecting.SelectProvider
import com.merseyside.adapters.feature.sorting.Comparator
import com.merseyside.adapters.feature.sorting.ComparatorProvider
import com.merseyside.adapters.feature.sorting.SortFeature
import com.merseyside.adapters.model.VM

fun AdapterConfig<*, *>.hasFeature(featureKey: String): Boolean {
    return featureList.find { it.featureKey == featureKey } != null
}

fun <Parent> AdapterConfig<Parent, *>.getFeatureByKey(featureKey: String): Feature<Parent, *>? {
    return featureList.find { it.featureKey == featureKey }
}

fun <Parent, F : Feature<Parent, *>>
        AdapterConfig<Parent, *>.getFeatureByInstance(clazz: Class<F>): F? {
    return featureList.filterIsInstance(clazz).firstOrNull()
}

fun <Parent, Model : VM<Parent>> AdapterConfig<Parent, Model>.getAdapterFilter(): AdapterFilter<Parent, Model>? {
    return featureList.filterIsInstance<FilterProvider<Parent, Model>>()
        .firstOrNull()?.adapterFilter
}

fun <Parent, Model : VM<Parent>> AdapterConfig<Parent, Model>.getAdapterComparator(): Comparator<Parent, Model>? {
    return featureList.filterIsInstance<ComparatorProvider<Parent, Model>>()
        .firstOrNull()?.comparator
}

fun <Parent, Model : VM<Parent>> AdapterConfig<Parent, Model>.getAdapterSelect(): AdapterSelect<Parent, *>? {
    return featureList.filterIsInstance<SelectProvider<Parent, Model>>()
        .firstOrNull()?.adapterSelect
}