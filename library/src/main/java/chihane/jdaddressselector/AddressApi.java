package chihane.jdaddressselector;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;


/**
 * Created by flame on 2017/6/20.
 */

public class AddressApi {

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
                tmp.id=mProvinces.get(i).id;
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
                tmp.id=cities.get(i).id;
                cityList.add(tmp);
            }
            return cityList;
        }
        return null;
    }

    //public

    public List<County> getCountries(int index) {
        ProvinceData province=mProvinces.get(selectProvince);
        List<AreaData> area=province.city.get(index).area;
        if(area!=null){
            List<County> counties=new ArrayList<>();
            for(int i=0;i<area.size();i++){
                County county=new County();
                county.name=area.get(i).name;
                county.city_id=index;
                county.id=area.get(i).id;
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
            return jsonToArrayList(inputStreamReader, ProvinceData.class);
        } catch (IOException e) {
            //KLog.i(e);
        }
        return null;
    }


    public static <T> ArrayList<T> jsonToArrayList(InputStreamReader inputStreamReader, Class<T> clazz)
    {
        Type type = new TypeToken<ArrayList<JsonObject>>() {}.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(inputStreamReader, type);
        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects)
        {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
            Log.d("ADD",jsonObject.toString());

        }
        return arrayList;
    }

    static class ProvinceData {
        int id;
        String name;
        List<CityData> city;
    }

    static class CityData {
        int id;
        String name;
        List<AreaData> area;
    }
    static class AreaData{
        int id;
        String name;
    }

}
