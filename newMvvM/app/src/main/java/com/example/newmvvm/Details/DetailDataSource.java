package com.example.newmvvm.Details;

import com.example.newmvvm.Details.ModelDetail.DetailList;

import java.util.List;

import io.reactivex.Single;

public interface DetailDataSource {
    Single<DetailList> getDetail(String api,String key);
}
