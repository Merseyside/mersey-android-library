@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.delegates.composites

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.ItemCallback
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.adapters.utils.AdapterListUtils
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.view.TypedBindingHolder
import com.merseyside.merseyLib.kotlin.concurency.Locker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.sync.Mutex

abstract class CompositeAdapter<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    val delegatesManager: DelegatesManager<Parent, Model> = DelegatesManager(),
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : RecyclerView.Adapter<TypedBindingHolder<Model>>(),
    ItemCallback<AdapterViewModel<Parent>>,
    AdapterListUtils<Parent, Model>, Locker {

    override var addJob: Job? = null
    override var updateJob: Job? = null
    override var filterJob: Job? = null

    override var isFiltered: Boolean = false
    override val filtersMap: HashMap<String, Any> by lazy { HashMap() }
    override val notAppliedFiltersMap: HashMap<String, Any> by lazy { HashMap() }
    override var filterPattern: String = ""
    override val filterKeyMap: MutableMap<String, List<Model>> by lazy { HashMap() }

    override val lock = Any()
    override val mutex: Mutex = Mutex()

    override var listener: OnItemClickListener<Parent>? = null

    override val modelList: MutableList<Model> = ArrayList()
    override val adapter: RecyclerView.Adapter<TypedBindingHolder<Model>>
        get() = this

    protected var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getViewTypeByItem(getModelByPosition(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypedBindingHolder<Model> {
        return delegatesManager.createViewHolder(parent, viewType)
    }

    @CallSuper
    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, position: Int) {
        delegatesManager.bindViewHolder(holder, getModelByPosition(position), position)
        listener?.let { holder.getModel().setOnItemClickListener(it) }
    }

    override fun getItemCount() = modelList.size

    override fun createModel(item: Parent): Model {
        val model = delegatesManager.createModel(item)
        onModelCreated(model)
        return model
    }

    @CallSuper
    protected open fun onModelCreated(model: Model) {
        listener?.let {
            model.setOnItemClickListener(it)
        }
    }

    open fun removeListeners() {
        listener = null
    }
}