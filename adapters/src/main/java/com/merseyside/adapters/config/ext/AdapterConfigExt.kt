package com.merseyside.adapters.base.config.ext

import com.merseyside.adapters.config.feature.Feature
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.feature.filtering.AdapterFilter
import com.merseyside.adapters.feature.filtering.FilterFeature
import com.merseyside.adapters.feature.sorting.Comparator
import com.merseyside.adapters.feature.sorting.SortFeature
import com.merseyside.adapters.model.VM

fun AdapterConfig<*, *>.hasFeature(featureKey: String): Boolean {
    return featureList.find { it.featureKey == featureKey } != null
}

fun <Parent, Model : VM<Parent>>
        AdapterConfig<Parent, Model>.getFeatureByKey(featureKey: String): Feature<Parent, Model>? {
    return featureList.find { it.featureKey == featureKey }
}

fun <Parent, Model : VM<Parent>, F: Feature<Parent, Model>>
        AdapterConfig<Parent, Model>.getFeatureByInstance(clazz: Class<F>): F? {
    return featureList.filterIsInstance(clazz).firstOrNull()
}

fun <Parent, Model : VM<Parent>> AdapterConfig<Parent, Model>
        .getAdapterFilter(): AdapterFilter<Parent, Model>? {

    return featureList.filterIsInstance<FilterFeature<Parent, Model>>().firstOrNull()?.adapterFilter
}

fun <Parent, Model : VM<Parent>> AdapterConfig<Parent, Model>
        .getAdapterComparator(): Comparator<Parent, Model>? {

    return featureList.filterIsInstance<SortFeature<Parent, Model>>().firstOrNull()?.comparator
}