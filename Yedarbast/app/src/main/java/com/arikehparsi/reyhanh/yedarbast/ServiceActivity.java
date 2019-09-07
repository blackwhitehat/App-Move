package com.arikehparsi.reyhanh.yedarbast;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cedarstudios.cedarmapssdk.CedarMaps;
import com.cedarstudios.cedarmapssdk.Dimension;
import com.cedarstudios.cedarmapssdk.listeners.StaticMapImageResultListener;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.storage.Resource;

import java.io.InputStream;
import java.util.ArrayList;

import Lib.Service;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iranSansWeb.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_service);


        Bundle bundle=getIntent().getExtras();

        String order_id=bundle.getString("order_id");
        String address1=bundle.getString("address1");
        String address2=bundle.getString("address2");
        String address3=bundle.getString("address3");
        String driver_name=bundle.getString("driver_name");
        String driver_mobile=bundle.getString("driver_mobile");
        String car_type=bundle.getString("car_type");
        String going_back=bundle.getString("going_back");
        String price=bundle.getString("price");
        String total_price=bundle.getString("total_price");
        String stop_time=bundle.getString("stop_time");
        String date=bundle.getString("date");


        TextView driver_name_text=(TextView)findViewById(R.id.driver_name_text);
        driver_name_text.setText(driver_name);

        TextView driver_type_text=(TextView)findViewById(R.id.driver_type_text);
        driver_type_text.setText(car_type);

        TextView address1_text=(TextView)findViewById(R.id.address1_text);
        address1_text.setText(address1);

        TextView address2_text=(TextView)findViewById(R.id.address2_text);
        address2_text.setText(address2);

        if(!address3.equals("null"))
        {
            TextView address3_text=(TextView)findViewById(R.id.address3_text);
            address3_text.setText(address3);
        }
        else
        {
            LinearLayout address3_box=(LinearLayout)findViewById(R.id.address3_box);
            address3_box.setVisibility(View.GONE);
        }

        if (going_back.equals("no"))
        {
            LinearLayout going_back_box=(LinearLayout)findViewById(R.id.going_back_box);
            going_back_box.setVisibility(View.GONE);
        }
        if(stop_time.equals("0"))
        {
            LinearLayout going_back_box=(LinearLayout)findViewById(R.id.shop_time_box);
            going_back_box.setVisibility(View.GONE);
        }
        else {
            TextView stop_time_text=(TextView)findViewById(R.id.stop_time_text);
            stop_time_text.setText(stop_time);
        }


        TextView price_text=(TextView)findViewById(R.id.price_text);
        price_text.setText(Service.get_price(price));

        int a=(Integer.valueOf(total_price)-Integer.valueOf(price));
        TextView discount_text=(TextView)findViewById(R.id.discount_text);
        discount_text.setText(Service.get_price(String.valueOf(a)));


        TextView total_price_text=(TextView)findViewById(R.id.total_price_text);
        total_price_text.setText(Service.get_price(total_price));

        TextView date_text=(TextView)findViewById(R.id.date_text);
        date_text.setText(date);

        TextView order_id_text=(TextView)findViewById(R.id.order_id_text);
        order_id_text.setText(order_id);

    }
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
