package com.gm88.alex.rxapplication;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by esymeptoo on 2018/3/27.
 */

public interface RetroInterface {

    @GET
    Observable<LoginBean> login (@Query("id") String id);
    @GET
    Observable<RegisterBean> register (@Query("id") String id);


    @GET
    Call<LoginBean> loginCall (@Query("id") String id);


}
