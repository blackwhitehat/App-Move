package com.arikehparsi.reyhanh.yedarbast;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    //String token = "";

//    @GET("user/test")
//    @Headers("x-access-token:" + token)
//    Call<String> send_data();

    @FormUrlEncoded
    @POST("user/login")
    Call<ServerData.login> login(@Field("mobile") String mobile,@Field("password") String password);

    @FormUrlEncoded
    @POST("user/register")
    Call<ServerData.register> register(@Field("name") String name,
                                       @Field("mobile") String mobile,
                                       @Field("password") String password,
                                       @Field("gender") String gender);
    @FormUrlEncoded
    @POST("user/active_mobile_number")
    Call<ServerData.active> active_mobile_number(@Field("active_code") String name,@Header("x-access-token") String token);

    @FormUrlEncoded
    @POST("driver/near_driver")
    Call<List<ServerData.location_driver>> send_location(@Field("lat") double lat, @Field("lng") double lng);


    @GET("maps/api/directions/json")
    Call<Place.routes> get_directions(@Query("origin") String origin, @Query("destination") String destination, @Query("sensor") String sensor, @Query("mode") String mode);

    @FormUrlEncoded
    @POST("driver/request_driver")
    Call<ServerData.driver_info> request_driver(@Field("lat") double lat, @Field("lng") double lng);

    @GET("maps/api/geocode/json")
    Call<LocationAddress.results> get_directions(@Query("latlng") String latlng  , @Query("sensor") String sensor, @Query("language") String language );

    @GET("user/get_service")
    Call<List<ServerData.get_service>> get_service(@Header("x-access-token") String token);
    @FormUrlEncoded
    @POST("user/set_service1")
    Call<ServerData.set_service1> set_service1(@Field("token")String token,
                                               @Field("driver_mobile")String DriverMobile);

    @FormUrlEncoded
    @POST("get_service_price")
    Call<ServerData.Service_Price> get_service_price(@Field("lat") String lat,@Field("lng") String lng);

    @GET("user/check_running_service")
    Call<ServerData.running_service> running_service(@Header("x-access-token") String token);
    @FormUrlEncoded
    @POST("user/cancel_request")
    Call<ServerData.cancel_request> cancel_request(@Header("x-access-token")String token,@Field("service_id")String service_id);
    @FormUrlEncoded
    @POST("user/")
    Call<ServerData.Off_code> off_code(@Header("x-access-token") String token
                                        ,@Field("offcode")String offcode);

}
