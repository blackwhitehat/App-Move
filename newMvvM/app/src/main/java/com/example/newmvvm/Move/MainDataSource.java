package com.example.newmvvm.Move;

import com.example.newmvvm.Move.Pro.Link;

import com.example.newmvvm.Move.Pro.Search;

import java.util.List;

import io.reactivex.Single;

public interface MainDataSource
{
    Single<Link> getList();
}
