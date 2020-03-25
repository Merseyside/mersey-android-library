package com.merseyside.merseyLib.domain.interactor.rxjava

import com.merseyside.merseyLib.domain.executor.PostExecutionThread
import com.merseyside.merseyLib.domain.executor.ThreadExecutor
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

abstract class SingleUseCase<T, Params> protected constructor(
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread
) {
    private val disposables: CompositeDisposable = CompositeDisposable()

    protected abstract fun buildUseCaseSingle(params : Params?) : Single<T>

    fun execute(observer: DisposableSingleObserver<T>, params: Params? = null) {

        val single = this.buildUseCaseSingle(params)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.scheduler)

        addDisposable(single.subscribeWith(observer))
    }

    private fun addDisposable(disposable: DisposableSingleObserver<T>?) {
        if (disposable != null) {
            disposables.add(disposable)
        }
    }

    fun dispose() {
        disposables.dispose()
    }

    fun clear() {
        disposables.clear()
    }
}