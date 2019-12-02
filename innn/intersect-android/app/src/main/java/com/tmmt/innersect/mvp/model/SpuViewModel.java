package com.tmmt.innersect.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by flame on 2017/5/4.
 */

public  class SpuViewModel {

    public String msg;
    public int code;
    public Data data;

    public static class Data{
        private Map<String, Group> mGroupMap;
        public List<ColorInfo> sku_color;
        public List<SizeInfo> sku_size;
        //TicketMap skuValGroupMap;
        public List<Group> skuValGroups;
        public Group defaultValGroup;

        public void parseSpuInfo() {
            mGroupMap = new TreeMap<>();
            for (SpuViewModel.Group group : skuValGroups) {
                mGroupMap.put(group.skuValIds, group);
            }
            defaultValGroup.availableCount=0;
        }
        public SpuViewModel.Group getGroup(int colorIndex, int sizeIndex) {
            if(sku_color==null ||sku_size==null){
                return defaultValGroup;
            }
            String size=sku_size.get(sizeIndex).skuValId;
            String color=sku_color.get(colorIndex).skuValId;
            String key = color + "-" + size;
            if (mGroupMap != null) {
                SpuViewModel.Group group=mGroupMap.get(key);
                if(group!=null){
                    return group;
                }
            }
            return defaultValGroup;
        }

        public boolean isSaleOut(){
            for(Group group:skuValGroups){
                if(group.getMinCount()>0){
                    return false;
                }
            }
            return true;
        }

        public float getPrice(){
            return defaultValGroup.salePrice;
        }

        public float getOriginPrice(){
            return defaultValGroup.originalPrice;
        }

//        public List<Group> getTicket(int index){
//            List<SizeInfo> skuDate= skuValGroupMap.sku_date;
//            List<Group> groups=new ArrayList<>();
//            if(skuDate!=null &&skuValGroups!=null&& skuDate.size()>index){
//                String id=skuDate.get(index).skuValId;
//                KLog.d(id);
//                for(int i=0;i<skuValGroups.size();i++){
//                    if(skuValGroups.get(i).skuValIds.startsWith(id)){
//                        groups.add(skuValGroups.get(i));
//                    }
//                }
//                return groups;
//            }
//            return null;
//        }
    }

    public static class ColorInfo{
        public String skuValId;
        public String displayName;
        public String imgUrl;
        public String valdesc;
        public int availableCount;
        public int pavailableCount;
    }

    public static class SizeInfo {
        public String skuValId;
        public String displayName;
        public int availableCount;
    }

    public static class Group implements Parcelable{
        public long skuId;
        public long productId;
        public String skuName;
        public String omsProductId;
        public String omsSkuId;
        public String thumbnail;
        public String skuValIds;
        public float originalPrice;
        public float salePrice;
        public int status;
        public int order;
        public int availableCount; //库存
        public int maxAvailableCount;
        public String giftInfo;
        public int quantity;
        public String date;
        public int pavailableCount;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(skuId);
            dest.writeLong(productId);
            dest.writeString(skuName);
            dest.writeString(omsProductId);
            dest.writeString(omsSkuId);
            dest.writeString(thumbnail);
            dest.writeString(skuValIds);
            dest.writeFloat(salePrice);
            dest.writeInt(quantity);
        }

        public int getMinCount(){
            int count=Math.max(availableCount,pavailableCount);
            return Math.min(count,maxAvailableCount);
        }

        public String getSkuName(){
            if(skuName!=null){
                if(skuName.indexOf("：")!=-1){
                    return skuName.split("：")[1];
                }
            }
            return skuName;

        }
        public Group(){}

        public Group(Parcel in){
            skuId=in.readLong();
            productId=in.readLong();
            skuName=in.readString();
            omsProductId=in.readString();
            omsSkuId=in.readString();
            thumbnail=in.readString();
            skuValIds=in.readString();
            salePrice=in.readFloat();
            quantity=in.readInt();
        }

        public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>()
        {
            public Group createFromParcel(Parcel in)
            {
                return new Group(in);
            }

            public Group[] newArray(int size)
            {
                return new Group[size];
            }
        };
    }


//    public static class TicketMap{
//        List<SizeInfo> sku_date;
//    }

}
