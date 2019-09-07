package com.example.newmvvm.Model;

import com.example.newmvvm.Details.ModelDetail.DetailList;
import com.example.newmvvm.Move.Pro.Link;

import com.example.newmvvm.Move.Pro.Search;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("?apikey=3e974fca&s=batman")
    Single<Link> getList();

    @GET("http://www.omdbapi.com/")
    Single<DetailList> getDet(@Query("apikey") String api,@Query("i") String key);
}
