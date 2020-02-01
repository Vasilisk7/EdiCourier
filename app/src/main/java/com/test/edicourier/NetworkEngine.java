package com.test.edicourier;

import com.test.edicourier.model.Application;
import com.test.edicourier.model.RequestUid;
import com.test.edicourier.model.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkEngine {

    private final static String TAG = "SUPERLOG_NetworkEngine";

    private static final NetworkEngine ourInstance = new NetworkEngine();
    private static final Token TOKEN_ERROR = new Token();
    private static final List<Application> APPLICATIONS_ERROR_LIST = new ArrayList<>(0);
    private static final RequestUid REQUEST_UID_ERROR = new RequestUid();

    public static NetworkEngine getInstance() {
        return ourInstance;
    }

    private NetworkEngine() {
    }

    public void loadToken(String login, String password, final TypedNetworkCallback<Token> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        TokenApi tokenApi = retrofit.create(TokenApi.class);

        Observable<Token> tokenObservable = tokenApi.token(login, password);
        tokenObservable.subscribeOn(Schedulers.io()) // планировщик
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(30, TimeUnit.SECONDS)
                .onErrorReturn(throwable -> {
                    if (throwable instanceof TimeoutException) {
                        callback.onError("Превышено время ожидания ответа");
                    } else {
                        callback.onError("Ошибка авторизации");
                    }
                    return TOKEN_ERROR; // в случае ошибки возвращается пустой Token
                })
                .subscribe(token -> {
                    if (!token.equals(TOKEN_ERROR)) {
                        callback.onLoad(token);
                    }
                });
    }

    public void loadApplicationList(String token, String dateFrom, String dateTo, final TypedNetworkCallback<List<Application>> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        RequestApi requestApi = retrofit.create(RequestApi.class);

        Observable<List<Application>> applicationObservable = requestApi.applicationListObservable(token, dateFrom, dateTo);
        applicationObservable.subscribeOn(Schedulers.io()) // планировщик
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(30, TimeUnit.SECONDS)
                .onErrorReturn(throwable -> {
                    if (throwable instanceof TimeoutException) {
                        callback.onError("Превышено время ожидания ответа");
                    } else {
                        callback.onError("Ошибка получения списка заявок");
                    }
                    return APPLICATIONS_ERROR_LIST; // в случае ошибки возвращается пустой лист
                })
                .subscribe(applications -> {
                    if (!applications.equals(APPLICATIONS_ERROR_LIST)) {
                        callback.onLoad(applications);
                    }
                });
    }

    public void loadRequestUid(String token, String uid, final TypedNetworkCallback<RequestUid> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        RequestApi requestApi = retrofit.create(RequestApi.class);

        Observable<RequestUid> requestUidObservable = requestApi.requestUidObservable(token, uid);
        requestUidObservable.subscribeOn(Schedulers.io()) // планировщик
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(30, TimeUnit.SECONDS)
                .onErrorReturn(throwable -> {
                    if (throwable instanceof TimeoutException) {
                        callback.onError("Превышено время ожидания ответа");
                    } else {
                        callback.onError("Ошибка получения полной версии заявки");
                    }
                    return REQUEST_UID_ERROR; // в случае ошибки возвращается пустой RequestUid
                })
                .subscribe(requestUid -> {
                    if (!requestUid.equals(REQUEST_UID_ERROR)) {
                        callback.onLoad(requestUid);
                    }
                });
    }
}
