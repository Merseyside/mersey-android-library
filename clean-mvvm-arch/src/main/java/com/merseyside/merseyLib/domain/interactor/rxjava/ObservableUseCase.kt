package com.merseyside.merseyLib.domain.interactor.rxjava

import com.merseyside.merseyLib.domain.executor.PostExecutionThread
import com.merseyside.merseyLib.domain.executor.ThreadExecutor

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by merseyside on 19.12.17.
 */

abstract class ObservableUseCase<T, Params> protected constructor(
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread
) {

    private val TAG = this.javaClass.simpleName
    private val disposables: CompositeDisposable = CompositeDisposable()

    protected abstract fun buildUseCaseObservable(params: Void?): Observable<T>

    fun execute(observer: DisposableObserver<T>, params: Void?) {

        val observable = this.buildUseCaseObservable(params)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.scheduler)
        addDisposable(observable.subscribeWith(observer))
    }

    fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

    fun clear() {
        disposables.clear()
    }

    private fun addDisposable(disposable: Disposable?) {
        if (disposable != null) {
            disposables.add(disposable)
        }
    }
}
