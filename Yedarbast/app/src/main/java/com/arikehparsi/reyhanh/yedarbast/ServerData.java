package com.arikehparsi.reyhanh.yedarbast;

import com.google.gson.annotations.SerializedName;

public class ServerData {

    public class register{

        @SerializedName("status")
        String status;
        @SerializedName("message")
        String message;
        @SerializedName("token")
        String token;


        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
    public class set_service1{
        @SerializedName("token")
        String token;
        @SerializedName("DriverMobile")
        String DriverMoible;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getDriverMoible() {
            return DriverMoible;
        }

        public void setDriverMoible(String driverMoible) {
            DriverMoible = driverMoible;
        }
    }

    public class login
    {
        @SerializedName("auth")
        String auth;

        @SerializedName("token")
        String token;

        public String getAuth() {
            return auth;
        }

        public void setAuth(String auth) {
            this.auth = auth;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public class active{

        @SerializedName("status")
        String status;

        @SerializedName("token")
        String token;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
    public class Off_code
    {
        @SerializedName("status")
        String status;
        @SerializedName("message")
        String message;

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
    public class cancel_request
    {
        @SerializedName("token")
        String token;

        @SerializedName("service_status")
        String service_status;

        public String getService_status() {
            return service_status;
        }

        public String getToken() {
            return token;
        }





    }

    public class location_driver {

        @SerializedName("lat")
        String lat;
        @SerializedName("lng")
        String lng;
        @SerializedName("angle")
        String angle;

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getAngle() {
            return angle;
        }

        public void setAngle(String angle) {
            this.angle = angle;
        }
    }
    public class get_service
    {
        @SerializedName("address1")
        String address1;

        @SerializedName("address2")
        String address2;

        @SerializedName("address3")
        String address3;

        @SerializedName("date")
        String date;

        @SerializedName("driving_status")
        int driving_status;

        @SerializedName("status")
        int status;

        @SerializedName("order_id")
        String order_id;

        @SerializedName("lat1")
        double lat1;

        @SerializedName("lat2")
        double lat2;

        @SerializedName("lat3")
        double lat3;

        @SerializedName("_id")
        String _id;

        @SerializedName("driver")
        DriverGata driverGata;

        @SerializedName("going_back")
        String going_back;

        @SerializedName("price")
        String price;

        @SerializedName("total_price")
        String total_price;

        @SerializedName("stop_time")
        String stop_time;


        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getAddress3() {
            return address3;
        }

        public void setAddress3(String address3) {
            this.address3 = address3;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }



        public int getDriving_status() {
            return driving_status;
        }

        public void setDriving_status(int driving_status) {
            this.driving_status = driving_status;
        }



        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }



        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }



        public double getLat1() {
            return lat1;
        }

        public void setLat1(double lat1) {
            this.lat1 = lat1;
        }

        public double getLat2() {
            return lat2;
        }

        public void setLat2(double lat2) {
            this.lat2 = lat2;
        }

        public double getLat3() {
            return lat3;
        }

        public void setLat3(double lat3) {
            this.lat3 = lat3;
        }



        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }



        public DriverGata getDriverGata() {
            return driverGata;
        }

        public void setDriverGata(DriverGata driverGata) {
            this.driverGata = driverGata;
        }



        public String getGoing_back() {
            return going_back;
        }

        public void setGoing_back(String going_back) {
            this.going_back = going_back;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }

        public String getStop_time() {
            return stop_time;
        }

        public void setStop_time(String stop_time) {
            this.stop_time = stop_time;
        }


    }
    public class DriverGata{

        @SerializedName("name")
        String driver_name;

        @SerializedName("mobile")
        String driver_mobile;

        @SerializedName("car_type")
        String car_type;

        public String getDriver_name() {
            return driver_name;
        }

        public void setDriver_name(String driver_name) {
            this.driver_name = driver_name;
        }

        public String getDriver_mobile() {
            return driver_mobile;
        }

        public void setDriver_mobile(String driver_mobile) {
            this.driver_mobile = driver_mobile;
        }

        public String getCar_type() {
            return car_type;
        }

        public void setCar_type(String car_type) {
            this.car_type = car_type;
        }
    }

    public class driver_info{

        @SerializedName("name")
        String name;

        @SerializedName("car_type")
        String car_type;

        @SerializedName("city_code")
        String city_code;

        @SerializedName("number_plates")
        String number_plates;

        @SerializedName("code_number_plates")
        String code_number_plates;

        @SerializedName("city_number")
        String city_number;

        @SerializedName("lat")
        Double lat;

        @SerializedName("lng")
        Double lng;

        @SerializedName("angle")
        int angle;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCar_type() {
            return car_type;
        }

        public void setCar_type(String car_type) {
            this.car_type = car_type;
        }

        public String getCity_code() {
            return city_code;
        }

        public void setCity_code(String city_code) {
            this.city_code = city_code;
        }

        public String getNumber_plates() {
            return number_plates;
        }

        public void setNumber_plates(String number_plates) {
            this.number_plates = number_plates;
        }

        public String getCode_number_plates() {
            return code_number_plates;
        }

        public void setCode_number_plates(String code_number_plates) {
            this.code_number_plates = code_number_plates;
        }

        public String getCity_number() {
            return city_number;
        }

        public void setCity_number(String city_number) {
            this.city_number = city_number;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }

        public int getAngle() {
            return angle;
        }

        public void setAngle(int angle) {
            this.angle = angle;
        }
    }

    public class Service_Price

    {
        @SerializedName("price")
        String price;

        @SerializedName("fixed_price")
        String fixed_price;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getFixed_price() {
            return fixed_price;
        }

        public void setFixed_price(String fixed_price) {
            this.fixed_price = fixed_price;
        }
    }
    public class running_service
    {
        @SerializedName("price")
        String price;
        @SerializedName("status")
        String status;
        @SerializedName("driverName")
        String name_driver;
        @SerializedName("driver_mobile")
        String driver_mobile;
        @SerializedName("photo_url")
        String photo_url;
        @SerializedName("code_number_plates")
        String code_number_plates;
        @SerializedName("service_id")
        String service_id;
        @SerializedName("car_type")
        String car_type;
        @SerializedName("Epay")
        int epay;
        @SerializedName("service_status")
        int service_status;

        public int getEpay() {
            return epay;
        }

        public int getService_status() {
            return service_status;
        }

        public String getCar_type() {
            return car_type;
        }

        public String getName_driver() {
            return name_driver;
        }

        public String getDriver_mobile() {
            return driver_mobile;
        }

        public String getPhoto_url() {
            return photo_url;
        }

        public String getCode_number_plates() {
            return code_number_plates;
        }

        public String getService_id() {
            return service_id;
        }

        public String getCity_code() {
            return city_code;
        }

        public String getCity_number() {
            return city_number;
        }

        @SerializedName("city_code")

        String city_code;
        @SerializedName("city_number")
        String city_number;
        public String getPrice() { return price; }
        public String getStatus() {
            return status;
        }





    }

}
