package com.arikehparsi.reyhanh.yedarbast;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Lib.Service;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SensorManager sensorManager;
    private Sensor all;

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    EditText name;
    EditText mobile;
    EditText password;
    Spinner spinner;
    String[] gen={"مرد","زن"};
    String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        spinner = findViewById(R.id.sex);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        all = sensorManager.getDefaultSensor(Sensor.TYPE_ALL);



        List<String> categories = new ArrayList<>();
        categories.add("مرد");
        categories.add("زن");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.gender_list, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.gender_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        item = parent.getItemAtPosition(position).toString();
//        Toast.makeText(this, item, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void add_user(View view) {

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);


        //verify
        if (validate()) {

//            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                    .connectTimeout(1, TimeUnit.MINUTES)
//                    .writeTimeout(1, TimeUnit.MINUTES)
//                    .readTimeout(1, TimeUnit.MINUTES)
//                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Service.baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();




            //interface
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            Call<ServerData.register> call = apiInterface.register(name.getText().toString(),
                    mobile.getText().toString(),
                    password.getText().toString(),
                    item.toString());

            Callback<ServerData.register> callback = new Callback<ServerData.register>() {
                @Override
                public void onResponse(Call<ServerData.register> call, Response<ServerData.register> response) {
                    if(response.isSuccessful()){

                        if(response.body().getStatus().equals("no")){

                            Toast.makeText(RegisterActivity.this, "خطا در ثبت نام، لطفا مجددا تلاش نمایید.", Toast.LENGTH_LONG).show();

                        }else if(response.body().getStatus().equals("ok")) {

                            String token = response.body().getToken();
                            Intent intent = new Intent(RegisterActivity.this, ActiveActivity.class);
                            intent.putExtra("token", token);
                            intent.putExtra("mobile", mobile.getText().toString());
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);

                        }

                    }
                }

                @Override
                public void onFailure(Call<ServerData.register> call, Throwable t) {
                    Log.i("response_data", "error");
                    Toast.makeText(RegisterActivity.this, "خطا در ثبت نام", Toast.LENGTH_LONG).show();
                }
            };

            call.enqueue(callback);
        }
    }

    public boolean validate() {

        boolean a = validateName();
        boolean b = validateMobile();
        boolean c = validatePassword();

        if (a && b && c){

            return true;

        }else {

            return false;
        }


    }

    public boolean validateName(){

        boolean a = true;

        if (name.getText().toString().trim().isEmpty()) {

            a = false;
            name.setBackgroundResource(R.drawable.validateborder);


        } else {
            a = true;
            name.setBackgroundResource(R.drawable.editborder);

        }

        return a;
    }

    public boolean validateMobile(){

        boolean a = true;

        if (mobile.getText().toString().trim().isEmpty()) {

            a = false;
            mobile.setBackgroundResource(R.drawable.validateborder);

        } else {

            String numberPattern = "[0-9]+";
            if(mobile.getText().toString().matches(numberPattern)){

                if(mobile.getText().toString().length() == 11){

                    if (mobile.getText().toString().substring(0, 2).equals("09")){

                        a = true;
                        mobile.setBackgroundResource(R.drawable.editborder);

                    }else {

                        a = false;
                        Toast.makeText(this, "شماره همراه وارد شده معتبر نمی باشد.", Toast.LENGTH_LONG).show();
                        mobile.setBackgroundResource(R.drawable.validateborder);

                    }


                }else if (mobile.getText().toString().length() == 10){

                    if (mobile.getText().toString().substring(0, 1).equals("9")){

                        a = true;
                        mobile.setBackgroundResource(R.drawable.editborder);

                    }else {

                        a = false;
                        Toast.makeText(this, "شماره همراه وارد شده معتبر نمی باشد.", Toast.LENGTH_LONG).show();
                        mobile.setBackgroundResource(R.drawable.validateborder);

                    }

                }else {

                    a = false;
                    Toast.makeText(this, "شماره همراه وارد شده معتبر نمی باشد.", Toast.LENGTH_LONG).show();
                    mobile.setBackgroundResource(R.drawable.validateborder);

                }

                a = true;
                mobile.setBackgroundResource(R.drawable.editborder);

            }else {

                a = false;
                Toast.makeText(this, "شماره همراه وارد شده معتبر نمی باشد.", Toast.LENGTH_LONG).show();
                mobile.setBackgroundResource(R.drawable.validateborder);

            }

        }

        return a;
    }

    public boolean validatePassword() {

        boolean a = true;

        if (password.getText().toString().trim().isEmpty()) {

            a = false;
            password.setBackgroundResource(R.drawable.validateborder);
        } else {

            if (password.getText().toString().length() < 6) {

                a = false;
                password.setBackgroundResource(R.drawable.validateborder);
                Toast.makeText(this, "رمز عبور باید حداقل دارای 6 کاراکتر باشد.", Toast.LENGTH_LONG).show();

            } else {

                a = true;
                password.setBackgroundResource(R.drawable.editborder);

            }


        }

        return a;
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

}
