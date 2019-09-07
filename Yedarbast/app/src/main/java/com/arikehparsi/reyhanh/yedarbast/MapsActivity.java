package com.arikehparsi.reyhanh.yedarbast;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.List;

import Lib.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    Marker m2;
    LatLng latLng1;
    LatLng latLng2;
    int set_latlng1 = 1;
    int set_latlng2 = 1;
    String m2_id;
    Marker m1;
    Marker[] car_marker;
    int remover_car_marker = 0;

    //access to the location
    LocationManager locationManager;

    String baseUrl = "http://192.168.1.103:3000/";

    //drawerLayout
    ActionBarDrawerToggle actionBarDrawerToggle;

    TextView car_count;

    RelativeLayout count_box, price_box;

    TextView toolbar_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fonts
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.arrow_color));

        car_count = findViewById(R.id.car_count);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        count_box = findViewById(R.id.count_box);
        price_box = findViewById(R.id.price_box);
        toolbar_text = findViewById(R.id.toolbar_text);

        if (CheckGPS()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                }, 101);
            } else {
                set_location();
            }
        } else {

            show_dialog_box();
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        //zoom controller
        //mMap.getUiSettings().setZoomControlsEnabled(true);

        //maximum or minimum of zoom
        //mMap.setMinZoomPreference(14);

        // Add a marker in Sydney and move the camera
        LatLng tehran = new LatLng(35.6892, 51.3890);

        //mMap.addMarker(new MarkerOptions().position(tehran).title("Marker in Tehran"));

        MarkerOptions markerOptions = new MarkerOptions().position(tehran)
                .title("مبدا").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_first));

        //Destination Marker
        final MarkerOptions markerOptions2 = new MarkerOptions().position(tehran)
                .title("مقصد").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_second));

        m1 = mMap.addMarker(markerOptions);
        final String m1_id = m1.getId();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //MY Location Marker
            mMap.setMyLocationEnabled(true);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tehran, 16));
        //Moving the marker
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                Log.i("getCameraPosition", String.valueOf(googleMap.getCameraPosition()));
                LatLng position = googleMap.getCameraPosition().target;
                //marker for default size
                int height = 100;
                int width = 100;

                if (set_latlng1 == 1) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.map_marker_first);
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    Bitmap small = Bitmap.createScaledBitmap(bitmap, width, height, true);

                    m1.setIcon(BitmapDescriptorFactory.fromBitmap(small));
                    m1.setPosition(position);
                } else {
                    if (set_latlng2 == 1) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.map_marker_second);
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Bitmap small = Bitmap.createScaledBitmap(bitmap, width, height, true);

                        m2.setIcon(BitmapDescriptorFactory.fromBitmap(small));

                        m2.setPosition(position);
                    }
                }


            }
        });

        //return the size of marker
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng position = googleMap.getCameraPosition().target;
                m1.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_first));
                if (m2 != null) {
                    m2.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_second));
                }

                if (set_latlng1 == 1){

                    get_driver(position.latitude, position.longitude);

                }


            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if (marker.getId().equals((m1_id))) {
                    latLng1 = marker.getPosition();
                    m2 = mMap.addMarker(markerOptions2);
                    m2.setZIndex(1);
                    m2.setPosition(marker.getPosition());
                    m2_id = m2.getId();
                    set_latlng1 = 0;
                }
                if (marker.getId().equals(m2_id)) {
                    set_latlng2 = 0;
                    latLng2 = marker.getPosition();

                    mMap.setMinZoomPreference(14);
                    mMap.setMaxZoomPreference(14);

                    get_directions();
                }


                return true;
            }
        });

        //default MY Location Mark for designing
        FloatingActionButton my_location = findViewById(R.id.myLocation);
        my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckGPS()) {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    Location last_location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    LatLng user_location = new LatLng(last_location.getLatitude(), last_location.getLongitude());

                    if(user_location != null){

                        m1.setPosition(user_location);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user_location, 16));

                    }
                    else {
                        set_location();
                    }

                }else {
                    show_dialog_box();
                }
            }
        });
    }

    public void set_location() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);


    }

    @Override
    public void onLocationChanged(Location location) {

//        String a = location.getLatitude() + "," + location.getLongitude();
//        Toast.makeText(this, a, Toast.LENGTH_LONG).show();
        if (location.getProvider().equals("gps")){

            locationManager.removeUpdates(this);
        }
        LatLng user_location = new LatLng(location.getLatitude(), location.getLongitude());
        m1.setPosition(user_location);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user_location, 16));

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

    //is the gps working or not?
    public boolean CheckGPS(){

        boolean status;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return status;
    }

    //shoe the message of GPS on or off
    public void show_dialog_box (){
        //show the message of turning on the location
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.location_message_r);
        dialog.show();
    }

    //fonts method
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    public void get_driver(double lat, double lng){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Service.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //interface
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<ServerData.location_driver>> call = apiInterface.send_location(lat, lng);

        Callback<List<ServerData.location_driver>> callback = new Callback<List<ServerData.location_driver>>() {
            @Override
            public void onResponse(Call<List<ServerData.location_driver>> call, Response<List<ServerData.location_driver>> response) {

                if (response.isSuccessful()){

                    if (remover_car_marker == 1){

                        for (int j = 0; j < car_marker.length; j++){

                            Marker c = car_marker[j];
                            c.remove();

                        }
                    }


                    BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.car_icon);
                    Bitmap bitmap = bitmapDrawable.getBitmap();

                    int i = 0;
                    car_marker = new Marker[response.body().size()];
                    String s = response.body().size() + " تاکسی موجود";

                    car_count.setText(s);

                    remover_car_marker = 1;


                    for (ServerData.location_driver location_driver : response.body()){

                        Matrix matrix = new Matrix();
                        matrix.postRotate(Float.valueOf(location_driver.getAngle()));
                        Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                        LatLng tehran = new LatLng(Double.valueOf(location_driver.getLat()), Double.valueOf(location_driver.getLng()));

                        final MarkerOptions markerOptions2 = new MarkerOptions().position(tehran)
                                .icon(BitmapDescriptorFactory.fromBitmap(b));

                        Marker car = mMap.addMarker(markerOptions2);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void get_directions(){

        toolbar_text.setText("سفر سلامت");

        final DecimalFormat decimalFormat = new DecimalFormat("###,###");

        car_count.setText("در حال محاسبه هزینه ...");

        String baseUrl = "https://maps.googleapis.com/";

        String origin = latLng1.latitude + "," + latLng1.longitude;
        String destination = latLng2.latitude + "," + latLng2.longitude;


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Service.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //interface
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<Place.routes> call = apiInterface.get_directions(origin, destination, "true", "driving");

        Callback<Place.routes> callback = new Callback<Place.routes>() {
            @Override
            public void onResponse(Call<Place.routes> call, Response<Place.routes> response) {

                if (response.isSuccessful()){

                    double a = response.body().getRoutes().get(0).getLegs().get(0).getDistance().getValue();
                    //هزینه ی اولیه
                    int p1 = 15000;
                    double p2 = (a * 5000)/1000;
                    double c = p2/5000;
                    c = Math.round(c) * 5000;
                    int p3 = (int) c;
                    int p = p1 + p3;
                    //Toast.makeText(MapsActivity.this, String.valueOf(p), Toast.LENGTH_LONG).show();


                    count_box.setVisibility(View.GONE);
                    price_box.setVisibility(View.VISIBLE);

                    TextView price = findViewById(R.id.price);
                    String p_string = decimalFormat.format(p) + " ريال";
                    price.setText(p_string);

                }

            }

            @Override
            public void onFailure(Call<Place.routes> call, Throwable t) {

            }
        };


        call.enqueue(callback);

    }

    public void request_driver(View view){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //interface
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ServerData.driver_info> call = apiInterface.request_driver(latLng1.latitude, latLng1.longitude);

        Callback<ServerData.driver_info> callback = new Callback<ServerData.driver_info>() {
            @Override
            public void onResponse(Call<ServerData.driver_info> call, Response<ServerData.driver_info> response) {

                if (response.isSuccessful()){

                    price_box.setVisibility(View.GONE);


                    ImageView close_service = findViewById(R.id.close_service);
                    close_service.setVisibility(View.VISIBLE);
                    ImageView search = findViewById(R.id.search);
                    close_service.setVisibility(View.GONE);

                    RelativeLayout driver_info = findViewById(R.id.driver_info);
                    driver_info.setVisibility(View.VISIBLE);

                    TextView driver_name = findViewById(R.id.driver_name);
                    TextView city_number = findViewById(R.id.city_number);
                    TextView city_code = findViewById(R.id.city_code);
                    TextView number_plates = findViewById(R.id.number_plates);
                    TextView code_number_plates = findViewById(R.id.code_number_plates);

                    driver_name.setText(response.body().getName());
                    city_number.setText(response.body().getCity_number());
                    city_code.setText(response.body().getCity_code());
                    number_plates.setText(response.body().getNumber_plates());
                    code_number_plates.setText(response.body().getCode_number_plates());


                    BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.car_icon);
                    Bitmap bitmap = bitmapDrawable.getBitmap();

                    Matrix matrix = new Matrix();
                    matrix.postRotate(response.body().getAngle());
                    Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                    LatLng tehran = new LatLng(response.body().getLat(), response.body().getLng());

                    final MarkerOptions markerOptions2 = new MarkerOptions().position(tehran)
                            .icon(BitmapDescriptorFactory.fromBitmap(b));

                    Marker car = mMap.addMarker(markerOptions2);


                }else {

                }

            }

            @Override
            public void onFailure(Call<ServerData.driver_info> call, Throwable t) {

            }
        };

        call.enqueue(callback);
    }


    public void show_hidden_panel(View view){

        LinearLayout hidden_panel = findViewById(R.id.hidden_panel);
        Animation up = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.up);
        hidden_panel.startAnimation(up);
        hidden_panel.setVisibility(View.VISIBLE);

    }
}
