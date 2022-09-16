package com.merseyside.adapters.delegates.composites

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.delegates.DelegateAdapter
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.delegates.SimpleDelegatesManager
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import kotlinx.coroutines.CoroutineScope

abstract class BaseCompositeAdapter<Parent, ParentModel>(
    scope: CoroutineScope,
    delegatesManager: DelegatesManager<DelegateAdapter<out Parent, Parent, ParentModel>, Parent, ParentModel>,
) : BaseAdapter<Parent, ParentModel>(scope)
    where ParentModel : AdapterParentViewModel<out Parent, Parent> {

    open val delegatesManager: DelegatesManager<DelegateAdapter<out Parent, Parent, ParentModel>, Parent, ParentModel> = delegatesManager

    override val adapter: RecyclerView.Adapter<TypedBindingHolder<ParentModel>>
        get() = this

    init {
        delegatesManager.setOnDelegateRemoveCallback { delegate ->
            val removeList = models.filter { delegate.isResponsibleFor(it.item) }
            removeAsync(removeList.map { it.item })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getViewTypeByItem(getModelByPosition(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypedBindingHolder<ParentModel> {
        return delegatesManager.createViewHolder(parent, viewType)
    }

    override fun bindModel(holder: TypedBindingHolder<ParentModel>, model: ParentModel, position: Int) {
        delegatesManager.onBindViewHolder(holder, model, position)
    }

    override fun getItemCount() = models.size

    @InternalAdaptersApi
    override fun createModel(item: Parent): ParentModel {
        return delegatesManager.createModel(item)
    }
}