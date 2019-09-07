package com.example.newmvvm.Move;

import com.example.newmvvm.Model.ApiProvider;
import com.example.newmvvm.Move.Pro.Link;

import com.example.newmvvm.Move.Pro.Search;

import java.util.List;

import io.reactivex.Single;

public class ApiMainDataSource implements MainDataSource{

    @Override
    public Single<Link> getList() {
        return ApiProvider.apiProvider().getList();
    }
}
