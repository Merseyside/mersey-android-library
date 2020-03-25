package com.merseyside.merseyLib.adapters

import com.merseyside.merseyLib.model.BaseAdapterViewModel

abstract class BaseSelectableAdapter<M: Any, T: BaseAdapterViewModel<M>> : BaseAdapter<M, T>() {

    interface OnItemSelectedListener<M> {
        fun onSelected(item: M, isSelected: Boolean, isSelectedByUser: Boolean)
    }

    private val listeners: MutableList<OnItemSelectedListener<M>> by lazy { ArrayList<OnItemSelectedListener<M>>() }

    private var selectedItem: T? = null

    fun setOnItemSelectedListener(listener: OnItemSelectedListener<M>) {
        listeners.add(listener)

        if (selectedItem != null) {
            listener.onSelected(
                isSelected = true,
                isSelectedByUser = false,
                item = selectedItem!!.getItem()
            )
        }
    }

    fun removeOnItemClickListener(listener: OnItemSelectedListener<M>) {
        listeners.remove(listener)
    }

    override fun add(model: T) {
        addItemToGroup(model)

        super.add(model)
    }

    override fun add(obj: M) {
        val isNoData = isEmpty()
        val item = createItemViewModel(obj)

        add(item)

        if (isNoData) {
            selectFirstSelectableItem()
        }
        notifyDataSetChanged()
    }

    override fun add(list: List<M>) {
        val isNoData = isEmpty()

        for (obj in list) {
            val item = createItemViewModel(obj)
            add(item)
        }

        if (isNoData) {
            selectFirstSelectableItem()
        }
        notifyDataSetChanged()
    }

    private fun selectFirstSelectableItem() {
        if (!isAllowToCancelSelection()) {

            getAllModels().forEach { item ->
                if (item is SelectableItemInterface) {
                    setItemSelected(item)
                    return
                }
            }
        }
    }

    private fun addItemToGroup(item: T) {
        if (item is SelectableItemInterface) {
            item.setOnItemClickListener(object : OnItemClickListener<M> {
                override fun onItemClicked(obj: M) {
                    setItemSelected(item, true)
                }
            })
        }
    }

    fun selectItem(item: M) {
        if (selectedItem == null || !selectedItem!!.areItemsTheSame(item)) {
            val found = find(item)

            if (found != null && found is SelectableItemInterface) {
                setItemSelected(found)
            }
        }
    }

    fun selectItem(position: Int) {
        val item = getModelByPosition(position)
        if (item is SelectableItemInterface) {

            if (selectedItem == null || !selectedItem!!.areItemsTheSame(item.obj)) {
                setItemSelected(item)
            }
        }
    }

    fun getSelectedItem(): M? {
        return selectedItem?.getItem()
    }

    private fun setItemSelected(item: T, isSelectedByUser: Boolean = false) {

        item as SelectableItemInterface

        if (!item.isSelected) {

            if (selectedItem != null) {
                (selectedItem as SelectableItemInterface).isSelected = false
            }

            item.isSelected = true
            selectedItem = item

            notifyItemSelected(item, isSelectedByUser)
        } else if (isAllowToCancelSelection()) {
            item.isSelected = false

            notifyItemSelected(item, isSelectedByUser)
            selectedItem = null
        }
    }

    private fun notifyItemSelected(item: T, isSelectedByUser: Boolean) {
        item as SelectableItemInterface

        listeners.forEach { listener ->
            listener.onSelected(item.getItem(), item.isSelected, isSelectedByUser)
        }
    }


    abstract fun isAllowToCancelSelection(): Boolean

    companion object {
        private const val TAG = "BaseSelectableAdapter"
    }
}