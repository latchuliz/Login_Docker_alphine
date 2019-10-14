package com.neopharma.datavault.model.request;

import androidx.lifecycle.MutableLiveData;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class RequestObserver<T> implements Observer<T> {
    private MutableLiveData<T> result;

    public RequestObserver(MutableLiveData<T> result) {
        this.result = result;
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(T value) {
        updateDB(value);
        result.postValue(value);
    }

    @Override
    public void onError(Throwable e) {
        result.postValue(null);
    }

    @Override
    public void onComplete() {

    }

    public abstract void updateDB(T value);
}
