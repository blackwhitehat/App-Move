package com.example.newmvvm.Move;

import com.example.newmvvm.Move.Pro.Link;

import com.example.newmvvm.Move.Pro.Search;

import java.util.List;

import io.reactivex.Single;

public class MainViewModel
{
    MainRepository repository=new MainRepository();
    public Single<Link> getList()
    {
        return repository.getList();
    }
}
