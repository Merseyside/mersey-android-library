package com.merseyside.adapters.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.adapters.holder.BaseBindingHolder
import com.merseyside.merseyLib.kotlin.logger.Logger

abstract class PagedAdapter<M: Any, T : AdapterViewModel<M>>(diffUtil: DiffUtil.ItemCallback<M>)
    : PagedListAdapter<M, BaseBindingHolder>(diffUtil),
    HasOnItemClickListener<M> {

    override var listener: OnItemClickListener<M>? = null

    enum class NetworkState { ERROR, NO_CONNECTION, CONNECTED, LOADING }
    private var networkState: INetworkState? = null

    interface INetworkState {

        interface OnRetryListener {
            fun onRetry()
        }

        fun setMessage(msg: String)
        fun onStateChanged(state: NetworkState)
        fun getNetworkState(): NetworkState
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return getViewHolder(binding)
    }

    protected abstract fun createItemViewModel(obj: M): T

    protected abstract fun getLayoutIdForPosition(position: Int): Int

    protected abstract fun getBindingVariable(): Int

    protected open fun getNetworkConnectionModel(): INetworkState {
        throw IllegalArgumentException("You have to override this method" +
                "in order to use network states")
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            getNetworkConnectionLayout()
        } else {
            getLayoutIdForPosition(position)
        }
    }

    override fun onBindViewHolder(holder: BaseBindingHolder, position: Int) {

        if (hasExtraRow() && position == itemCount - 1) {
            holder.bind(getBindingVariable(), networkState!!)
        } else {
            val obj = getItem(position)

            if (obj != null) {
                holder.bind(getBindingVariable(), createItemViewModel(obj))

                holder.itemView.setOnClickListener {
                    listener?.onItemClicked(obj)
                }
            }
        }

    }

    open fun getViewHolder(binding: ViewDataBinding): BaseBindingHolder {
        return BaseBindingHolder(binding)
    }

    private fun hasExtraRow() = networkState != null && networkState!!.getNetworkState() != NetworkState.CONNECTED

    @LayoutRes
    open fun getNetworkConnectionLayout(): Int {
        return 0
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState, msg: String? = null) {
        Logger.log(this, "set network state $newNetworkState")
        if (networkState == null) {
            networkState = getNetworkConnectionModel()
        }

        val previousState = this.networkState!!.getNetworkState()
        val hadExtraRow = hasExtraRow()

        setupNetworkItem(newNetworkState, msg)

        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun setupNetworkItem(networkState: NetworkState, msg: String? = null) {
        this.networkState?.apply {
            onStateChanged(networkState)
            if (msg != null) setMessage(msg)
        }
    }
}