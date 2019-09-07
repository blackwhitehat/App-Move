package com.arikehparsi.reyhanh.yedarbast;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

import Lib.Service;

public class PaymentActivity extends AppCompatActivity {

    public static String amount;
    private WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mWebview = new WebView(this);

        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

        final Activity activity = this;
        String mPhoneNumber ="";
        mWebview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        } else {

            mPhoneNumber=tMgr.getLine1Number();
        }


        //String mPhoneNumber = tMgr.getLine1Number();


        SharedPreferences sp5;
        sp5 = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);
        String token7 = sp5.getString("token", "no");

        mWebview .loadUrl("https://sep.shaparak.ir/MobilePG/MobilePayment");
        WebView webview = new WebView(this);
        setContentView(webview);
        String url = "https://sep.shaparak.ir/MobilePG/MobilePayment";
        String postData = null;
        try {
            postData = "Amount=" +URLEncoder.encode(amount,"UTF-8")+
                    "&MID=" + URLEncoder.encode("11366527", "UTF-8") +
                    "&CellNumber="+ URLEncoder.encode(mPhoneNumber,"UTF-8")+
                    "&ResNum="+ URLEncoder.encode(token7,"UTF-8") +
                    "&MultiSettle="+ URLEncoder.encode("False","UTF-8")+
                    "&RedirectURL="+ URLEncoder.encode("http://185..8.175.151:3000/user/useer_money","UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }







        mWebview.postUrl(url,postData.getBytes());
        setContentView(mWebview );
        mWebview.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {


                Toast.makeText(getApplication(), description,
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);

                if (url.equals("http://demo.sep.ir/result2.php"))
                {
                    try {
                        ConnectionTask task = new ConnectionTask();
                        task.execute(url);
                    }  catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e("onPageFinished url", url);


                }}

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }


    private class ConnectionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL aURL = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();

                BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = streamReader.readLine()) != null) {
                    response.append(inputLine);
                }
                is.close();

                String s = response.toString();

                Log.d("sasas", "response" + s);
                return  s;

            }catch ( Exception ex)
            {

            }


            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            // result is what you got from your connection
            if(result.equals("2")){
                try {
                    SharedPreferences Taxi_user_datas = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);
                    String  token = Taxi_user_datas.getString("token", "null");
                    com.github.nkzawa.socketio.client.Socket socket = IO.socket(Service.baseURL);
                    socket.connect();
                    socket.emit("user_add_amount",token,Integer.parseInt(amount));
                    startActivity(new Intent(PaymentActivity.this,MenuActivity.class));

                }catch (URISyntaxException e)
                {
                  e.printStackTrace();
                }


            }else{
                return;
            }


        }

    }


}