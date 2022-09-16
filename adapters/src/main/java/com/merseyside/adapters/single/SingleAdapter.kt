package com.merseyside.adapters.single

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@InternalAdaptersApi
abstract class SingleAdapter<Item, Model>(scope: CoroutineScope): BaseAdapter<Item, Model>(scope)
    where Model : AdapterViewModel<Item> {

    protected abstract fun getLayoutIdForPosition(position: Int): Int
    protected abstract fun getBindingVariable(): Int
    protected abstract fun createItemViewModel(item: Item): Model

    override fun bindModel(holder: TypedBindingHolder<Model>, model: Model, position: Int) {
        bind(holder, model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypedBindingHolder<Model> {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return getBindingHolder(binding)
    }

    open fun getBindingHolder(binding: ViewDataBinding): TypedBindingHolder<Model> {
        return TypedBindingHolder(binding)
    }

    @CallSuper
    internal open fun bind(holder: TypedBindingHolder<Model>, model: Model) {
        holder.bind(getBindingVariable(), model)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    override fun createModel(item: Item): Model = createItemViewModel(item)

    @CallSuper
    override fun onViewRecycled(holder: TypedBindingHolder<Model>) {
        super.onViewRecycled(holder)
        if (holder.absoluteAdapterPosition != RecyclerView.NO_POSITION &&
            holder.absoluteAdapterPosition < itemCount) {

            getModelByPosition(holder.absoluteAdapterPosition).apply {
                bindItemList.remove(this)
                onRecycled()
            }
        }
    }

}