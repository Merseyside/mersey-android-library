package com.merseyside.adapters.feature.selecting

import com.merseyside.adapters.model.VM

interface SelectProvider<Parent, Model>
        where Model : VM<Parent> {

    val adapterSelect: AdapterSelect<Parent, Model>
}