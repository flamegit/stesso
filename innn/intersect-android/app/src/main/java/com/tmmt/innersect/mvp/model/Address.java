package com.tmmt.innersect.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by flame on 2017/5/2.
 */

public class Address implements Parcelable{

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return mobile;
    }

    public void setTel(String tel) {
        this.mobile = tel;
    }

    public String getCity() {
        StringBuilder stringBuilder=new StringBuilder();
        if(provinceName!=null){
            stringBuilder.append(provinceName+" ");
        }
        if(cityName!=null){
            stringBuilder.append(cityName+" ");
        }
        if(countyName!=null){
            stringBuilder.append(countyName+" ");
        }
        return stringBuilder.toString();
    }

    public void setCity(String city,int id) {
        cityName = city;
        cityId=id;
    }

    public String getDetail() {
        return getCity()+addressDetail;
    }

    public String getStreet(){
        return addressDetail;
    }


    public void setDetail(String detail) {
        this.addressDetail = detail;
    }

    public boolean isDefault() {
        return isDefault==1;
    }

    public void setUserId(String userId){
        this.userId=userId;
    }

    public void setDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public void setCounty(String county,int id){
        countyName=county;
        countyId=id;
    }

    public void setProvince(String province,int id){
        provinceName=province;
        provinceId=id;
    }

    @SerializedName(value = "id", alternate = {"deliveryAddressId"})
    int id;
    @SerializedName(value = "addressDetail", alternate = {"address"})
    String addressDetail;
    public int cityId;

    @SerializedName(value = "cityName", alternate = {"city"})
    String cityName;

    String countryCode;

    @SerializedName(value = "countyName", alternate = {"county"})
    String countyName;

    public int countyId;
    int isDefault;

    String mobile;

    @SerializedName(value = "name", alternate = {"consignee"})
    String name;

    @SerializedName(value = "provinceName", alternate = {"province"})
    String provinceName;

    public int provinceId;
    String userId;

    public Address(){}

    public int getId(){
        return id;
    }
    public Address(Parcel source){
        id=source.readInt();
        name=source.readString();
        mobile=source.readString();
        provinceName=source.readString();
        cityName=source.readString();
        countyName=source.readString();
        provinceId=source.readInt();
        cityId=source.readInt();
        countyId=source.readInt();
        addressDetail=source.readString();
        isDefault=source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(mobile);
        dest.writeString(provinceName);
        dest.writeString(cityName);
        dest.writeString(countyName);
        dest.writeInt(provinceId);
        dest.writeInt(cityId);
        dest.writeInt(countyId);
        dest.writeString(addressDetail);
        dest.writeInt(isDefault);
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>()
    {
        public Address createFromParcel(Parcel in)
        {
            return new Address(in);
        }
        public Address[] newArray(int size)
        {
            return new Address[size];
        }
    };
}
