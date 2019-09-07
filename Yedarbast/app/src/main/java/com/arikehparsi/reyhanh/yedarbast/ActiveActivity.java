package com.arikehparsi.reyhanh.yedarbast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

import Lib.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActiveActivity extends AppCompatActivity {

    EditText num1, num2 , num3, num4, num5;

    int[] editText_id = {R.id.num1, R.id.num2, R.id.num3, R.id.num4, R.id.num5};

    int time = 180;

    String baseUrl = "http://192.168.1.103:3000/";

    String active_code = "";
    public String mobileNumber;

    String token;

    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);

        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        num3 = findViewById(R.id.num3);
        num4 = findViewById(R.id.num4);
        num5 = findViewById(R.id.num5);

        for (int i = 0; i<editText_id.length; i++){

            int j = i + 1;
            final EditText number1 = findViewById(editText_id[i]);

            if (editText_id.length - 1 != i){

                final EditText number2 = findViewById(editText_id[j]);

                number1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (!number1.getText().toString().isEmpty()){

                            number2.requestFocus();

                        }


                    }
                });

            }

        }

        final Handler handler = new Handler();

//        Thread thread = new Thread(new Runnable() {
//
//            TextView time_active = findViewById(R.id.time_active);
//            String t = "مدت زمان باقی مانده: ";
//
//            @Override
//            public void run() {
//
//
//                while (time > -1){
//
//                    try {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                if (time == 0){
//
//                                    LinearLayout active_box = findViewById(R.id.active_box);
//                                    active_box.setVisibility(View.GONE);
//                                }else {
//
//                                    String t2 = "";
//                                    double a = time/60;
//                                    int b = (int) a;
//                                    if (b > 0){
//
//                                        int c = time - (b * 60);
//                                        t2 = b + ":" + c;
//                                    }else {
//
//                                        t2 = String.valueOf(time);
//                                    }
//
//                                    time_active.setText(t + t2);
//
//                                }
//
//                            }
//                        });
//
//                        Thread.sleep(1000);
//                    }catch (InterruptedException e){
//
//                        e.printStackTrace();
//                    }
//
//                    time--;
//                }
//
//
//            }
//        });
//
//        thread.stop();


        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("token");
        String mobile = bundle.getString("mobile");
        TextView mobile_number = findViewById(R.id.mobile_number);
        mobile_number.setText(mobile);
        mobileNumber = mobile ;


    }
    public void AgainSendCode(View view)
    {
        Toast.makeText(this, "کد فعالسازی مجددا برای شما ارسال گردید", Toast.LENGTH_SHORT).show();
        try
        {
            Socket socket = IO.socket(Service.baseURL);
            socket.connect();
            socket.emit("send_code_again_user",mobileNumber);

        }catch (URISyntaxException e)
        {
            e.printStackTrace();
        }


    }

    public void send_code (View view){

        boolean send = true;

        active_code = "";

        for (int i = 0; i<editText_id.length; i++){

            final EditText number1 = findViewById(editText_id[i]);

            if (number1.getText().toString().trim().isEmpty()){
                send = false;
            }else {
                active_code = active_code + number1.getText().toString().trim();
            }

        }

        if (send){

            Log.i("token", active_code);


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Service.baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            //interface
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            Call<ServerData.active> call = apiInterface.active_mobile_number(active_code, token);

            Callback<ServerData.active> callback = new Callback<ServerData.active>() {
                @Override
                public void onResponse(Call<ServerData.active> call, Response<ServerData.active> response) {
                    //Log.i("token", response.body());

                    if (response.isSuccessful()){

                        String status = response.body().getStatus();
                        if (status.equals("ok")){

                            String token = response.body().getToken();
                            sp = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("token", token);
                            editor.commit();

                            Intent intent = new Intent(ActiveActivity.this, MenuActivity.class);
                            startActivity(intent);


                        }else if (status.equals("error_code")){

                            Toast.makeText(ActiveActivity.this, "کد فعالسازی اشتباه می باشد.", Toast.LENGTH_LONG).show();

                        }else {

                            Intent intent = new Intent(ActiveActivity.this, RegisterActivity.class);
                            startActivity(intent);
                        }
                    }

                }

                @Override
                public void onFailure(Call<ServerData.active> call, Throwable t) {

                }
            };

            call.enqueue(callback);

        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
