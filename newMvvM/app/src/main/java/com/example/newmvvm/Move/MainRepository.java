package com.example.newmvvm.Move;

import com.example.newmvvm.Move.Pro.Link;

import com.example.newmvvm.Move.Pro.Search;

import java.util.List;

import io.reactivex.Single;

public class MainRepository implements MainDataSource{
    ApiMainDataSource apiMainDataSource=new ApiMainDataSource();
    @Override
    public Single<Link> getList() {
        return apiMainDataSource.getList();
    }
}
