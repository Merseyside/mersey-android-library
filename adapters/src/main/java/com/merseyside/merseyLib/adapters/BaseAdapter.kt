package com.merseyside.merseyLib.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.merseyLib.model.BaseAdapterViewModel
import com.merseyside.merseyLib.presentation.view.BaseViewHolder
import kotlin.IllegalArgumentException

abstract class BaseAdapter<M, T : BaseAdapterViewModel<M>> : RecyclerView.Adapter<BaseViewHolder>() {

    protected var isRecyclable: Boolean? = null

    private var listener: OnItemClickListener<M>? = null

    protected open val modelList: MutableList<T> = ArrayList()
    private val bindItemList: MutableList<T> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val obj = getModelByPosition(position)
        bindItemList.add(obj)

        listener?.let { obj.setOnItemClickListener(listener!!) }
        holder.bind(getBindingVariable(), obj)

        if (isRecyclable != null) {
            holder.setIsRecyclable(isRecyclable!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    protected open fun getModelByPosition(position: Int): T {
        return modelList[position]
    }

    open fun getItemByPosition(position: Int): M {
        return getModelByPosition(position).getItem()
    }

    protected abstract fun getLayoutIdForPosition(position: Int): Int

    protected abstract fun getBindingVariable(): Int

    fun setOnItemClickListener(listener: OnItemClickListener<M>) {
        this.listener = listener
    }

    open fun removeOnItemClickListener(listener: OnItemClickListener<M>) {
        bindItemList.forEach { model -> model.removeOnItemClickListener(listener) }
    }

    interface OnItemClickListener<M> {
        fun onItemClicked(obj: M)
    }

    override fun getItemCount() = modelList.size

    protected open fun add(model: T) {
        modelList.add(model)
    }

    open fun add(obj: M) {
        add(createItemViewModel(obj))
        notifyDataSetChanged()
    }

    open fun add(list: List<M>) {
        for (obj in list) {
            this.modelList.add(createItemViewModel(obj))
        }
        notifyDataSetChanged()
    }

    open fun remove(obj: M) {
        val foundObj = modelList.firstOrNull { it.areItemsTheSame(obj) }

        if (foundObj != null) {
            remove(foundObj)
        }
    }

    open fun remove(list: List<M>) {
        list.forEach {
            remove(it)
        }
    }

    private fun remove(obj: T) {
        modelList.remove(obj)
    }

    open fun clear() {
        modelList.clear()
        notifyDataSetChanged()
    }

    @Throws(IllegalArgumentException::class)
    open fun getPositionOfObj(obj: M): Int {
        modelList.forEachIndexed { index, t ->
            if (t.areItemsTheSame(obj)) return index
        }

        throw IllegalArgumentException("No data found")
    }

    open fun find(obj: M): T? {
        modelList.forEach {
            if (it.areItemsTheSame(obj)) {
                return it
            }
        }

        return null
    }

    /**
     * Call this when actual object has already changed
     * @param obj is changed object
     */
    @Throws(IllegalArgumentException::class)
    open fun notifyItemChanged(obj: M) {
        find(obj)?.notifyUpdate()
    }

    open fun setFilter(query: String) {
        throw NotImplementedError()
    }

    open fun setFilterAsync(query: String, func: () -> Unit = {}) {
        throw NotImplementedError()
    }

    open fun filter(obj: T, query: String): Boolean {
        return true
    }

    open fun filter(obj: T, filterMap : Map<String, Any>): Boolean {
        return true
    }

    open fun getAll(): List<M> {
        return modelList.map { it.obj }
    }

    fun notifyUpdateAll() {
        modelList.forEach { it.notifyUpdate() }
    }

    protected open fun getAllModels() = modelList.toList()

    /**
     * @return true if modelList has items else - false
     */
    open fun isEmpty(): Boolean = modelList.isEmpty()

    open fun isNotEmpty(): Boolean = !isEmpty()

    @Throws(IndexOutOfBoundsException::class)
    open fun first(): M {
        try {
            return getModelByPosition(0).getItem()
        } catch (e: Exception) {
            throw IndexOutOfBoundsException("List is empty")
        }
    }

    @Throws(IndexOutOfBoundsException::class)
    open fun last(): M {
        try {
            return getModelByPosition(itemCount - 1).getItem()
        } catch (e: Exception) {
            throw IndexOutOfBoundsException("List is empty")
        }
    }

    @CallSuper
    override fun onViewRecycled(holder: BaseViewHolder) {
        super.onViewRecycled(holder)
        if (holder.adapterPosition != RecyclerView.NO_POSITION && holder.adapterPosition < itemCount) {

            getModelByPosition(holder.adapterPosition).apply {
                bindItemList.remove(this)
                listener?.let {
                    removeOnItemClickListener(it)
                }
                onRecycled()
            }
        }
    }

    protected abstract fun createItemViewModel(obj: M): T

    companion object {
        private const val TAG = "BaseAdapter"
    }
}
