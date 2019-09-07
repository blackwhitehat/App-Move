package Lib;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arikehparsi.reyhanh.yedarbast.ApiInterface;
import com.arikehparsi.reyhanh.yedarbast.CedarMapActivity;
import com.arikehparsi.reyhanh.yedarbast.ServerData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.arikehparsi.reyhanh.yedarbast.R;

import java.text.DecimalFormat;

public class OrderPrice {

    public static String baseURL= "http://185.8.175.151:3000/";

    static int price = 0;

    static int fixed_price = 0;
    static int i = 0;

    static double service_lat, service_lng, service_directions;
    static boolean run_fun = false;
    static Context cx;
    public static int service_price;



    public static void get_price_from_server(double lat,double lng)
    {
        service_lat = lat;
        service_lng = lng;
        i = 1;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ServerData.Service_Price> call=apiInterface.get_service_price(String.valueOf(lat),String.valueOf(lng));
        Callback<ServerData.Service_Price> callback=new Callback<ServerData.Service_Price>() {
            @Override
            public void onResponse(Call<ServerData.Service_Price> call, Response<ServerData.Service_Price> response) {

                if(response.isSuccessful())
                {
                    price = Integer.valueOf(response.body().getPrice());
                    fixed_price = Integer.valueOf(response.body().getFixed_price());


                    Log.i("price", response.body().getPrice());
                    Log.i("fixed_price", response.body().getFixed_price());
                    if (run_fun){
                        show_service_info(cx, service_directions);
                    }
                }

            }

            @Override
            public void onFailure(Call<ServerData.Service_Price> call, Throwable t) {

            }

        };
        call.enqueue(callback);
    }

    public static void set_error_service(){



    }

    public static void show_service_info(Context context, double directions){

        Toast.makeText(context, String.valueOf(service_lat), Toast.LENGTH_SHORT).show();
        
        if (price == 0 && i < 3){

            service_directions = directions;
            cx = context;
            i++;
            run_fun = true;
            get_price_from_server(service_lat, service_lng);
        }
        else {

            service_price = get_final_price(directions);
            show_view(context, service_price);


        }

    }

    public static int get_final_price(double directions){

        double c = (directions) / 1000;
        c = Math.round(c) * price;
        int p3 = (int) c;
        int p = fixed_price + p3;
        return p;
    }

    public static void show_view(Context context, int service_price){


        final DecimalFormat decimalFormat = new DecimalFormat("###,###");
        RelativeLayout count_box = ((Activity) context).findViewById(R.id.count_box);
        RelativeLayout price_box = ((Activity) context).findViewById(R.id.price_box);
        RelativeLayout request_driver = ((Activity) context).findViewById(R.id.request_driver);
        TextView price_text = ((Activity) context).findViewById(R.id.price);

        count_box.setVisibility(View.GONE);
        price_box.setVisibility(View.VISIBLE);

        request_driver.setVisibility(View.VISIBLE);

        price_text.setText(service_price + " ريال");
       // CedarMapActivity.show_price_info(service_price,context);

    }

}
