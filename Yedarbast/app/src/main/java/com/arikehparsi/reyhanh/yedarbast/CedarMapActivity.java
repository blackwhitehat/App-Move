package com.arikehparsi.reyhanh.yedarbast;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arikehparsi.reyhanh.yedarbast.Game.Game_Activity_Main;
import com.cedarstudios.cedarmapssdk.CedarMaps;
import com.cedarstudios.cedarmapssdk.MapView;
import com.cedarstudios.cedarmapssdk.listeners.OnTilesConfigured;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.SphericalUtil;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.nightonke.boommenu.Animation.EaseEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.victor.loading.rotate.RotateLoading;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import Lib.OrderPrice;
import Lib.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CedarMapActivity extends AppCompatActivity implements LocationListener {

    String baseUrl = "http://192.168.1.103:3000/";


    MapboxMap mMapboxMap;
    com.mapbox.mapboxsdk.annotations.Marker m1, m2, m3;

    int set_latlng1 = 1;
    public static Boolean Finalepay = false;
    int set_latlng2 = 1;
    int set_latlng3 = 1;
    String going_back = "no";
    Long m1_id, m2_id, m3_id;
    String address1, address2, address3;
    com.github.nkzawa.socketio.client.Socket socket;


    com.mapbox.mapboxsdk.geometry.LatLng latLng1, latLng2, latLng3;


    TextView price, toolbar_text, car_count, show_hidden_panel;
    double service_price = 0;
    double service_price2 = 0;
    RelativeLayout count_box, price_box, request_driver;

    int remover_car_marker = 0;
    private Boolean inState = false;
    int service_status;
    com.mapbox.mapboxsdk.annotations.Marker[] car_marker;
    private BoomMenuButton bmb;

    private String lst[] = {"بازی", "لیست سفر ها", "صفحه کاربری", "پشتیبانی", "درباره یه دربست", "موسیقی", "معرفی به دوستان", "اینستاگرام", "تنظیمات"};
    private int pic[] = {R.drawable.ic_game, R.drawable.ic_list, R.drawable.ic_profile, R.drawable.ic_support, R.drawable.ic_about, R.drawable.ic_music, R.drawable.ic_share, R.drawable.instagram, R.drawable.ic_setting};


    LocationManager locationManager;
    private RelativeLayout watinglayout;

    int stop_time = 0;
    Handler handler;

    String mobile;

    SharedPreferences sp;

    //driver info
    TextView driver_name_text;
    TextView car_type_text;


    TextView city_number_text;

    TextView city_code_text;

    TextView number_plates_text;

    TextView code_number_plates_text;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Bundle bundle = getIntent().getExtras();


        CedarMaps.getInstance()
                .setClientID(CedarMapConfig.CLIENT_ID)
                .setClientSecret(CedarMapConfig.CLIENT_SECRET)
                .setContext(this);
        try {
            socket = IO.socket(Service.baseURL);
            socket.connect();
            SharedPreferences sp5;
            sp5 = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);
            String token7 = sp5.getString("token", "no");
            socket.emit("User_Connected", token7);
        } catch (URISyntaxException E) {
            E.printStackTrace();
        }


        CedarMaps.getInstance().prepareTiles(new OnTilesConfigured() {
            @Override
            public void onSuccess() {
                setContentView(R.layout.activity_cedar_map);
                android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar_cedar);
                setSupportActionBar(toolbar);
                count_box = findViewById(R.id.count_box);
                price_box = findViewById(R.id.price_box);
                toolbar_text = findViewById(R.id.toolbar_text);
                car_count = findViewById(R.id.car_count);
                request_driver = findViewById(R.id.request_driver);
                show_hidden_panel = findViewById(R.id.show_hidden_panel);
                //driver info

                sp = getSharedPreferences("Taxi_user_datas", 0);
                sp = getApplicationContext().getSharedPreferences("Taxi_user_datas", 0);
                map();

                /////////////////////////////////menu

                bmb = findViewById(R.id.bmb);

                bmb.setShowMoveEaseEnum(EaseEnum.EaseOutBack);
                for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
                    TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                            .normalImageRes(pic[i])
                            .shadowEffect(true)
                            .normalTextColor(Color.YELLOW)

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
                                            startActivity(new Intent(getApplicationContext(), SupportActivity.class));
                                            break;
                                        case 4:
                                            startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                                            break;
                                        case 5:
                                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
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

                ///////////////////////end of menu
                boolean intrip = bundle.getBoolean("intrip", false);
                if (intrip) {
                    SharedPreferences Taxi_user_datas = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);
                    token1 = Taxi_user_datas.getString("token", "null");
                    startService(new Intent(getApplicationContext(), IService.class));
                    NameOfDriver = bundle.getString("driverName");
                    driverMobile = bundle.getString("driver_mobile");
                    url = bundle.getString("photo_url");
                    city_number = bundle.getString("city_number");
                    service_price = Integer.parseInt(bundle.getString("price"));
                    car_type = bundle.getString("car_type");
                    city_code = bundle.getString("city_code");
                    s_id = bundle.getString("service_id");
                    number_plates = bundle.getString("number_plates");
                    service_status = bundle.getInt("service_status");
                    if (service_status == 2) {
                        RelativeLayout request_message = (RelativeLayout) findViewById(R.id.request_message);
                        request_message.setVisibility(View.VISIBLE);
                        MenuActivity.intrip = false;
                        startActivity(new Intent(getApplicationContext(), CedarMapActivity.class));
                    } else if (service_status == -10) {
                        Toast.makeText(CedarMapActivity.this, " سفر لغو شده", Toast.LENGTH_SHORT).show();
                    } else if (service_status == 3) {
                        RelativeLayout request_message = (RelativeLayout) findViewById(R.id.request_message);
                        request_message.setVisibility(View.VISIBLE);
                        TextView Message = findViewById(R.id.Trip_Status);
                        Message.setText("در حال سفر");

                    } else if (service_status == 5 || service_status == 4) {
                        TRIP_Has_Ended();
                    }
                    Finalepay = bundle.getBoolean("epay");
                    socket.on("set_status", StatusHandle);
                    socket.on("end_of_trip", endTrip);
                    inState = true;
                    driver_name_text = findViewById(R.id.driver_name);
                    car_type_text = findViewById(R.id.car_type);
                    city_number_text = findViewById(R.id.city_number);
                    city_code_text = findViewById(R.id.city_code);
                    number_plates_text = findViewById(R.id.number_plates);
                    code_number_plates_text = findViewById(R.id.code_number_plates);


                    handler = new Handler();
                    handler3 = new Handler();
                    startService(new Intent(getApplicationContext(), IService.class));

                    RelativeLayout driver_info = (RelativeLayout) findViewById(R.id.driver_info);
                    driver_info.setVisibility(View.VISIBLE);


                    ImageView close_service = findViewById(R.id.close_service);
                    close_service.setVisibility(View.VISIBLE);


                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                            price_box.setLayoutParams(layoutParams);
//                            price_box.setVisibility(View.VISIBLE);

                    RelativeLayout request_car_btn = (RelativeLayout) findViewById(R.id.request_driver);
                    request_car_btn.setVisibility(View.GONE);

//                            View price_box_border=(View)findViewById(R.id.price_box_border);
//                            price_box_border.setVisibility(View.VISIBLE);


                    driver_name_text.setText(NameOfDriver);
                    car_type_text.setText(car_type);
                    city_number_text.setText(city_number);
                    city_code_text.setText(city_code);
                    new DownloadImageTask(findViewById(R.id.driver_icon)).execute(url);
                    number_plates_text.setText(number_plates);
                    code_number_plates_text.setText(code_number_plates);


                } else {
                    Toast.makeText(CedarMapActivity.this, "HI", Toast.LENGTH_SHORT).show();
                    stopService(new Intent(getApplicationContext(), IService.class));
                }

            }

            @Override
            public void onFailure(@NonNull String errorMessage) {

            }
        });


    }

    public void map() {

        MapView mMapView = findViewById(R.id.mapView);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;

                com.mapbox.mapboxsdk.geometry.LatLng latLng = new com.mapbox.mapboxsdk.geometry.LatLng(35.762879, 51.448243);

                MarkerOptions options = new MarkerOptions().position(latLng).icon(IconFactory.getInstance(getBaseContext()).fromResource(R.drawable.firstlocation));
                m1 = mMapboxMap.addMarker(options);

                m1_id = m1.getId();

                if (CheckGPS()) {
                    if (ActivityCompat.checkSelfPermission(CedarMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CedarMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(CedarMapActivity.this, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                        }, 101);
                    } else {
                        set_location();
                    }
                } else {

                    show_dialog_box();
                }


                mapboxMap.addOnCameraIdleListener(new MapboxMap.OnCameraIdleListener() {

                    com.mapbox.mapboxsdk.geometry.LatLng position = mapboxMap.getCameraPosition().target;

                    @Override
                    public void onCameraIdle() {

                        if (set_latlng1 == 1) {

                            m1.setIcon(IconFactory.getInstance(getBaseContext()).fromResource(R.drawable.firstlocation));

                        }
                        if (m2 != null) {

                            m2.setIcon(IconFactory.getInstance(getBaseContext()).fromResource(R.drawable.secondlocation));

                        }
                        if (m3 != null) {

                            m3.setIcon(IconFactory.getInstance(getBaseContext()).fromResource(R.drawable.thirdlocation));

                        }
                        if (set_latlng1 == 1) {

                            get_driver(position.getLatitude(), position.getLongitude());
                        }

                    }
                });

                move_map(mMapboxMap);

                marker_click(mMapboxMap);


            }
        });
    }

    public void move_map(final MapboxMap mapboxMap) {

        final int width = 40;
        final int height = 50;

        mMapboxMap.addOnCameraMoveListener(new MapboxMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

                com.mapbox.mapboxsdk.geometry.LatLng position = new com.mapbox.mapboxsdk.geometry.LatLng(mapboxMap.getCameraPosition().target.getLatitude(), mapboxMap.getCameraPosition().target.getLongitude());

                if (set_latlng1 == 1) {

                    BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.firstlocation);
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    Bitmap small = Bitmap.createScaledBitmap(bitmap, width, height, true);

                    m1.setIcon(IconFactory.getInstance(getBaseContext()).fromBitmap(small));
                    m1.setPosition(position);

                } else {

                    if (set_latlng2 == 1) {

                        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.secondlocation);
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Bitmap small = Bitmap.createScaledBitmap(bitmap, width, height, true);

                        m2.setIcon(IconFactory.getInstance(getBaseContext()).fromBitmap(small));
                        m2.setPosition(position);

                    } else if (set_latlng3 == 1 && m3_id != null) {

                        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.thirdlocation);
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Bitmap small = Bitmap.createScaledBitmap(bitmap, width, height, true);

                        m3.setIcon(IconFactory.getInstance(getBaseContext()).fromBitmap(small));
                        m3.setPosition(position);
                    }
                }

            }
        });

    }

    public void marker_click(final MapboxMap mapboxMap) {

        com.mapbox.mapboxsdk.geometry.LatLng latLng = new com.mapbox.mapboxsdk.geometry.LatLng(35.762879, 51.448243);

        final MarkerOptions options2 = new MarkerOptions()
                .setPosition(latLng)
                .icon(IconFactory.getInstance(getBaseContext()).fromResource(R.drawable.secondlocation));


        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull com.mapbox.mapboxsdk.annotations.Marker marker) {

                if (marker.getId() == m1_id && set_latlng1 == 1) {

                    latLng1 = marker.getPosition();
                    LatLng position1 = new LatLng(latLng1.getLatitude(), latLng1.getLongitude());
                    LatLng m2_position = SphericalUtil.computeOffset(position1, 40, 220);

                    com.mapbox.mapboxsdk.geometry.LatLng position2 = new com.mapbox.mapboxsdk.geometry.LatLng(m2_position.latitude, m2_position.longitude);


                    m2 = mapboxMap.addMarker(options2);
                    m2.setPosition(position2);
                    m2_id = m2.getId();

                    set_latlng1 = 0;
                    Service.set_address(latLng1, 1);

                    OrderPrice.get_price_from_server(latLng1.getLatitude(), latLng1.getLongitude());
                    // set_address(latLng1);
                }
                if (marker.getId() == m2_id && set_latlng2 == 1) {


                    latLng2 = marker.getPosition();
                    mapboxMap.setMinZoomPreference(14);
                    mapboxMap.setMaxZoomPreference(14);
                    get_directions();
                    Service.set_address(latLng2, 2);
                    set_latlng2 = 0;
                }

                if (m3_id != null) {

                    if (marker.getId() == m3_id && set_latlng3 == 1) {

                        set_latlng3 = 0;

                        latLng3 = marker.getPosition();
                        mapboxMap.setMinZoomPreference(14);
                        mapboxMap.setMaxZoomPreference(14);
                        get_directions2();
                        Service.set_address(latLng3, 3);
                    }
                }

                return false;
            }
        });

    }

    public void get_directions() {

        final DecimalFormat decimalFormat = new DecimalFormat("###,###");
        toolbar_text.setText("سفر سلامت");
        car_count.setText("در حال محاسبه هزینه ...");

        Service.get_directions(latLng1.getLatitude(), latLng1.getLongitude(), latLng2.getLatitude(), latLng2.getLongitude(), new Service.get_callback() {
            @Override
            public void onResponse(double directions) {


//                service_price = Service.get_final_price(directions);
//                price = findViewById(R.id.price);
//                Amount = String.valueOf(service_price);
//
//
//                if (service_price == 0) {
//
//
//                    set_error_final_service_price();
//
//                } else {
//
//                    count_box.setVisibility(View.GONE);
//                    price_box.setVisibility(View.VISIBLE);
//                    request_driver.setVisibility(View.VISIBLE);
//
//
//                    String p_string = decimalFormat.format(service_price) + " ريال";
//                    price.setText(p_string);
//                    Log.i("price", p_string);
//                }


                OrderPrice.show_service_info(CedarMapActivity.this, directions);


            }

            @Override
            public void onFailure(String error) {

//                Toast.makeText(CedarMapActivity.this, "خطا در محاسبه هزینه ...", Toast.LENGTH_LONG).show();
//                Log.i("error", error);
//                set_error_final_service_price();
                OrderPrice.set_error_service();

            }
        });


    }
    public static void show_price_info(int sp ,Context cx)
    {
        TextView pricce = ((Activity) cx).findViewById(R.id.price);
        pricce.setText(sp+ " ريال");



    }



    public void get_driver(double lat, double lng) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Service.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<List<ServerData.location_driver>> call = apiInterface.send_location(lat, lng);
        Callback<List<ServerData.location_driver>> callback = new Callback<List<ServerData.location_driver>>() {
            @Override
            public void onResponse(Call<List<ServerData.location_driver>> call, Response<List<ServerData.location_driver>> response) {

                if (response.isSuccessful()) {

                    if (remover_car_marker == 1) {

                        for (int j = 0; j < car_marker.length; j++) {

                            com.mapbox.mapboxsdk.annotations.Marker c = car_marker[j];
                            c.remove();

                        }
                    }

                    BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.car);
                    Bitmap bitmap = bitmapDrawable.getBitmap();

                    int i = 0;
                    car_marker = new com.mapbox.mapboxsdk.annotations.Marker[response.body().size()];
                    String s = response.body().size() + " تاکسی موجود";

                    car_count.setText(s);

                    remover_car_marker = 1;


                    for (ServerData.location_driver location_driver : response.body()) {

                        Matrix matrix = new Matrix();
//                        matrix.postRotate(Float.valueOf(location_driver.getAngle()));
                        Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                        com.mapbox.mapboxsdk.geometry.LatLng tehran = new com.mapbox.mapboxsdk.geometry.LatLng(Double.valueOf(location_driver.getLat()), Double.valueOf(location_driver.getLng()));

                        final MarkerOptions markerOptions2 = new MarkerOptions().position(tehran)
                                .icon(IconFactory.getInstance(CedarMapActivity.this).fromBitmap(b));

                        com.mapbox.mapboxsdk.annotations.Marker car = mMapboxMap.addMarker(markerOptions2);
                        car_marker[i] = car;
                        i++;

                    }
                }
            }

            @Override
            public void onFailure(Call<List<ServerData.location_driver>> call, Throwable t) {

            }

        };
        call.enqueue(callback);

    }

    public void show_hidden_panel(View view) {

        Service.show_hidden_panel(CedarMapActivity.this);

    }

    public void close(View view) {

        Service.close(CedarMapActivity.this);
    }

    public void second_destination(View view) {

        LinearLayout hidden_panel = findViewById(R.id.hidden_panel);
        hidden_panel.setVisibility(View.GONE);

        RelativeLayout requset_driver = findViewById(R.id.request_driver);
        requset_driver.setVisibility(View.GONE);

        RelativeLayout price_box = findViewById(R.id.price_box);
        price_box.setVisibility(View.GONE);

        RelativeLayout count_box = findViewById(R.id.count_box);
        count_box.setVisibility(View.VISIBLE);

        car_count.setText("انتخاب مقصد دوم");

        MarkerOptions markerOptions3 = new MarkerOptions().position(latLng2)
                .icon(IconFactory.getInstance(CedarMapActivity.this).fromResource(R.drawable.thirdlocation));

        m3 = mMapboxMap.addMarker(markerOptions3);

        LatLng l = new LatLng(latLng2.getLatitude(), latLng2.getLongitude());

        LatLng m3_p = SphericalUtil.computeOffset(l, 70, 250);
        com.mapbox.mapboxsdk.geometry.LatLng m3_position = new com.mapbox.mapboxsdk.geometry.LatLng(m3_p.latitude, m3_p.longitude);
        m3.setPosition(m3_position);
        m3_id = m3.getId();

        mMapboxMap.moveCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngZoom(m3_position, 16));

    }

    public void get_directions2() {

        toolbar_text.setText("سفر سلامت");
        final DecimalFormat decimalFormat = new DecimalFormat("###,###");
        car_count.setText("در حال محاسب هزینه ...");
        Service.get_directions(latLng2.getLatitude(), latLng2.getLongitude(), latLng3.getLatitude(), latLng3.getLongitude(), new Service.get_callback() {
            @Override
            public void onResponse(double directions) {

                double p = Service.get_price(directions, 25000);

                p = p + service_price;
                count_box.setVisibility(View.GONE);
                price_box.setVisibility(View.VISIBLE);
                service_price = p;
                price = findViewById(R.id.price);
                String p_string = decimalFormat.format(p) + " ريال";
                Amount = String.valueOf(service_price);
                price.setText(p_string);

                RelativeLayout request_driver = findViewById(R.id.request_driver);
                request_driver.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(String error) {

                Toast.makeText(CedarMapActivity.this, "خطا در محاسبه هزینه ...", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static String driverMobile;
    public String token1;

    public void Cancel_Request(View view) {
        /*SharedPreferences Taxi_user_datas = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);
        token1 = Taxi_user_datas.getString("token", "null");
        String service_id = s_id;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Service.baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ServerData.cancel_request> call = apiInterface.cancel_request(token1,service_id);
        Callback<ServerData.cancel_request> callback =new Callback<ServerData.cancel_request>() {
            @Override
            public void onResponse(Call<ServerData.cancel_request> call, Response<ServerData.cancel_request> response) {
                if(response.isSuccessful())
                {
                    String result = response.body().getService_status();
                    if(result.equals("0"))
                    {
                        Toast.makeText(CedarMapActivity.this, "Trip Canceled", Toast.LENGTH_SHORT).show();
                        Intent j =new Intent(getApplicationContext(),MenuActivity.class);
                        startActivity(j);
                    }
                    else
                        {
                            Toast.makeText(CedarMapActivity.this, "failed for cancelation req", Toast.LENGTH_SHORT).show();
                        }
                }

            }

            @Override
            public void onFailure(Call<ServerData.cancel_request> call, Throwable t) {
                Toast.makeText(CedarMapActivity.this, "failed for cancelation req", Toast.LENGTH_SHORT).show();

            }
        };
        call.enqueue(callback);*/
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext()).setMessage("آیا از لغو سفر اطمینان داردید؟");
        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences Taxi_user_datas = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);
                token1 = Taxi_user_datas.getString("token", "null");
                String service_id = s_id;

                try{
                    IO.Options options = new IO.Options();
                    options.reconnection = true;
                    Socket socket= IO.socket(Service.baseURL);
                    socket.emit("cancel_request", token1, service_id, new Ack() {
                        @Override
                        public void call(Object... args) {
                            JSONObject jsonObject = (JSONObject)args[0];
                            dialogInterface.dismiss();
                        }
                    });
                }catch (URISyntaxException e){e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        builder.show();





    }

    @Override
    public void onLocationChanged(Location location) {

        //        String a = location.getLatitude() + "," + location.getLongitude();
//        Toast.makeText(this, a, Toast.LENGTH_LONG).show();
        if (location.getProvider().equals("gps")) {

            locationManager.removeUpdates(this);
        }
        com.mapbox.mapboxsdk.geometry.LatLng user_location = new com.mapbox.mapboxsdk.geometry.LatLng(location.getLatitude(), location.getLongitude());

        if (m1 != null) {

            m1.setPosition(user_location);

        }

        mMapboxMap.moveCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngZoom(user_location, 16));


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void set_location() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);

    }

    public boolean CheckGPS() {

        boolean status;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return status;
    }

    public void show_dialog_box() {
        //show the message of turning on the location
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.location_message_r);
        dialog.show();

        TextView btn_true = dialog.findViewById(R.id.btn_true);


        btn_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        });


        TextView btn_false = dialog.findViewById(R.id.btn_false);

        btn_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });
    }

    RotateLoading waiting_loading;

    public void request_driver(View view) {
        startService(new Intent(getApplicationContext(), IService.class));

        watinglayout = findViewById(R.id.waitinglayout);
        waiting_loading = findViewById(R.id.rotateLoading);
        watinglayout.bringToFront();
        watinglayout.setVisibility(View.VISIBLE);
        service_price = OrderPrice.service_price;


        waiting_loading.start();
        SharedPreferences Taxi_user_datas = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);
        String token = Taxi_user_datas.getString("token", "null");


        handler = new Handler();


        if (latLng3 != null) {

            socket.emit("add_service", token, Service.address1, Service.address2, Service.address3, OrderPrice.service_price, stop_time,
                    latLng1.getLatitude(), latLng2.getLatitude(),
                    latLng1.getLongitude(), latLng2.getLongitude(), "", latLng3.getLatitude(), latLng3.getLongitude());
        } else {

            socket.emit("add_service", token, Service.address1, Service.address2, Service.address3, OrderPrice.service_price, stop_time,
                    latLng1.getLatitude(), latLng2.getLatitude(),
                    latLng1.getLongitude(), latLng2.getLongitude(), "");

        }

        socket.on("set_status", StatusHandle);



    }

    public String NameOfDriver;
    Handler handler3;
    Handler handler4;
    String code_number_plates;
    String number_plates;
    String city_number;
    String city_code;
    String car_type;
    String s_id;

    public Emitter.Listener StatusHandle = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            final JSONObject data = (JSONObject) args[0];
            handler.post(new Runnable() {
                @Override
                public void run() {

                    try {
                        String Status = data.getString("status");

                        if (Status.equals("1")) {


                            handler4 = new Handler();
                            handler3 = new Handler();


                            inState = true;



                            watinglayout.setVisibility(View.INVISIBLE);

                            RelativeLayout driver_info = (RelativeLayout) findViewById(R.id.driver_info);
                            driver_info.setVisibility(View.VISIBLE);
                            driver_name_text = findViewById(R.id.driver_name);
                            car_type_text = findViewById(R.id.car_type);
                            city_number_text = findViewById(R.id.city_number);
                            city_code_text = findViewById(R.id.city_code);
                            number_plates_text = findViewById(R.id.number_plates);
                            code_number_plates_text = findViewById(R.id.code_number_plates);

                            String driver_name = data.getString("driver_name");
                            car_type = data.getString("car_type");
                            city_code= data.getString("city_code");
                            city_number = data.getString("city_number");
                            number_plates = data.getString("number_plates");
                            code_number_plates = data.getString("code_number_plates");
                            s_id = data.getString("service_id");

                            mobile = data.getString("mobile");
                            url = data.getString("url");
                            ImageView close_service = findViewById(R.id.close_service);
                            close_service.setVisibility(View.VISIBLE);



                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            // price_box.setLayoutParams(layoutParams);
//                            price_box.setVisibility(View.VISIBLE);

                            RelativeLayout request_car_btn = (RelativeLayout) findViewById(R.id.request_driver);
                            request_car_btn.setVisibility(View.GONE);
                            new DownloadImageTask(findViewById(R.id.driver_icon)).execute(url);

//                            View price_box_border=(View)findViewById(R.id.price_box_border);
//                            price_box_border.setVisibility(View.VISIBLE);


                            driverMobile = mobile;

                            NameOfDriver = driver_name;
                            Toast.makeText(CedarMapActivity.this, NameOfDriver, Toast.LENGTH_SHORT).show();

                            driver_name_text.setText(driver_name);
                            car_type_text.setText(car_type);
                            city_number_text.setText(city_number);
                            city_code_text.setText(city_code);
                            number_plates_text.setText(number_plates);
                            code_number_plates_text.setText(code_number_plates);

                            socket.on("end_of_trip", endTrip);
                        } else if (Status.equals("2")) {
                            RelativeLayout request_message = (RelativeLayout) findViewById(R.id.request_message);
                            request_message.setVisibility(View.VISIBLE);
                            ImageView close_service = findViewById(R.id.close_service);
                            close_service.setVisibility(View.GONE);
                        } else if (Status.equals("-10")) {
                            Toast.makeText(CedarMapActivity.this, "سفر لغو شد", Toast.LENGTH_SHORT).show();
                            Intent j = new Intent(getApplicationContext(),MenuActivity.class);

                            startActivity(j);

                        }else if(Status.equals("3"))
                        {
                            ImageView close_service = findViewById(R.id.close_service);
                            close_service.setVisibility(View.GONE);
                            RelativeLayout request_message = (RelativeLayout) findViewById(R.id.request_message);
                            request_message.setVisibility(View.VISIBLE);
                            TextView Message = findViewById(R.id.Trip_Status);
                            Message.setText("در حال سفر");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    String url;
    public Emitter.Listener photogetter = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject object = (JSONObject) args[0];
            handler4.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        url = object.getString("image");
                        new DownloadImageTask(findViewById(R.id.driver_icon)).execute(url);

                    } catch (JSONException E) {
                        E.printStackTrace();
                    }
                }
            });

        }
    };

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public Emitter.Listener endTrip = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            handler3.post(new Runnable() {
                @Override
                public void run() {
                    ImageView close_service = findViewById(R.id.close_service);
                    close_service.setVisibility(View.GONE);
                    LinearLayout endLayout = findViewById(R.id.endLayout);
                    endLayout.setVisibility(View.VISIBLE);
                    endLayout.bringToFront();
                    TextView driverName = findViewById(R.id.NameOfDriver);
                    TextView AmountOfService = findViewById(R.id.ServiceAmount);
                    driverName.setText("");
                    AmountOfService.setText("");
                    TextView Yes = findViewById(R.id.yes);
                    TextView No = findViewById(R.id.No);
                    driverName.setText(NameOfDriver);
                    if (Finalepay) {
                        AmountOfService.setText("از اعتبار شما کاسته شد" + service_price + "مبلغ");
                    } else {
                        AmountOfService.setText("نقدی پرداخت کنید" + service_price + "مبلغ");
                    }
                    Yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            inState = false;

                            socket.emit("like_Driver", token1, driverName, s_id);
                            endLayout.setVisibility(View.INVISIBLE);
                            Intent myIntentService = new Intent(CedarMapActivity.this, IService.class);

                            stopService(myIntentService);

                            Intent j = new Intent(getApplicationContext(), MenuActivity.class);
                            j.putExtra("intrip", false);
                            startActivity(j);


                        }
                    });
                    No.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inState = false;
                            socket.emit("dilike_Driver", token1, driverName, s_id);
                            endLayout.setVisibility(View.INVISIBLE);
                            Intent myIntentService = new Intent(CedarMapActivity.this, MyService.class);
                            stopService(myIntentService);

                            Intent j = new Intent(getApplicationContext(), MenuActivity.class);
                            j.putExtra("intrip", false);
                            startActivity(j);


                        }
                    });

                }
            });


        }
    };


    public void select_time1(View view) {

        if (stop_time == 0) {

            select_time();
        } else {

            final DecimalFormat decimalFormat = new DecimalFormat("###,###");

            final TextView times = findViewById(R.id.times);
            times.setText("مجموع توقف");
            service_price = service_price - stop_time;
            Amount = String.valueOf(service_price);
            String p_string = decimalFormat.format(service_price) + " ريال";
            price.setText(p_string);

        }

    }

    public void TRIP_Has_Ended() {
        ImageView close_service = findViewById(R.id.close_service);
        close_service.setVisibility(View.GONE);
        LinearLayout endLayout = findViewById(R.id.endLayout);
        endLayout.setVisibility(View.VISIBLE);
        endLayout.bringToFront();
        TextView driverName = findViewById(R.id.NameOfDriver);
        TextView AmountOfService = findViewById(R.id.ServiceAmount);
        driverName.setText("");
        AmountOfService.setText("");
        TextView Yes = findViewById(R.id.yes);
        TextView No = findViewById(R.id.No);
        driverName.setText(NameOfDriver);
        if (Finalepay) {
            AmountOfService.setText("از اعتبار شما کاسته شد" + service_price + "مبلغ");
        } else {
            AmountOfService.setText("نقدی پرداخت کنید" + service_price + "مبلغ");
        }
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inState = false;

                Socket socket1;
                try {
                    socket1 = IO.socket(Service.baseURL);
                    socket1.connect();
                    socket1.emit("like_Driver", token1, driverName, s_id);
                } catch (URISyntaxException E) {
                    E.printStackTrace();
                }
                endLayout.setVisibility(View.INVISIBLE);
                Intent j = new Intent(getApplicationContext(), MenuActivity.class);
                stopService(new Intent(getApplicationContext(), IService.class));
                j.putExtra("intrip", false);
                startActivity(j);


            }
        });
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inState = false;
                Socket socket1;
                try {
                    socket1 = IO.socket(Service.baseURL);
                    socket1.connect();
                    socket1.emit("dislike_Driver", token1, driverName, s_id);
                } catch (URISyntaxException E) {
                    E.printStackTrace();
                }
                stopService(new Intent(getApplicationContext(), IService.class));
                endLayout.setVisibility(View.INVISIBLE);
                stopService(new Intent(getApplicationContext(), IService.class));
                Intent j = new Intent(getApplicationContext(), MenuActivity.class);
                j.putExtra("intrip", false);
                startActivity(j);


            }
        });

    }


    public void select_time2(View view) {

        select_time();

    }

    public void select_time() {

        final TextView times = findViewById(R.id.times);
        final DecimalFormat decimalFormat = new DecimalFormat("###,###");


        Dialog dialog_time = new Dialog(this);
        dialog_time.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View time_view = LayoutInflater.from(this).inflate(R.layout.time_dialog, null);

        final NumberPicker time = (NumberPicker) time_view.findViewById(R.id.time);

        String[] values = new String[10];
        values[0] = "0 تا 5 دقیقه";
        values[1] = "5 تا 10 دقیقه";
        values[2] = "10 تا 15 دقیقه";
        values[3] = "15 تا 20 دقیقه";
        values[4] = "20 تا 25 دقیقه";
        values[5] = "25 تا 30 دقیقه";
        values[6] = "30 تا 35 دقیقه";
        values[7] = "35 تا 40 دقیقه";
        values[8] = "40 تا 45 دقیقه";
        values[9] = "45 تا 50 دقیقه";

        int[] price_time = new int[10];
        price_time[0] = 10000;
        price_time[1] = 20000;
        price_time[2] = 30000;
        price_time[3] = 40000;
        price_time[4] = 50000;
        price_time[5] = 60000;
        price_time[6] = 70000;
        price_time[7] = 80000;
        price_time[8] = 90000;
        price_time[9] = 100000;

        time.setMinValue(0);
        time.setMaxValue(9);
        time.setDisplayedValues(values);

        Button select_time = time_view.findViewById(R.id.select_time);
        select_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(CedarMapActivity.this, String.valueOf(time.getValue()), Toast.LENGTH_LONG).show();
                int c = price_time[time.getValue()];
                service_price = service_price + c;
                dialog_time.dismiss();
                stop_time = c;
                times.setText(values[time.getValue()]);
                String p_string = decimalFormat.format(service_price) + " ريال";
                price.setText(p_string);
                Amount = String.valueOf(service_price);
            }
        });

        dialog_time.setContentView(time_view);
        dialog_time.show();

    }


    public void CampusSafClick(View view) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + driverMobile));

        if (ContextCompat.checkSelfPermission(CedarMapActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CedarMapActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {

            startActivity(callIntent);
        }

    }

    public void going_round(View view) {

        RelativeLayout going_round1 = findViewById(R.id.going_round1);
        RelativeLayout going_round2 = findViewById(R.id.going_round2);

        if (service_price2 == 0) {

            going_round1.setVisibility(View.GONE);
            going_round2.setVisibility(View.VISIBLE);

            final DecimalFormat decimalFormat = new DecimalFormat("###,###");
            toolbar_text.setText("سفر سلامت");
            car_count.setText("در حال محاسبه هزینه ...");

            Service.get_directions(latLng2.getLatitude(), latLng2.getLongitude(), latLng1.getLatitude(), latLng1.getLongitude(), new Service.get_callback() {
                @Override
                public void onResponse(double directions) {

                    count_box.setVisibility(View.GONE);
                    price_box.setVisibility(View.VISIBLE);
                    request_driver.setVisibility(View.VISIBLE);
                    service_price = Service.get_price(directions, 25000);
                    price = findViewById(R.id.price);
                    service_price = 1.6 * service_price;
                    service_price2 = 1.6 * service_price2;
                    String p_string = decimalFormat.format(service_price) + " ريال";
                    price.setText(p_string);
                    Amount = String.valueOf(service_price);
                    Log.i("price", p_string);

                }

                @Override
                public void onFailure(String error) {

                    Toast.makeText(CedarMapActivity.this, "خطا در محاسبه هزینه ...", Toast.LENGTH_LONG).show();
                    Log.i("error", error);

                }
            });


        } else {

            going_round1.setVisibility(View.VISIBLE);
            going_round2.setVisibility(View.GONE);

            final DecimalFormat decimalFormat = new DecimalFormat("###,###");
            service_price = service_price / 1.6;
            String p_string = decimalFormat.format(service_price) + " ريال";
            Amount = String.valueOf(service_price);
            price.setText(p_string);
            service_price2 = 0;

        }


    }

    public void search_click(View view) {

        TextView toolbar_text = findViewById(R.id.toolbar_text);
        toolbar_text.setVisibility(View.GONE);
        LinearLayout search_txt = findViewById(R.id.search_txt);
        search_txt.setVisibility(View.VISIBLE);
        EditText msearch = findViewById(R.id.msearch);
        msearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER
                ) {
                    geolocate(msearch);
                }
                return false;
            }
        });


    }

    private void geolocate(EditText mSearch) {
        Toast.makeText(this, "در حال جستجو...", Toast.LENGTH_LONG).show();
        String searchString = mSearch.getText().toString();
        Geocoder geocoder = new Geocoder(CedarMapActivity.this);
        List<Address> list1 = new ArrayList<>();
        try {
            list1 = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Toast.makeText(this, "خطا در جستجو", Toast.LENGTH_SHORT).show();
        }
        if (list1.size() > 0) {
            Address address = list1.get(0);
            mMapboxMap.moveCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLng(new com.mapbox.mapboxsdk.geometry.LatLng(address.getLatitude(), address.getLongitude())));

        }
        TextView toolbar_text = findViewById(R.id.toolbar_text);
        toolbar_text.setVisibility(View.VISIBLE);
        LinearLayout search_txt = findViewById(R.id.search_txt);
        search_txt.setVisibility(View.GONE);
        mSearch.setText("");


    }

    public String Amount = "";

    public void Pay_Request(View view) {
        LinearLayout pay_border = findViewById(R.id.pay_border);
        if (pay_border.getVisibility() == View.VISIBLE) {
            pay_border.setVisibility(View.GONE);
        } else if (pay_border.getVisibility() == View.GONE) {
            pay_border.setVisibility(View.VISIBLE);
            pay_border.bringToFront();
            Button pay_btn = findViewById(R.id.pay_btn);
            TextView cash_pay = findViewById(R.id.cash_pay);
            TextView epay = findViewById(R.id.epay);
            TextView stock_amount = findViewById(R.id.stock_amount);
            TextView amount1 = findViewById(R.id.amount1);
            TextView amount2 = findViewById(R.id.amount2);
            TextView sprice = findViewById(R.id.service_price);
            amount1.setVisibility(View.INVISIBLE);
            amount2.setVisibility(View.INVISIBLE);
            stock_amount.setVisibility(View.INVISIBLE);
            sprice.setVisibility(View.INVISIBLE);
            Handler handler;
            handler = new Handler();
            epay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount1.setVisibility(View.VISIBLE);
                    amount2.setVisibility(View.VISIBLE);
                    stock_amount.setVisibility(View.VISIBLE);
                    sprice.setVisibility(View.VISIBLE);
                    cash_pay.setTextColor(R.color.zard);
                    epay.setTextColor(R.color.khakestari);
                    sprice.setText("هزینه ی سفر:");
                    amount2.setText(service_price + "ريال");
                    stock_amount.setText("اعتبار شما:");
                    SharedPreferences Taxi_user_datas = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);
                    String token = Taxi_user_datas.getString("token", "null");

                    socket.emit("get_stock", token);
                    socket.on("set_stock", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            final JSONObject data = (JSONObject) args[0];
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (data.getDouble("money") == 0) {
                                            amount1.setText("فاقد اعتبار");
                                            amount1.setTextColor(Color.parseColor("#9D1D1D"));
                                            pay_btn.setText("شارژ حساب کاربری");
                                            pay_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(new Intent(CedarMapActivity.this, PayActivity.class));

                                                }
                                            });
                                        } else if (data.getDouble("money") < service_price) {
                                            amount1.setText(data.getDouble("money") + "ريال");
                                            amount1.setTextColor(Color.parseColor("#9D1D1D"));
                                            pay_btn.setText("افزایش اعتبار");
                                            pay_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    pay_border.setVisibility(View.GONE);
                                                    startActivity(new Intent(CedarMapActivity.this, PayActivity.class));

                                                }
                                            });

                                        } else if (data.getDouble("money") >= service_price) {
                                            amount1.setText(data.getDouble("money") + "ريال");
                                            amount1.setTextColor(Color.parseColor("#ADF200"));
                                            pay_btn.setText("پرداخت اعتباری");
                                            pay_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    socket.emit("complete_payment", token, driverMobile, s_id);
                                                    Finalepay = true;
                                                    pay_border.setVisibility(View.GONE);
                                                }
                                            });

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });


                        }
                    });


                }
            });
            cash_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount1.setVisibility(View.VISIBLE);
                    amount2.setVisibility(View.VISIBLE);
                    stock_amount.setVisibility(View.VISIBLE);
                    sprice.setVisibility(View.VISIBLE);
                    cash_pay.setTextColor(getResources().getColor(R.color.khakestari));
                    epay.setTextColor(getResources().getColor(R.color.zard));
                    sprice.setText("هزینه ی سفر:");
                    amount2.setText(service_price + "ريال");
                    stock_amount.setText("هزینه ی قابل پرداخت:");
                    amount1.setText(service_price + "ريال");
                    pay_btn.setText("پرداخت نقدی");
                    pay_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SharedPreferences Taxi_user_datas = getSharedPreferences("Taxi_user_datas", MODE_PRIVATE);
                            String token = Taxi_user_datas.getString("token", "null");

                            socket.emit("complete_payment1", token, driverMobile, s_id);
                            pay_border.setVisibility(View.GONE);
                            Finalepay = false;


                        }
                    });

                }
            });


        }
    }

    public void set_error_final_service_price() {
        set_latlng1 = 1;
        set_latlng2 = 1;
        m2.remove();
        Toast.makeText(CedarMapActivity.this, "خطا در محاسبه هزینه ..", Toast.LENGTH_SHORT).show();
        com.mapbox.mapboxsdk.geometry.LatLng latLng = new com.mapbox.mapboxsdk.geometry.LatLng(m1.getPosition().getLatitude(), m1.getPosition().getLongitude());
        mMapboxMap.setMaxZoomPreference(16);
        mMapboxMap.moveCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngZoom(latLng, 16));
        car_count.setText("انتخاب مبدا");
        remover_car_marker = 0;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        startService(new Intent(getApplicationContext(), IService.class));
    }

//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//    }

    public void horn_play(View view) {

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.car_sound);
        mp.start();
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
