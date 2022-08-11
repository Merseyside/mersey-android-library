package com.merseyside.adapters.interfaces.simple

import com.merseyside.adapters.interfaces.base.AdapterPositionListActions
import com.merseyside.adapters.model.AdapterParentViewModel

interface SimpleAdapterListActions<Parent, Model : AdapterParentViewModel<out Parent, Parent>> :
    AdapterPositionListActions<Parent, Model>