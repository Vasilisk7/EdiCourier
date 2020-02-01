package com.test.edicourier;

public interface TypedNetworkCallback<T> {

    void onLoad(T data);

    void onError(String errorString);
}
