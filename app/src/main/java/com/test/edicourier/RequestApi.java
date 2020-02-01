package com.test.edicourier;

import com.test.edicourier.model.Application;
import com.test.edicourier.model.RequestUid;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestApi {

    @GET("requests")
    Observable<List<Application>> applicationListObservable(@Header("token") String token, @Query("date-from") String dateFrom, @Query("date-to") String dateTo);

    @GET("request/{uid}")
    Observable<RequestUid> requestUidObservable(@Header("token") String token, @Path("uid") String uid);
}
