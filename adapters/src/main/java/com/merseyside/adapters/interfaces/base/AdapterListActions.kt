package com.merseyside.adapters.interfaces.base

import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.modelList.ModelListCallback
import com.merseyside.adapters.utils.InternalAdaptersApi

interface AdapterListActions<Parent, Model : AdapterParentViewModel<out Parent, Parent>> :
    AdapterListContract<Parent, Model>