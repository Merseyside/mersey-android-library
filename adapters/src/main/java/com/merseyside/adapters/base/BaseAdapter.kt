package com.merseyside.adapters.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.adapters.view.TypedBindingHolder
import com.merseyside.merseyLib.kotlin.extensions.isZero
import com.merseyside.merseyLib.kotlin.extensions.minByNullable

abstract class BaseAdapter<M, T : AdapterViewModel<M>>
    : RecyclerView.Adapter<TypedBindingHolder<T>>(),
    ItemCallback<AdapterViewModel<M>>,
    HasOnItemClickListener<M> {

    protected var isRecyclable: Boolean = true

    override var listener: OnItemClickListener<M>? = null

    internal open val modelList: MutableList<T> = ArrayList()
    private val bindItemList: MutableList<T> = ArrayList()
    lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypedBindingHolder<T> {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return getBindingHolder(binding)
    }

    open fun getBindingHolder(binding: ViewDataBinding): TypedBindingHolder<T> {
        return TypedBindingHolder(binding)
    }

    @CallSuper
    override fun onBindViewHolder(holder: TypedBindingHolder<T>, position: Int) {
        val obj = getModelByPosition(position)
        obj.onPositionChanged(position)

        bindItemList.add(obj)

        listener?.let { obj.setOnItemClickListener(it) }
        bind(holder, obj)

        if (!isRecyclable || isRecyclable && !holder.isRecyclable) {
            holder.setIsRecyclable(isRecyclable)
        }
    }

    @CallSuper
    internal open fun bind(holder: TypedBindingHolder<T>, obj: T) {
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

    open fun removeOnItemClickListener(listener: OnItemClickListener<M>) {
        bindItemList.forEach { model -> model.removeOnItemClickListener(listener) }
    }

    override fun getItemCount() = modelList.size

    protected open fun add(model: T) {

        modelList.add(model)
    }

    open fun add(obj: M) {
        add(initItemViewModel(obj))
        notifyItemInserted(itemCount - 1)
    }

    open fun add(list: List<M>) {
        val startPosition = itemCount - 1
        val models = itemsToModels(list)

        addModels(models)
        notifyItemRangeChanged(startPosition, models.size)
    }

    open fun update(updateRequest: UpdateRequest<M>): Boolean {
        TODO("Not implemented")
    }

    internal open fun update(obj: M): Boolean {
        TODO("Not implemented")
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

            notifyItemsRemoved(smallestPosition)
        }
    }

    private fun remove(obj: T) {
        val position = getPositionOfModel(obj)
        modelList.remove(obj)

        notifyItemsRemoved(position)
    }

    private fun getSmallestPosition(list: List<T>): Int {
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

    protected open fun notifyItemsRemoved(startsWithPosition: Int) {
        if (startsWithPosition < itemCount - 1) {
            (startsWithPosition until itemCount).forEach { index ->
                modelList[index].onPositionChanged(index)
            }
        }

        notifyDataSetChanged()
    }

    /**
     * -1 means item has removed.
     */
    protected open fun notifyItemMoved(
        toPosition: Int
    ) {
        getModelByPosition(toPosition).onPositionChanged(toPosition)
    }

    open fun clear() {
        modelList.clear()
        notifyDataSetChanged()
    }

    @Throws(IllegalArgumentException::class)
    open fun getPositionOfItem(obj: M): Int {
        modelList.forEachIndexed { index, t ->
            if (t.areItemsTheSame(obj)) return index
        }

        throw IllegalArgumentException("No data found")
    }

    @Throws(IllegalArgumentException::class)
    protected open fun getPositionOfModel(model: T): Int {
        modelList.forEachIndexed { index, t ->
            if (t == model) return index
        }

        throw IllegalArgumentException("No data found")
    }

    internal open fun find(obj: M): T? {
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

    @Throws(NotImplementedError::class)
    open fun setFilter(query: String): Int {
        throw NotImplementedError()
    }

    @Throws(NotImplementedError::class)
    open fun setFilterAsync(query: String, callback: (filteredCount: Int) -> Unit = {}) {
        throw NotImplementedError()
    }

    open fun filter(obj: T, query: String): Boolean {
        return true
    }

    open fun filter(obj: T, key: String, filterObj: Any): Boolean {
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
    override fun onViewRecycled(holder: TypedBindingHolder<T>) {
        super.onViewRecycled(holder)
        if (holder.absoluteAdapterPosition != RecyclerView.NO_POSITION && holder.absoluteAdapterPosition < itemCount) {

            getModelByPosition(holder.absoluteAdapterPosition).apply {
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

    internal fun getModelsByItems(list: List<M>): List<T> {
        return list.mapNotNull { item -> modelList.find { model -> model.areItemsTheSame(item) } }
    }

    internal open fun initItemViewModel(obj: M): T {
        return createItemViewModel(obj).apply {
            setItemPositionInterface(this@BaseAdapter)
        }
    }

    open fun removeListeners() {
        listener = null
    }

    open fun notifyAdapterRemoved() {}

    protected abstract fun createItemViewModel(obj: M): T

    companion object {
        const val NO_ITEM = -1
    }
}
