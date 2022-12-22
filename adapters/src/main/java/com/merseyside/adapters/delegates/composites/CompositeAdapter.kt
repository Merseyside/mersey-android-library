package com.merseyside.adapters.delegates.composites

import android.view.ViewGroup
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.DA
import com.merseyside.adapters.model.VM
import com.merseyside.adapters.utils.InternalAdaptersApi

open class CompositeAdapter<Parent, ParentModel>(
    adapterConfig: AdapterConfig<Parent, ParentModel>,
    delegatesManager: DelegatesManager<DA<Parent, ParentModel>, Parent, ParentModel> = DelegatesManager()
) : BaseAdapter<Parent, ParentModel>(adapterConfig)
        where ParentModel : VM<Parent> {

    open val delegatesManager: DelegatesManager<DA<Parent, ParentModel>, Parent, ParentModel> =
        delegatesManager

    init {
        delegatesManager.setOnDelegateRemoveCallback { delegate ->
            val removeList = models.filter { delegate.isResponsibleFor(it.item) }
            remove(removeList.map { it.item })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getViewTypeByItem(getModelByPosition(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TypedBindingHolder<ParentModel> {
        return delegatesManager.createViewHolder(parent, viewType)
    }

    override fun bindModel(
        holder: TypedBindingHolder<ParentModel>,
        model: ParentModel,
        position: Int
    ) {
        super.bindModel(holder, model, position)
        delegatesManager.onBindViewHolder(holder, model, position)
    }

    override fun onBindViewHolder(
        holder: TypedBindingHolder<ParentModel>,
        position: Int,
        payloads: List<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        delegatesManager.onBindViewHolder(holder, position, payloads)
    }

    @InternalAdaptersApi
    override fun createModel(item: Parent): ParentModel {
        return delegatesManager.createModel(item)
    }
}