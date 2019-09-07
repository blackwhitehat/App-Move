package com.example.newmvvm.Move;

import android.content.Context;
import android.content.SharedPreferences;

public class Save_Info {
    private Context context;
    private SharedPreferences preferences;
    public Save_Info(Context context) {
        this.context=context;
        preferences=context.getSharedPreferences("Login",Context.MODE_PRIVATE);
    }
    public void SplashSave(String user)
    {
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("user",user);
        editor.apply();
    }

    public String GetInfo()
    {

        return preferences.getString("user","null");
    }


}
