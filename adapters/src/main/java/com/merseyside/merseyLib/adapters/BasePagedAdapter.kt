package com.merseyside.merseyLib.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.merseyside.merseyLib.view.BaseBindingHolder

abstract class BasePagedAdapter<T: Any>(diffUtil: DiffUtil.ItemCallback<T>)
    : PagedListAdapter<T, BaseBindingHolder<T>>(diffUtil) {

    private var listener: BaseAdapter.OnItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingHolder<T> {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return getViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    override fun onBindViewHolder(holder: BaseBindingHolder<T>, position: Int) {
        val obj = getItem(position)

        holder.bind(getBindingVariable(), obj!!)

        holder.itemView.setOnClickListener{
            listener?.onItemClicked(obj)
        }
    }

    open fun getViewHolder(binding: ViewDataBinding): BaseBindingHolder<T> {
        return BaseBindingHolder(binding)
    }

    fun addOnItemClickListener(listener : BaseAdapter.OnItemClickListener<T>) {
        this.listener = listener
    }

    fun getOnItemClickListener() : BaseAdapter.OnItemClickListener<T>? {
        return listener
    }

    protected abstract fun getLayoutIdForPosition(position: Int): Int

    protected abstract fun getBindingVariable(): Int


}