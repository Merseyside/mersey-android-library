package com.merseyside.adapters.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.model.BaseExpandableAdapterViewModel
import com.merseyside.utils.ext.isNotNullAndEmpty
import com.merseyside.adapters.view.TypedBindingHolder

abstract class BaseExpandableAdapter<M: Any, T: BaseExpandableAdapterViewModel<M, L>, L: Any> (
    selectableMode: SelectableMode = SelectableMode.MULTIPLE,
    isAllowToCancelSelection: Boolean = true
): BaseSelectableAdapter<M, T>(selectableMode, isAllowToCancelSelection) {

    private var adapterList: MutableList<Pair<T, BaseAdapter<L, *>>> = ArrayList()
    private var updateRequest: UpdateRequest<M>? = null

    override fun onBindViewHolder(holder: TypedBindingHolder<T>, position: Int) {
        super.onBindViewHolder(holder, position)
        val model = getModelByPosition(position)
        val data = model.getData()

        if (data.isNotNullAndEmpty()) {

            val recyclerView: RecyclerView? = getExpandableView(holder.binding)
            recyclerView?.apply {

                val adapter = getAdapterIfExists(model) ?: initExpandableList().also { adapter ->
                    putAdapter(model, adapter)
                }

                this.adapter = adapter
                addExpandableItems(adapter, data)
            }
        }
    }

    private fun putAdapter(model: T, adapter: BaseAdapter<L, *>) {
        adapterList.add(model to adapter)
    }

    private fun getAdapterIfExists(model: T): BaseAdapter<L, *>? {
        return adapterList.find { it.first.areItemsTheSame(model.getItem()) }?.second
    }

    abstract fun initExpandableList(): BaseAdapter<L, *>

    abstract fun getExpandableView(binding: ViewDataBinding): RecyclerView?

    open fun addExpandableItems(adapter: BaseAdapter<L, *>, list: List<L>) {
        adapter.add(list)
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

                if (adapter is BaseSortedAdapter<L, *>) {
                    adapter.let {
                        val updateRequest = UpdateRequest.Builder(
                            model.getData() ?: emptyList())
                            .isAddNew(this.updateRequest!!.isAddNew)
                            .isDeleteOld(this.updateRequest!!.isDeleteOld)
                            .build()

                        adapter.update(updateRequest)
                    }

                }
            }
        }

        return updated
    }
}