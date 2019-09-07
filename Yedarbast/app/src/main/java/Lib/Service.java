package Lib;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.arikehparsi.reyhanh.yedarbast.ApiInterface;
import com.arikehparsi.reyhanh.yedarbast.CedarMapActivity;
import com.arikehparsi.reyhanh.yedarbast.MapsActivity;
import com.arikehparsi.reyhanh.yedarbast.Place;
import com.arikehparsi.reyhanh.yedarbast.R;
import com.arikehparsi.reyhanh.yedarbast.ServerData;
import com.cedarstudios.cedarmapssdk.CedarMaps;
import com.cedarstudios.cedarmapssdk.listeners.GeoRoutingResultListener;
import com.cedarstudios.cedarmapssdk.listeners.ReverseGeocodeResultListener;
import com.cedarstudios.cedarmapssdk.model.geocoder.reverse.ReverseGeocode;
import com.cedarstudios.cedarmapssdk.model.routing.GeoRouting;
import com.github.nkzawa.socketio.client.Socket;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Service {


    public static String baseURL= "http://185.8.175.151:3000/";
    public static String baseURLM= "http://185.8.175.151:8000/";

    static double directions;
    static int i= 0 ;
    public static String address1, address2, address3;

    static String api_service = "Cedar";
    static Socket socket;

    static int price = 0;

    static int fixed_price = 0;


    public static void set_address(LatLng latLng, int n){

//        if (api_service.equals("Cedar")){
//
//
//
//
//        }else {
//
//        }

        CedarMaps.getInstance().reverseGeocode(
                latLng,
                new ReverseGeocodeResultListener() {
                    @Override
                    public void onSuccess(@NonNull ReverseGeocode result) {

                        String s= result.getCity();

                        if (!result.getDistrict().isEmpty()){

                            s = s + " " + result.getDistrict();
                        }
                        if (!result.getLocality().isEmpty()){

                            s = s + " " + result.getLocality();
                        }
                        if (!result.getPlace().isEmpty()){

                            s = s + " " + result.getPlace();
                        }

                        s = s + " " + result.getAddress();

                        if (n == 1){

                            address1 = s;

                        }else if (n == 2){

                            address2 = s;

                        }else if (n == 3){

                            address3 = s;

                        }

                        Log.i("address", s);

                    }

                    @Override
                    public void onFailure(@NonNull String errorMessage) {

                    }
                });

    }

    public static void get_directions(double lat1, double lng1, double lat2, double lng2, get_callback get_callback){

        if (api_service.equals("google")){

            get_directions_from_google(lat1, lng1, lat2, lng2, get_callback);

//            get_directions_from_google(lat1, lng1, lat2, lng2, new get_callback() {
//                @Override
//                public double onResponse(double directions) {
//                    return directions;
//                }
//
//                @Override
//                public double onFailure(Throwable t) {
//                    return 0;
//                }
//            });



        }else {

            get_directions_from_cedar(lat1, lng1, lat2, lng2, get_callback);

        }
    }

    public static void get_directions_from_google(double lat1, double lng1, double lat2, double lng2, final get_callback get_callback){

        directions = 0;

        String baseUrl = "https://maps.googleapis.com/";

        String origin = lat1 + "," + lng1;
        String destination = lat2 + "," + lng2;


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //interface
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<Place.routes> call = apiInterface.get_directions(origin, destination, "true", "driving");

        Callback<Place.routes> callback = new Callback<Place.routes>() {
            @Override
            public void onResponse(Call<Place.routes> call, Response<Place.routes> response) {

                if (response.isSuccessful()){

                    try{

                        directions = response.body().getRoutes().get(0).getLegs().get(0).getDistance().getValue();
                        get_callback.onResponse(directions);

                    }catch (Exception e){

                        get_callback.onFailure("error");


                    }
                }

            }

            @Override
            public void onFailure(Call<Place.routes> call, Throwable t) {

                get_callback.onFailure("error");

            }
        };


        call.enqueue(callback);

    }


    public static void get_directions_from_cedar(double lat1, double lng1, double lat2, double lng2, get_callback get_callback){

        LatLng departure = new LatLng(lat1, lng1);
        LatLng destination = new LatLng(lat2, lng2);

        CedarMaps.getInstance().direction(departure, destination,
                new GeoRoutingResultListener() {
                    @Override
                    public void onSuccess(@NonNull GeoRouting result) {


                        try{

                            get_callback.onResponse(result.getRoutes().get(0).getDistance().doubleValue());


                        }catch (Exception e){

                            get_callback.onFailure("error");

                        }

                    }

                    @Override
                    public void onFailure(@NonNull String error) {

                        get_callback.onFailure("error");

                    }
                });



    }


    public interface get_callback{

        void onResponse(double directions);
        void onFailure(String error);

    }


    public static int get_final_price(double directions){

        double c = (directions) / 1000;
        c = Math.round(c) * price;
        int p3 = (int) c;
        int p = fixed_price + p3;
        return p;
    }

    public static int get_price(double directions, int price_km){

        double c = (directions) / 1000;
        c = Math.round(c) * 10000;
        int p3 = (int) c;
        int p = price_km + p3;
        return p;
    }





    public static void show_hidden_panel(Context context)
    {
        ImageView close_service=((Activity) context).findViewById(R.id.close_service2);
        close_service.setVisibility(View.VISIBLE);

        ImageView search=((Activity) context).findViewById(R.id.search);
        search.setVisibility(View.GONE);

//       View price_box_border=((Activity) context).findViewById(R.id.price_box_border);
//       price_box_border.setVisibility(View.VISIBLE);

        LinearLayout hidden_panel=((Activity) context).findViewById(R.id.hidden_panel);
        hidden_panel.bringToFront();
        Animation up= AnimationUtils.loadAnimation(context,R.anim.up);
        hidden_panel.startAnimation(up);
        hidden_panel.setVisibility(View.VISIBLE);
    }

    public static void close (Context context){


        ImageView close_service=((Activity) context).findViewById(R.id.close_service2);
        close_service.setVisibility(View.GONE);

        ImageView search=((Activity) context).findViewById(R.id.search);
        search.setVisibility(View.VISIBLE);

//       View price_box_border=((Activity) context).findViewById(R.id.price_box_border);
//       price_box_border.setVisibility(View.VISIBLE);

        LinearLayout hidden_panel=((Activity) context).findViewById(R.id.hidden_panel);

        Animation down= AnimationUtils.loadAnimation(context,R.anim.down);
        hidden_panel.startAnimation(down);
        hidden_panel.setVisibility(View.GONE);

    }


    public static String[] get_time_value()
    {
        String[] values=new String[11];
        values[0]="۰ تا ۵ دقیقه";
        values[1]="۵ تا ۱۰ دقیقه";
        values[2]="۱۰ تا ۱۵ دقیقه";
        values[3]="۱۵ تا ۲۰ دقیقه";
        values[4]="۲۰ تا ۲۵ دقیقه";
        values[5]="۲۵ تا ۳۰ دقیقه";
        values[6]="۳۰ تا ۴۵ دقیقه";
        values[7]="۴۵ تا ۱ ساعت";
        values[8]="۱ تا ۱.۵ ساعت";
        values[9]="۱.۵ تا ۲ ساعت";
        values[10]="";
        return  values;
    }
    public static int[] get_price_time()
    {
        final int[] price_time=new int[3];
        price_time[0]=5000;
        price_time[1]=10000;
        price_time[2]=15000;
        return  price_time;
    }
    public static String get_number(String number)
    {
        number=number.replace("0","۰");
        number=number.replace("1","۱");
        number=number.replace("2","۲");
        number=number.replace("3","۳");
        number=number.replace("4","۴");
        number=number.replace("5","۵");
        number=number.replace("6","۶");
        number=number.replace("7","۷");
        number=number.replace("8","۸");
        number=number.replace("9","۹");

        return  number;
    }
    public static String get_service_status(int s)
    {
        Map<Integer,String> map=new HashMap<Integer,String>();

        map.put(-10,"لغو سفر");
        map.put(-1,"در انتظار راننده");
        map.put(1,"قبول درخواست توسط راننده");
        map.put(2,"رسیدن راننده به مبدا");
        map.put(3,"سوار شدن مسافر");
        map.put(4,"رسیدن به مقصد اول");
        map.put(5,"رسیدن به مقصد دوم");


        return  map.get(s);
    }
    public static String get_price(String price)
    {
        DecimalFormat decimalFormat=new DecimalFormat("###,###");

        String p_string=decimalFormat.format(Integer.valueOf(price))+" ریال";

        p_string=p_string.replace("0","۰");
        p_string=p_string.replace("1","۱");
        p_string=p_string.replace("2","۲");
        p_string=p_string.replace("3","۳");
        p_string=p_string.replace("4","۴");
        p_string=p_string.replace("5","۵");
        p_string=p_string.replace("6","۶");
        p_string=p_string.replace("7","۷");
        p_string=p_string.replace("8","۸");
        p_string=p_string.replace("9","۹");

        return  p_string;
    }
    public static void show_notification(Context context,String title,String text,String class_name,String activity_key,String activity_value)
    {
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId="user_channel";
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,channelId);
        builder.setSmallIcon(R.mipmap.ic_logo);
        builder.setColor(Color.RED);


        RemoteViews content=new RemoteViews(context.getPackageName(),R.layout.notification);
        content.setTextViewText(R.id.title,title);
        content.setTextViewText(R.id.content,text);
        builder.setCustomContentView(content);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            String channel_name="taxi notification";
            int importance=NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel=new NotificationChannel(channelId,channel_name,importance);
            notificationChannel.setDescription("taxi notification channel");
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            if(notificationManager!=null)
            {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }


        Class<?> myClass=null;

        try {
            myClass=myClass.forName(class_name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent=new Intent(context,myClass);

        if(!activity_key.isEmpty() && !activity_value.isEmpty())
        {
            intent.putExtra(activity_key,activity_value);
        }

        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        i++;
        notificationManager.notify(i,builder.build());
    }

    public static void set_Socket(Socket s)
    {
        socket=s;
    }
    public static Socket get_Socket()
    {
        return socket;
    }





}
