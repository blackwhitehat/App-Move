package com.arikehparsi.reyhanh.yedarbast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import Lib.Service;

public class PayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        SharedPreferences Taxi_user_datas = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);
        String token = Taxi_user_datas.getString("token", "null");
        Handler handler;
        handler = new Handler();
        try {
            com.github.nkzawa.socketio.client.Socket socket = IO.socket(Service.baseURL);
            socket.connect();
            socket.emit("get_stock",token);
            socket.on("set_stock", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    final JSONObject data =(JSONObject) args[0];
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                TextView credit = findViewById(R.id.credit);
                                credit.setText(data.getDouble("money")+"ريال");
                            }catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });

        }catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    public void pay_final(View view)
    {
        EditText pay_amount = findViewById(R.id.paytxt);

        if(pay_amount.getText().toString()=="")
        {
            Toast.makeText(this, "طفا مقداری را وارد کنید", Toast.LENGTH_SHORT).show();
        }else if(Integer.valueOf(pay_amount.getText().toString()) <= 1000)
        {
            Toast.makeText(this, "مفدار وارد شده می بایست بیشتر از 1000 ريال باشد", Toast.LENGTH_SHORT).show();
        }else{
            PaymentActivity.amount = pay_amount.getText().toString();
            startActivity(new Intent(PayActivity.this,PaymentActivity.class));
        }


    }
}
