package com.merseyside.utils.mvvm

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.merseyside.utils.Logger
import java.util.concurrent.atomic.AtomicBoolean


class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val mPending: AtomicBoolean = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Logger.logInfo(
                this,
                "Multiple observers registered but only one will be notified of changes."
            )
        }

        super.observe(owner, Observer { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}