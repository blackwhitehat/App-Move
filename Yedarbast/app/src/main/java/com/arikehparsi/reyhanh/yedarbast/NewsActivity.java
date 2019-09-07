package com.arikehparsi.reyhanh.yedarbast;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.arikehparsi.reyhanh.yedarbast.App.Adapter;
import com.arikehparsi.reyhanh.yedarbast.App.AppController;
import com.arikehparsi.reyhanh.yedarbast.App.Info;
import com.baoyz.widget.PullRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;


public class NewsActivity extends AppCompatActivity {

    final Timer tm = new Timer();
    private RecyclerView recyclerView;
    private Adapter adapter;
    String[] jarr, jdate,jmatn;
    public static String[] jimg;
    private SwipeRefreshLayout swipeContainer;
    int i;
    boolean flg = true;
    public static boolean noti=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        final PullRefreshLayout layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recy);

        Intent intent = new Intent(getApplicationContext(), MyService.class);
        startService(intent);



        ////////////////////////////////////////////////////////////
        final Timer tm=new Timer();

        tm.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        //  getdatainit();
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //  Toast.makeText(Main.this, "bbb", Toast.LENGTH_SHORT).show();

                            }
                        }, 7000);


                    }
                });
            }
        },1,7000);
        //////////////////////////////////////////////////////////
        getdata();

        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdata();
                layout.setRefreshing(false);
            }
        });

        layout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);

    }

    public void broadcastIntent(View view) {
        Intent intent = new Intent();
        intent.setAction("com.app2app.CUSTOM_INTENT");
        intent.putExtra("value",1000);
        sendBroadcast(intent);

    }

    private void getdata()
    {
        String url="http://185.8.175.151/get.php";
        Response.Listener<String> listener=new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                //  Toast.makeText(Main.this, response+"", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("jsons");
                    jarr=new String[jsonArray.length()];
                    jdate=new String[jsonArray.length()];
                    jimg=new String[jsonArray.length()];
                    jmatn=new String[jsonArray.length()];
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject c=jsonArray.getJSONObject(i);
                        jarr[i]=c.getString("title");
                        jdate[i]=c.getString("tarikh");
                        jimg[i]=c.getString("address");
                        jmatn[i]=c.getString("matn");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                init();
            }

        };
        Response.ErrorListener errorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewsActivity.this, error.getMessage()+ "", Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest request=new StringRequest(Request.Method.GET,url,listener,errorListener);
        AppController.getInstance().addToRequestQueue(request);
    }

    public void getdatainit()
    {
        String url="http://192.168.56.1/get.php";
        Response.Listener<String> listener=new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {



            }

        };
        Response.ErrorListener errorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewsActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest request=new StringRequest(Request.Method.GET,url,listener,errorListener);
        AppController.getInstance().addToRequestQueue(request);
    }

    private void init()
    {
        adapter=new Adapter(this,data());

        recyclerView.setHasFixedSize(true);


        AlphaInAnimationAdapter anim=new AlphaInAnimationAdapter(adapter);



        /*
        anim.setInterpolator(new FastOutLinearInInterpolator());
        anim.setDuration(1000);
        anim.setFirstOnly(false);
        */

        ScaleInAnimationAdapter left=new ScaleInAnimationAdapter(anim);
        left.setDuration(1000);
        left.setFirstOnly(false);

        recyclerView.setAdapter(anim);


        recyclerView.setHasFixedSize(true);



        recyclerView.setAdapter(left);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        // recyclerView.setLayoutManager(new LinearLayoutManager(Main.this, LinearLayoutManager.VERTICAL, false));
        //startActivity(new Intent(Main.this,Json.class));

    }

    private List<Info> data() {
        final List<Info> data=new ArrayList<>();
        String jarrs[]={"aaa","sss","ddd"};
        if (flg) {
            for (i = 0; i < jarr.length; i++) {
                Info cur = new Info();
                cur.infname = jarr[i];
                cur.infdate = jdate[i];
                cur.infimg  = jimg[i];
                cur.infmatn=jmatn[i];
                data.add(cur);
            }
            flg=true;
        }
        return data;
    }










}