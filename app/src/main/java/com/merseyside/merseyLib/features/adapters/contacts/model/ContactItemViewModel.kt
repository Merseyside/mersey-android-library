package com.merseyside.merseyLib.features.adapters.contacts.model

import com.merseyside.adapters.model.ComparableAdapterViewModel

class ContactItemViewModel(obj: String): ComparableAdapterViewModel<String>(obj) {
    override fun areItemsTheSame(obj: String): Boolean {
        return this.obj == obj
    }

    override fun notifyUpdate() {}

    override fun areContentsTheSame(obj: String): Boolean {
        return areItemsTheSame(obj)
    }

    override fun compareTo(obj: String): Int {
        return this.obj.compareTo(obj)
    }
}