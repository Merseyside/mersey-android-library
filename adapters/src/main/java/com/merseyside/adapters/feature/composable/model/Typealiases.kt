package com.merseyside.adapters.feature.composable.model

import com.merseyside.adapters.feature.composable.view.base.SCV
import com.merseyside.adapters.feature.composable.view.selectable.CSV
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.SelectableAdapterParentViewModel

//open class ViewAdapterViewModel<Parent : SCV>: VM<Parent>()

typealias ViewAdapterViewModel = AdapterParentViewModel<out SCV, SCV>
typealias SelectableViewAdapterViewModel = SelectableAdapterParentViewModel<out CSV, CSV>