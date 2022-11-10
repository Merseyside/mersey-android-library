package com.merseyside.adapters.delegates.composites

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.delegates.DelegateAdapter
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.DA
import com.merseyside.adapters.utils.InternalAdaptersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.merseyside.adapters.model.VM

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
            removeAsync(removeList.map { it.item })
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

    @InternalAdaptersApi
    override fun createModel(item: Parent): ParentModel {
        return delegatesManager.createModel(item)
    }
}