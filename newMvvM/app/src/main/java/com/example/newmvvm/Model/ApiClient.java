package com.example.newmvvm.Model;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient
{
    private static Retrofit retrofit;
    private static final String url="http://www.omdbapi.com/";
    public static Retrofit getClient()
    {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(url).build();
        }
        return retrofit;
    }
}
