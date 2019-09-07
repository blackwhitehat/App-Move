package com.arikehparsi.reyhanh.yedarbast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import Lib.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String baseUrl = "http://192.168.1.103:3000";

    SharedPreferences sp;


    String token ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);

      token = sp.getString("token", "no");
        if (!token.equals("no")){
            startActivity(new Intent(getApplicationContext(),MenuActivity.class));

        }else {
            return;
        }


    }


    public void login (View view){

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    public void register(View view){

        Intent intent = new Intent(MainActivity.this, RegisterRulsActivity.class);
        startActivity(intent);
    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
