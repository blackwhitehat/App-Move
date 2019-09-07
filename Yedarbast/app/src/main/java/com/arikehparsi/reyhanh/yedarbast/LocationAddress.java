package com.arikehparsi.reyhanh.yedarbast;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LocationAddress
{
    public class results
    {
        @SerializedName("results")
        public List<LocationAddress.Address> result=new ArrayList<>();

        public List<Address> getResult() {
            return result;
        }

        public void setResult(List<Address> result) {
            this.result = result;
        }
    }
    public class Address
    {
        @SerializedName("address_components")
        public List<LocationAddress.address_params > address_components=new ArrayList<>();

        public List<address_params> getAddress_components() {
            return address_components;
        }

        public void setAddress_components(List<address_params> address_components) {
            this.address_components = address_components;
        }
    }
    public class address_params
    {
        @SerializedName("short_name")
        String short_name;

        public String getShort_name() {
            return short_name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }
    }
}
