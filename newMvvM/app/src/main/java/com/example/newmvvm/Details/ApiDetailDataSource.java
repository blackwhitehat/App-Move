package com.example.newmvvm.Details;

import com.example.newmvvm.Details.ModelDetail.DetailList;
import com.example.newmvvm.Model.ApiProvider;

import java.util.List;

import io.reactivex.Single;

public class ApiDetailDataSource implements DetailDataSource{
    @Override
    public Single<DetailList> getDetail(String api,String key) {
        return ApiProvider.apiProvider().getDet(api,key);
    }
}
