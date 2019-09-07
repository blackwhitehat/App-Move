package com.arikehparsi.reyhanh.yedarbast;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.ParseException;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.arikehparsi.reyhanh.yedarbast.Game.Game_Activity_Main;
import com.arikehparsi.reyhanh.yedarbast.R;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.nightonke.boommenu.Animation.EaseEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Lib.Service;
import dyanamitechetan.vusikview.VusikView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuActivity extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener
        ,MediaPlayer.OnCompletionListener
        ,MediaPlayer.OnPreparedListener
        ,MediaPlayer.OnErrorListener{

    private ImageView btn_play_pause;
    private ListView listView;
    private ImageView listBtn;
    private VusikView musicView;

    ArrayList<String> musicNames = new ArrayList<>();
    public ArrayList<String> musicUrls = new ArrayList<>();
    Handler handler2;
    private ImageView refreshbtn;
    private ImageView nextBtn ;
    private ImageView previousBtn ;
    private MediaPlayer mediaPlayer;
    private ArrayAdapter musicAdapter;
    public static boolean intrip= true;
    final Handler handler = new Handler();
    private int CurrentMusic = 0 ;
    private int pic[]={R.drawable.ic_game,R.drawable.ic_list, R.drawable.ic_profile, R.drawable.ic_support, R.drawable.ic_about, R.drawable.ic_notice, R.drawable.ic_share, R.drawable.instagram, R.drawable.ic_setting};
    private BoomMenuButton bmb;
    private String lst[] = {"بازی", "لیست سفر ها", "صفحه کاربری", "پشتیبانی", "درباره یه دربست", "اعلانات", "معرفی به دوستان", "اینستاگرام", "تنظیمات"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);



        //NUadd();
        Socket socket ;
        try
        {
            SharedPreferences sp5;
            sp5 = getSharedPreferences("Taxi_user_datas",MODE_PRIVATE);
            String token7 = sp5.getString("token","no");
            socket = IO.socket(Service.baseURL);
            socket.connect();
            socket.emit("User_Connected",token7);
        }catch (URISyntaxException e)
        {
            Toast.makeText(this, "couldnt connect", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        VideoView videoview = (VideoView) findViewById(R.id.video);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/" + R.raw.video);
        videoview.setVideoURI(uri);
        handler2 = new Handler();
        videoview.setOnPreparedListener(PreparedListener);
        videoview.start();
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.music);
        mediaPlayer.start();
        mediaPlayer = new MediaPlayer();
        //refreshbtn = findViewById(R.id.songs_refresh);
        musicView = findViewById(R.id.musicView);
        listView = findViewById(R.id.list) ;
        listView.bringToFront();
        listView.setVisibility(View.INVISIBLE);
        nextBtn = findViewById(R.id.btn_next_songe);
        previousBtn = findViewById(R.id.btn_previous_songe);
        listBtn = findViewById(R.id.songs_list);
        btn_play_pause = findViewById(R.id.btn_play_pause);


        bmb = findViewById(R.id.bmb1);

        bmb.setShowMoveEaseEnum(EaseEnum.EaseOutBack);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(pic[i])
                    .shadowEffect(true)

                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {

                            switch (index) {
                                case 0:
                                    startActivity(new Intent(getApplicationContext(), Game_Activity_Main.class));
                                    break;
                                case 1:
                                    startActivity(new Intent(getApplicationContext(), ServiceListActivity.class));
                                    break;
                                case 2:
                                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                    break;
                                case 3:
                                    startActivity(new Intent(getApplicationContext(),SupportActivity.class));
                                    break;
                                case 4:
                                    startActivity(new Intent(getApplicationContext(),AboutActivity.class));
                                    break;
                                case 5:
                                    startActivity(new Intent(getApplicationContext(),NewsActivity.class));
                                    break;
                                case 6:
                                    share_link();
                                    break;
                                case 7:
                                    instagram();
                                    break;
                                case 8:
                                    startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                                    break;
                            }
                        }
                    })
                    .normalText(lst[i]);

            bmb.addBuilder(builder);
        }


        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {

            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.

                        }
                    });
        }



        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentMusic==0){
                    if(musicUrls.size()>1){
                        CurrentMusic++;
                    }
                }else if((CurrentMusic+1)==musicUrls.size()){
                    CurrentMusic=0;
                }else{
                    CurrentMusic++;
                }
                resting();
                try {
                    mediaPlayer.setDataSource(MenuActivity.this,Uri.parse(musicUrls.get(CurrentMusic)));
                }catch (IOException e){
                    Toast.makeText(MenuActivity.this,"Failed",Toast.LENGTH_LONG).show();
                }
                mediaPlayer.prepareAsync();

            }
        });
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrentMusic == 0){
                    try{
                        resting();
                        mediaPlayer.setDataSource(MenuActivity.this,Uri.parse(musicUrls.get(musicNames.size())));
                        CurrentMusic = musicNames.size() ;
                    }catch (IOException e){
                        Toast.makeText(MenuActivity.this,"Failed",Toast.LENGTH_LONG).show();
                    }catch (Exception ee){
                        Toast.makeText(MenuActivity.this,"Failed",Toast.LENGTH_LONG).show();
                    }
                }
                else if(CurrentMusic > 0){

                    try{
                        resting();
                        CurrentMusic -- ;
                        mediaPlayer.setDataSource(MenuActivity.this,Uri.parse(musicUrls.get(CurrentMusic)));
                    }catch (IOException e){
                        Toast.makeText(MenuActivity.this,"Failed",Toast.LENGTH_LONG).show();
                    }

                }
                mediaPlayer.prepareAsync();
            }
        });



        musicAdapter = new ArrayAdapter(this, R.layout.list_item ,musicNames);
        listView.setAdapter(musicAdapter);


        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listView.getVisibility()== View.VISIBLE){

                    listView.setVisibility(View.INVISIBLE);
                }
                else if(listView.getVisibility()== View.INVISIBLE){
                    listView.setVisibility(View.VISIBLE);
                    refreshing();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {

                    resting();
                    CurrentMusic = i ;
                    mediaPlayer.setDataSource(MenuActivity.this, Uri.parse(musicUrls.get(i)));
                }catch (IOException e){
                    Toast.makeText(MenuActivity.this,"Internet Error Occured",Toast.LENGTH_LONG);
                }

                mediaPlayer.prepareAsync();
            }
        });


        btn_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayer.isPlaying() && mediaPlayer != null)
                {

                    mediaPlayer.start();
                    btn_play_pause.setImageResource(R.drawable.pause);
                    musicView.start();
                }
                else if(mediaPlayer != null)
                {
                    musicView.stopNotesFall();
                    mediaPlayer.pause();
                    btn_play_pause.setImageResource(R.drawable.play);
                }
            }
        });




    }


    public  void check_order()
    {
        handler2.post(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp1;
                sp1 = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);
                String token5 = sp1.getString("token", "no");
                Retrofit retrofit = new Retrofit.Builder().baseUrl(Service.baseURL).addConverterFactory(GsonConverterFactory.create()).build();
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                Call<ServerData.running_service> call = apiInterface.running_service(token5);
                Callback<ServerData.running_service> callback = new Callback<ServerData.running_service>() {
                    @Override
                    public void onResponse(Call<ServerData.running_service> call, Response<ServerData.running_service> response) {
                        if(response.isSuccessful())
                        {
                            //Toast.makeText(MenuActivity.this, "dfklvgjbefkjl", Toast.LENGTH_SHORT).show();
                                String status = response.body().getStatus();
                                if(status.equals("2"))
                                {
                                    String driverName = response.body().getName_driver();
                                    String car_type = response.body().getCar_type();
                                    String price = response.body().getPrice();
                                    String city_code = response.body().getCity_code();
                                    String service_id = response.body().getService_id();
                                    String city_number= response.body().getCity_number();
                                    String drivermobile = response.body().getDriver_mobile();
                                    int epay = response.body().getEpay();
                                    int service_status = response.body().getService_status();

                                    String photo_url = response.body().getPhoto_url();

                                    String code_number_plates = response.body().getCode_number_plates();
                                    Intent j  = new Intent(getApplicationContext(),CedarMapActivity.class);
                                    j.putExtra("driverName",driverName);
                                    j.putExtra("car_type",car_type);
                                    if(epay==1)
                                    {
                                        j.putExtra("epay",true);
                                    }else if(epay== 0)
                                    {
                                        j.putExtra("epay",false);
                                    }
                                    j.putExtra("price",price);
                                    j.putExtra("city_code",city_code);
                                    j.putExtra("number_plates",code_number_plates);
                                    j.putExtra("service_id",service_id);
                                    j.putExtra("photo_url",photo_url);
                                    j.putExtra("city_number",city_number);
                                    j.putExtra("driver_mobile",drivermobile);
                                    j.putExtra("service_status",service_status);
                                    j.putExtra("intrip",true);
                                    startActivity(j);
                                }



                            }




                    }

                    @Override
                    public void onFailure(Call<ServerData.running_service> call, Throwable t) {
                        //Toast.makeText(MenuActivity.this, "KOrre khar2", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),CedarMapActivity.class);
                        stopService(new Intent(getApplicationContext(),IService.class));
                        intent.putExtra("intrip",false);
                        startActivity(intent);

                    }
                };
                call.enqueue(callback);
            }
        });
    }
    private void refreshing(){
        musicNames.clear();
        musicUrls.clear();
        new JSONAsyncTask().execute("http://185.8.175.151:8000/json");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        btn_play_pause.setImageResource(R.drawable.pause);
        musicView.stopNotesFall();

    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        mediaPlayer.start();
        int[]  myImageList = new int[]{R.drawable.note1,R.drawable.note2};
        musicView.setImages(myImageList).start();
        btn_play_pause.setImageResource(R.drawable.play);

    }


    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Toast.makeText(MenuActivity.this,"Error Occured",Toast.LENGTH_LONG).show();
        mediaPlayer.reset();
        return false;
    }
    private void resting(){
        mediaPlayer.release();
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(this);

        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//for displaying progress bar
            dialog = new ProgressDialog(MenuActivity.this);

            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
//establishing http connection
                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                // if connected then access data
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);


                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray("songs");

                    for (int i= 0 ; i<jarray.length();i++){
                        JSONObject object =jarray.getJSONObject(i);
                        musicNames.add(object.getString("nameofsong"));
                        musicUrls.add(object.getString("urlofsong"));
                    }
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            // if data dint fetch from the url
            dialog.cancel();
            musicAdapter.notifyDataSetChanged();
            if (result == false)
                Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_LONG).show();
        }
    }

    public void trip (View view){

        check_order();
    }


    MediaPlayer.OnPreparedListener PreparedListener = new MediaPlayer.OnPreparedListener(){

        @Override
        public void onPrepared(MediaPlayer m) {
            try {
                if (m.isPlaying()) {
                    m.stop();
                    m.release();
                    m = new MediaPlayer();
                }
                m.setVolume(0f, 0f);
                m.setLooping(true);
                m.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }


    public void goToUrl (View view) {
        String url = "https://logo.saramad.ir/verify.aspx?CodeShamad=1-2-748067-63-0-1";
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


    public void share_link () {
//        String url = "http://yedarbast.ir/";
//        Uri uriUrl = Uri.parse(url);
//        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
//        startActivity(launchBrowser);

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "معرفی یه دربست به دوستان");
        share.putExtra(Intent.EXTRA_TEXT, "http://yedarbast.ir/");

        startActivity(Intent.createChooser(share, "Share link!"));
    }


    public void instagram () {
        String url = "https://www.instagram.com/?hl=en";
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}



