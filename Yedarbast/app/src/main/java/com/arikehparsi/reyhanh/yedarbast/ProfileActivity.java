package com.arikehparsi.reyhanh.yedarbast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import Lib.Service;

public class ProfileActivity extends AppCompatActivity {
    Socket socket;
    private String token;
    private Handler handler;
    Handler handler1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences Taxi_user_datas = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);
        token = Taxi_user_datas.getString("token", "null");

        try {
            socket = IO.socket(Service.baseURL);
            handler = new Handler();
            handler1 = new Handler();
            socket.connect();
            socket.emit("req_profile",token);
            socket.on("send_profile",pro_handle);
            socket.emit("get_stock",token);
            socket.on("set_stock", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject)args[0];
                    handler1.post(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                TextView credit = findViewById(R.id.your_credit);
                                if(data.getDouble("money")== 0 )
                                {
                                    credit.setText("فاقد اعتبار");
                                }else
                                {
                                    credit.setText("اعتبار شما:"+data.getDouble("money")+"ريال");
                                }
                            }catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    });

                }
            });
        }catch (URISyntaxException e){
            Toast.makeText(this, "Couldn't Load Profile", Toast.LENGTH_SHORT).show();
        }


    }
    Emitter.Listener pro_handle= new Emitter.Listener() {

        @Override

        public void call(Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    final JSONObject data=(JSONObject)args[0];
                    try{
                        String pname =data.getString("name");
                        String pphone = data.getString("mobile");
                        String gender = data.getString("gender");
                        TextView username = findViewById(R.id.PNAME);
                        TextView userPhone = findViewById(R.id.Pphone);
                        TextView userGender = findViewById(R.id.Pgender);
                        username.setText(pname);
                        userPhone.setText(pphone);
                        userGender.setText(gender);
                        Toast.makeText(ProfileActivity.this, pname, Toast.LENGTH_SHORT).show();

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });


        }
    };


    public void pay(View view){

        Intent intent = new Intent(ProfileActivity.this, PayActivity.class);
        startActivity(intent);

    }
}
