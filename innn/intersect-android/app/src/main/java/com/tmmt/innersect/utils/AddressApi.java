package com.tmmt.innersect.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.socks.library.KLog;
import com.tmmt.innersect.App;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;


/**
 * Created by flame on 2017/6/20.
 */

public class AddressApi {

    private static final String ADDRESS_DATA_SP="address_data_sp";
    private static final String ADDRESS_DATA="address_data";

    public String getAddressData(){
        SharedPreferences sp= App.getAppContext().getSharedPreferences(ADDRESS_DATA_SP, Context.MODE_PRIVATE);
        return sp.getString(ADDRESS_DATA,null);
    }

    public static void saveAddressData(String  address){
        SharedPreferences sp= App.getAppContext().getSharedPreferences(ADDRESS_DATA_SP, Context.MODE_PRIVATE);
        sp.edit().putString(ADDRESS_DATA,address).apply();

    }


    List<ProvinceData> mProvinces;
    Context mContext;

    private int selectProvince;

    public List<Province> getProvinces() {
        if(mProvinces==null){
            mProvinces=parse();
        }
        if(mProvinces!=null){
            List<Province> provinces=new ArrayList<>();
            for(int i=0;i<mProvinces.size();i++){
                Province tmp=new Province();
                tmp.name=mProvinces.get(i).name;
                tmp.id=i;
                provinces.add(tmp);
            }
            return provinces;
        }
        return null;
    }


    public List<City> getCities(int index) {
        selectProvince=index;
        List<CityData> cities=mProvinces.get(index).city;
        if(cities!=null){
            List<City> cityList=new ArrayList<>();
            for(int i=0;i<cities.size();i++){
                City tmp=new City();
                tmp.name=cities.get(i).name;
                tmp.province_id=index;
                tmp.id=i;
                cityList.add(tmp);
            }
            return cityList;
        }
        return null;
    }


    public List<County> getCountries(int index) {
        ProvinceData province=mProvinces.get(selectProvince);
        List<String> area=province.city.get(index).area;
        if(area!=null){
            List<County> counties=new ArrayList<>();
            for(int i=0;i<area.size();i++){
                County county=new County();
                county.name=area.get(i);
                county.city_id=index;
                counties.add(county);
            }
            return counties;
        }
        return null;
    }

    public AddressApi(Context context) {
        mContext = context;
    }


    private List<ProvinceData> parse() {
        try {
            InputStream in = mContext.getResources().getAssets().open("province.json");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            return Util.jsonToArrayList(inputStreamReader, ProvinceData.class);
        } catch (IOException e) {
            KLog.i(e);
        }
        return null;
    }

    static class ProvinceData {
        String name;
        List<CityData> city;
    }

    static class CityData {
        String name;
        List<String> area;
    }

}
