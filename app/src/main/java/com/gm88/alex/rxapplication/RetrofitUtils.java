package com.gm88.alex.rxapplication;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by esymeptoo on 2018/3/27.
 */


public class RetrofitUtils {

    private static final String ENDPOINT = "https://www.baidu.com";

    public static Retrofit instance(){

        OkHttpClient.Builder builder =  new OkHttpClient().newBuilder();
        builder.readTimeout(10 , TimeUnit.SECONDS);
        builder.connectTimeout(9 , TimeUnit.SECONDS);

        if (BuildConfig.DEBUG){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        return new Retrofit.Builder().baseUrl(ENDPOINT)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }
}
