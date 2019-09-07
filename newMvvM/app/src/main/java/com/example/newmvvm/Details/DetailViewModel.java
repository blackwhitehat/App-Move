package com.example.newmvvm.Details;

import com.example.newmvvm.Details.ModelDetail.DetailList;

import java.util.List;

import io.reactivex.Single;

public class DetailViewModel {
    DetailRepository repository=new DetailRepository();
    public Single<DetailList> getDetailoList(String api,String key)
    {
        return repository.getDetail(api,key);
    }
}
