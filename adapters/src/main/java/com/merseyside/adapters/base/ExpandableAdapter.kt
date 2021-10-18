package com.merseyside.adapters.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.model.ExpandableAdapterViewModel
import com.merseyside.adapters.view.TypedBindingHolder
import com.merseyside.utils.ext.isNotNullAndEmpty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ExpandableAdapter<M : Any, T : ExpandableAdapterViewModel<M, SubItem>,
        SubItem : Any, InnerAdapter : BaseAdapter<SubItem, *>>(
    selectableMode: SelectableMode = SelectableMode.MULTIPLE,
    isAllowToCancelSelection: Boolean = true,
    scope: CoroutineScope = CoroutineScope(
        Dispatchers.Main + SupervisorJob()
    )
) : SelectableAdapter<M, T>(selectableMode, isAllowToCancelSelection, scope = scope) {

    private var adapterList: MutableList<Pair<T, InnerAdapter>> = ArrayList()
    private var updateRequest: UpdateRequest<M>? = null

    /*Inner adapters add out of sync, so we have to be sure when can work with them */
    var onAdapterInitializedCallback: (InnerAdapter) -> Unit = {}

    override fun onBindViewHolder(holder: TypedBindingHolder<T>, position: Int) {
        super.onBindViewHolder(holder, position)
        val model = getModelByPosition(position)
        val data = model.getData()

        if (data.isNotNullAndEmpty()) {

            val recyclerView: RecyclerView? = getExpandableView(holder.binding)
            recyclerView?.apply {

                var newAdapter = false
                val adapter =
                    getAdapterIfExists(model) ?: initExpandableList(model).also { adapter ->
                        newAdapter = true
                        putAdapter(model, adapter)
                    }

                this.adapter = adapter
                addExpandableItems(adapter, data)

                if (newAdapter) {
                    onAdapterInitializedCallback(adapter)
                }
            }
        }
    }

    fun getAdapterByItem(item: M): InnerAdapter? {
        val model = getModelByObj(item)
        return model?.let {
            getAdapterIfExists(it)
        }
    }

    fun getExpandableAdapters(): List<InnerAdapter> {
        return adapterList.map { it.second }
    }

    private fun putAdapter(model: T, adapter: InnerAdapter) {
        adapterList.add(model to adapter)
    }

    private fun getAdapterIfExists(model: T): InnerAdapter? {
        return adapterList.find { it.first.areItemsTheSame(model.getItem()) }?.second
    }

    abstract fun initExpandableList(model: T): InnerAdapter

    abstract fun getExpandableView(binding: ViewDataBinding): RecyclerView?

    private fun addExpandableItems(adapter: InnerAdapter, list: List<SubItem>) {
        if (adapter.isEmpty()) {
            adapter.add(list)
        } else adapter.update(UpdateRequest.Builder(list)
            .isAddNew(true)
            .isDeleteOld(true)
            .build()
        )
    }

    override fun update(updateRequest: UpdateRequest<M>) {
        this.updateRequest = updateRequest

        if (updateRequest.isDeleteOld) {
            adapterList = adapterList.filter { adapter ->
                updateRequest.list.find { adapter.first.areItemsTheSame(it) } != null
            }.toMutableList()
        }

        super.update(updateRequest)
        this.updateRequest = null
    }

    override fun update(obj: M): Boolean {
        val updated = super.update(obj)

        if (updated) {
            val model = getModelByObj(obj)
            if (model != null) {
                val adapter = getAdapterIfExists(model)
                adapter?.let {
                    model.getData()?.let { data ->
                        adapter.update(UpdateRequest(data))
                    }
                }
            }
        }

        return updated
    }

    override fun setFilter(models: List<T>): List<T>? {
        return models.filter { model ->
            val adapter = getAdapterIfExists(model)
            adapter?.let {
                val hasSubItems = try {
                     it.setFilter(filterPattern)
                } catch (e: NotImplementedError) {
                    true
                }

                val passedFilter = filter(model, filterPattern)
                hasSubItems || passedFilter
            } ?: false
        }.also { isFiltered = true }
    }

    fun removeOnAdapterInitializedCallback() {
        onAdapterInitializedCallback = {}
    }
}