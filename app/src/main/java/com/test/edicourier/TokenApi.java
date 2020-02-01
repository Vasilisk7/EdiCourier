package com.test.edicourier;

import com.test.edicourier.model.Token;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface TokenApi {

    @GET("auth")
    Observable<Token> token(@Header("login") String login, @Header("passwd") String password);
}
