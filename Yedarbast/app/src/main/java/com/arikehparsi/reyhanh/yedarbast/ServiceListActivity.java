package com.arikehparsi.reyhanh.yedarbast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Lib.Service;
import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ServiceListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iranSansWeb.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_service_list);


        get_data();


    }
    public void get_data() {
        final RelativeLayout progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        progress_layout.setVisibility(View.VISIBLE);
        final LinearLayout box_data = (LinearLayout) findViewById(R.id.box_data);
        SharedPreferences Taxi_user_datas = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);
        String token = Taxi_user_datas.getString("token", "null");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Service.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        final Call<List<ServerData.get_service>> call = apiInterface.get_service(token);

        Callback<List<ServerData.get_service>> callback = new Callback<List<ServerData.get_service>>() {
            @Override
            public void onResponse(Call<List<ServerData.get_service>> call, Response<List<ServerData.get_service>> response) {
                if (response.isSuccessful()) {
                    progress_layout.setVisibility(View.GONE);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 0, 0, 10);

                    for (ServerData.get_service get_service : response.body()) {
                        View view = LayoutInflater.from(ServiceListActivity.this).inflate(R.layout.service_row, null);
                        view.setLayoutParams(layoutParams);

                        TextView date = (TextView) view.findViewById(R.id.date);
                        date.setText(Service.get_number(get_service.getDate()));

                        TextView address1 = (TextView) view.findViewById(R.id.address1);
                        address1.setText("مبدا : " + get_service.getAddress1());

                        TextView address2 = (TextView) view.findViewById(R.id.address2);
                        TextView address3 = (TextView) view.findViewById(R.id.address3);

                        if (!String.valueOf(get_service.getAddress3()).equals("null")) {
                            address3.setText("مقصد دوم : " + get_service.getAddress3());
                            address2.setText("مقصد اول : " + get_service.getAddress2());
                        } else {
                            address2.setText("مقصد : " + get_service.getAddress2());
                        }

                        TextView status = (TextView) view.findViewById(R.id.status);
                        if (get_service.getDriving_status() == 2) {
                            status.setText("اتمام سفر");
                        } else {
                            status.setText(Service.get_service_status(get_service.getStatus()));
                        }

                        TextView order_id = (TextView) view.findViewById(R.id.order_id);
                        order_id.setText("شماره درخواست : " + get_service.getOrder_id());

//                        TextView price_show = findViewById(R.id.price_show);
//                        price_show.setText( "هزینه سفر: " + get_service.getPrice());


                        RelativeLayout other_data = (RelativeLayout) view.findViewById(R.id.other_data);
                        /*other_data.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent j = new Intent(ServiceListActivity.this, ServiceActivity.class);
                                j.putExtra("order_id", get_service.getOrder_id());
                                j.putExtra("address1", get_service.getAddress1());
                                j.putExtra("address2", get_service.getAddress2());
                                j.putExtra("address3", String.valueOf(get_service.getAddress3()));
                                if(get_service.status==-10|| get_service.status== -1)
                                {
                                    j.putExtra("driver_name", "No Driver Found");
                                    j.putExtra("driver_mobile", "No Driver Found");
                                    j.putExtra("car_type", "NO Driver Found");

                                }else
                                    {
                                    j.putExtra("driver_name", get_service.getDriverGata().getDriver_name());
                                    j.putExtra("driver_mobile", get_service.getDriverGata().getDriver_mobile());
                                        j.putExtra("car_type", get_service.getDriverGata().getCar_type());
                                }


                                j.putExtra("going_back", get_service.getGoing_back());
                                j.putExtra("price", get_service.getPrice());
                                j.putExtra("total_price", get_service.getTotal_price());
                                j.putExtra("stop_time", get_service.getStop_time());
                                j.putExtra("date", get_service.getDate());
                                startActivity(j);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                            }
                        });*/

                        box_data.addView(view);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<ServerData.get_service>> call, Throwable t) {
                progress_layout.setVisibility(View.GONE);
            }

        };
        call.enqueue(callback);
    }





    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



}

