package com.arikehparsi.reyhanh.yedarbast;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Lib.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Offcode_Activity extends AppCompatActivity {
    EditText code_edit;
    Button sub_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offcode_);
        code_edit = findViewById(R.id.eidt_code);
        sub_code = findViewById(R.id.sub_code);
        sub_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code_edit.getText().toString() =="")
                {
                    Toast.makeText(Offcode_Activity.this, "لطفا کد را وارد کنید", Toast.LENGTH_SHORT).show();
                }else
                    {
                        SharedPreferences sp5;
                        sp5 = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);
                        String token7 = sp5.getString("token", "no");
                        String code = code_edit.getText().toString();
                        Retrofit retrofit = new Retrofit.Builder().baseUrl(Service.baseURL).addConverterFactory(GsonConverterFactory.create()).build();
                        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                        Call<ServerData.Off_code> call = apiInterface.off_code(token7,code);
                        Callback<ServerData.Off_code> callback = new Callback<ServerData.Off_code>() {
                            @Override
                            public void onResponse(Call<ServerData.Off_code> call, Response<ServerData.Off_code> response) {
                                if(response.isSuccessful())
                                {
                                    String message = response.body().getMessage();
                                    String status = response.body().getStatus();
                                    if(status == "ok")
                                    {
                                        Toast.makeText(Offcode_Activity.this, message, Toast.LENGTH_SHORT).show();
                                    }else
                                        {
                                            Toast.makeText(Offcode_Activity.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                }
                                else
                                    {
                                        Toast.makeText(Offcode_Activity.this, "خطا در ثبت کد", Toast.LENGTH_SHORT).show();
                                    }
                            }

                            @Override
                            public void onFailure(Call<ServerData.Off_code> call, Throwable t) {
                                Toast.makeText(Offcode_Activity.this, "خطا در ثبت کد", Toast.LENGTH_SHORT).show();
                            }
                        };
                    }
            }
        });

    }
}
