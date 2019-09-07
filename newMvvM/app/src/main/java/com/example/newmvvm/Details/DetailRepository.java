package com.example.newmvvm.Details;

import com.example.newmvvm.Details.ModelDetail.DetailList;

import java.util.List;

import io.reactivex.Single;

public class DetailRepository implements DetailDataSource{
    ApiDetailDataSource apiDetailDataSource=new ApiDetailDataSource();
    @Override
    public Single<DetailList> getDetail(String api,String key) {
        return apiDetailDataSource.getDetail(api,key);
    }
}
