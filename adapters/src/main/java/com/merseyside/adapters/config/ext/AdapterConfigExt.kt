package com.merseyside.adapters.base.config

import com.merseyside.adapters.config.feature.Feature
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.feature.filter.AdapterFilter
import com.merseyside.adapters.feature.filter.FilterFeature
import com.merseyside.adapters.feature.sorting.Comparator
import com.merseyside.adapters.feature.sorting.SortFeature
import com.merseyside.adapters.model.AdapterParentViewModel

fun AdapterConfig<*, *>.hasFeature(featureKey: String): Boolean {
    return featureList.find { it.featureKey == featureKey } != null
}

fun <Parent, Model : AdapterParentViewModel<out Parent, Parent>>
        AdapterConfig<Parent, Model>.getFeatureByKey(featureKey: String): Feature<Parent, Model>? {
    return featureList.find { it.featureKey == featureKey }
}

fun <Parent, Model : AdapterParentViewModel<out Parent, Parent>, F: Feature<Parent, Model>>
        AdapterConfig<Parent, Model>.getFeatureByInstance(clazz: Class<F>): F? {
    return featureList.filterIsInstance(clazz).firstOrNull()
}

fun <Parent, Model : AdapterParentViewModel<out Parent, Parent>> AdapterConfig<Parent, Model>
        .getAdapterFilter(): AdapterFilter<Parent, Model>? {

    return featureList.filterIsInstance<FilterFeature<Parent, Model>>().firstOrNull()?.adapterFilter
}

fun <Parent, Model : AdapterParentViewModel<out Parent, Parent>> AdapterConfig<Parent, Model>
        .getAdapterComparator(): Comparator<Parent, Model>? {

    return featureList.filterIsInstance<SortFeature<Parent, Model>>().firstOrNull()?.comparator
}