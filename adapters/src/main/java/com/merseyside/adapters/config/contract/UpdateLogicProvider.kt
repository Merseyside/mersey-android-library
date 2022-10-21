package com.merseyside.adapters.config.contract

import com.merseyside.adapters.config.update.UpdateActions
import com.merseyside.adapters.config.update.UpdateLogic
import com.merseyside.adapters.model.AdapterParentViewModel

interface UpdateLogicProvider<Parent, Model: AdapterParentViewModel<out Parent, Parent>> {

    fun updateLogic(updateActions: UpdateActions<Parent, Model>): UpdateLogic<Parent, Model>
}