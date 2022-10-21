package com.merseyside.adapters.interfaces.base

import com.merseyside.adapters.model.VM

interface AdapterListActions<Parent, Model : VM<Parent>> :
    AdapterListContract<Parent, Model>