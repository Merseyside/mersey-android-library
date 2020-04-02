package com.merseyside.merseyLib.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.merseyside.merseyLib.view.BaseViewHolder

abstract class BasePagedAdapter<T>(diffUtil: DiffUtil.ItemCallback<T>)
    : PagedListAdapter<T, BaseViewHolder>(diffUtil) {

    private var listener: BaseAdapter.OnItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return BaseViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val obj = getItem(position)

        holder.bind(getBindingVariable(), obj!!)

        holder.itemView.setOnClickListener{
            listener?.onItemClicked(obj)
        }
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