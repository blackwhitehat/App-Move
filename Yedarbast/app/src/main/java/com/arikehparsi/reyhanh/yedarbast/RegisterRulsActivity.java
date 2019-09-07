package com.arikehparsi.reyhanh.yedarbast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class RegisterRulsActivity extends AppCompatActivity {

    private CheckBox checkBox;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ruls);


        checkBox = findViewById(R.id.Rulls);
        sp = getSharedPreferences("rulls" , 0);
        if(checkBox.isChecked())
        {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("ok","1");
            editor.commit();

        }
        else
        {
            Toast.makeText(this, "لطفا قوانین و شرایط را تائیید کنید", Toast.LENGTH_SHORT).show();
        }
    }

    public void rullcheck(View view) {

        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
    }
}
