package com.merseyside.adapters.interfaces.base

import com.merseyside.adapters.model.VM

interface AdapterActions<Parent, Model : VM<Parent>> :
    AdapterContract<Parent, Model>