package com.chris.android_samples.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chris.android_samples.util.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    fun addDisposable(vararg disposables: Disposable) {
        disposables.forEach {
            compositeDisposable.addAll(it)
        }
    }

    protected val _progressVisibility = MutableLiveData<Event<Boolean>>()
    val progressVisibility: LiveData<Event<Boolean>>
        get() = _progressVisibility

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}