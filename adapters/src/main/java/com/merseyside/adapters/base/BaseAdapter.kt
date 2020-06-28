package com.merseyside.adapters.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.model.BaseAdapterViewModel
import com.merseyside.utils.ext.isZero
import com.merseyside.utils.ext.minByNullable
import com.merseyside.adapters.view.BaseBindingHolder

abstract class BaseAdapter<M, T : BaseAdapterViewModel<M>>
    : RecyclerView.Adapter<BaseBindingHolder<T>>(),
    ItemPositionInterface<BaseAdapterViewModel<M>> {

    protected var isRecyclable: Boolean? = null

    private var listener: OnItemClickListener<M>? = null

    protected open val modelList: MutableList<T> = ArrayList()
    private val bindItemList: MutableList<T> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingHolder<T> {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return getBindingHolder(binding)
    }

    open fun getBindingHolder(binding: ViewDataBinding): BaseBindingHolder<T> {
        return BaseBindingHolder(binding)
    }

    @CallSuper
    override fun onBindViewHolder(holder: BaseBindingHolder<T>, position: Int) {
        val obj = getModelByPosition(position)
        bindItemList.add(obj)

        listener?.let { obj.setOnItemClickListener(listener!!) }
        bind(holder, obj)

        if (isRecyclable != null) {
            holder.setIsRecyclable(isRecyclable!!)
        }
    }

    @CallSuper
    internal open fun bind(holder: BaseBindingHolder<T>, obj: T) {
        holder.bind(getBindingVariable(), obj)
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

    fun setOnItemClickListener(listener: OnItemClickListener<M>?) {
        this.listener = listener
    }

    fun getOnItemClickListener(): OnItemClickListener<M>? {
        return listener
    }

    fun onItemClicked(onClick: (M) -> Unit): OnItemClickListener<M> {
        this.listener = object:
            OnItemClickListener<M> {
            override fun onItemClicked(obj: M) {
                onClick.invoke(obj)
            }
        }

        return this.listener!!
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
        add(initItemViewModel(obj))
        notifyDataSetChanged()
    }

    open fun add(list: List<M>) {
        val models = itemsToModels(list)

        addModels(models)
        notifyDataSetChanged()
    }

    internal open fun addModels(list: List<T>) {
        modelList.addAll(list)
    }

    internal fun getModelByObj(obj: M): T? {
        return modelList.firstOrNull { it.areItemsTheSame(obj) }
    }

    open fun remove(obj: M) {
        val foundObj = getModelByObj(obj)

        if (foundObj != null) {
            remove(foundObj)
        }
    }

    open fun remove(list: List<M>) {
        val removeList = list.mapNotNull {
            getModelByObj(it)
        }

        removeList(removeList)
    }

    private fun removeList(list: List<T>) {
        if (list.isNotEmpty()) {
            val smallestPosition = getSmallestPosition(list)

            modelList.removeAll(list)

            notifyPositionsChanged(smallestPosition)
        }
    }

    private fun remove(obj: T) {
        val position = getPositionOfModel(obj)
        modelList.remove(obj)

        notifyPositionsChanged(position)
    }

    protected fun getSmallestPosition(list: List<T>): Int {
        return run minValue@{

            list.minByNullable {
                try {
                    val position = getPositionOfModel(it)

                    if (position.isZero()) return@minByNullable position
                    else position
                } catch (e: IllegalArgumentException) {
                    null
                }
            }?.getPosition() ?: 0
        }
    }

    protected open fun notifyPositionsChanged(startWithPosition: Int) {
        if (startWithPosition < itemCount - 1) {
            (startWithPosition until itemCount).forEach { index ->
                modelList[index].onPositionChanged(index)
            }
        }
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

    @Throws(IllegalArgumentException::class)
    open fun getPositionOfModel(model: T): Int {
        modelList.forEachIndexed { index, t ->
            if (t == model) return index
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
    override fun onViewRecycled(holder: BaseBindingHolder<T>) {
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

    protected fun itemsToModels(list: List<M>): List<T> {
        return list.map { initItemViewModel(it) }
    }

    internal open fun initItemViewModel(obj: M): T {
        return createItemViewModel(obj).apply {
            setItemPositionInterface(this@BaseAdapter)
        }
    }

    protected abstract fun createItemViewModel(obj: M): T

    override fun getPosition(model: BaseAdapterViewModel<M>): Int {
        return getPositionOfModel(model as T)
    }

    override fun isLast(model: BaseAdapterViewModel<M>): Boolean {
        return getPosition(model) == itemCount - 1
    }

    override fun isFirst(model: BaseAdapterViewModel<M>): Boolean {
        return getPosition(model) == 0
    }
}
