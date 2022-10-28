package com.merseyside.adapters.compose.model

import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.base.selectable.CSV
import com.merseyside.adapters.model.AdapterParentViewModel

typealias ViewAdapterViewModel = AdapterParentViewModel<out SCV, SCV>
//typealias SelectableViewAdapterViewModel = SelectableAdapterParentViewModel<out CSV, CSV>