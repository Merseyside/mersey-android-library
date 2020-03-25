package com.merseyside.merseyLib.domain.interactor.rxjava

import io.reactivex.observers.DisposableObserver

open class DefaultObserver<T> : DisposableObserver<T>() {

    override fun onComplete() {
        // no-op by default.
    }

    override fun onNext(t: T) {
        // no-op by default.
    }

    override fun onError(e: Throwable) {
        // no-op by default.
    }
}