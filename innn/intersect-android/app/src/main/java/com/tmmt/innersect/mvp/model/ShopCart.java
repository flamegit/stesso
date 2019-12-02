package com.tmmt.innersect.mvp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flame on 2017/5/11.
 */

public class ShopCart {
    private static ShopCart mInstance = new ShopCart();
    List<ShopItem> mContent;
    public String headText;
    public String headDesc;
    public String headSchema;
    private boolean refresh;
    public boolean isRefresh() {
        return refresh;
    }
    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }
    private ShopCart() {
        mContent = new ArrayList<>();
        refresh = true;
    }
    public void updateQuantity(int position, int quantity) {
        mContent.get(position).quantity = quantity;
    }

    public void addItems(List<ShopItem> items) {
        if (!mContent.isEmpty()) {
            mContent.clear();
        }
        mContent.addAll(items);
    }

    public String getShop(){
        return getSelect().get(0).shop;
    }

    public ShopItem getItem(int index) {
        return mContent.get(index);
    }
    public void clear() {
        mContent.clear();
        headDesc=null;
        headSchema=null;
        headText=null;
    }
    public boolean isEmpty() {
        return mContent.isEmpty();
    }

    public int getCount() {
        return mContent.size();
    }

    public int getAvailabeCount() {
        int count = 0;
        for (ShopItem item : mContent) {
            if(!item.isAvailabel()){
                count++;
            }
        }
        return count;
    }

    public int getCommodityCount() {
        int count = 0;
        for (ShopItem item : mContent) {
            count += item.quantity;
        }
        return count;
    }
    public List<ShopItem> getSelect() {
        List<ShopItem> list = new ArrayList<>();
        for (ShopItem item : mContent) {
            if (item.payment) {
                list.add(item);
            }
        }
        return list;
    }

    public int getStatus(){
        int status=3;
        boolean include=false;
        int itemStatus=0;
        for (ShopItem item : mContent) {
            if (item.payment) {
                itemStatus=item.getAvailabeStatus();
                if(itemStatus==3){
                    include=true;
                }
                status&=itemStatus;
            }
        }

        if(status==1 && include){
            return 5;
        }
        if(status==2 && include){
            return 6;
        }
        return status;
    }

    public int getRemoveCount() {
        int count = 0;
        for (ShopItem item : mContent) {
            if (item.delete) {
                count++;
            }
        }
        return count;
    }

    public int getSelectCount() {
        int count = 0;
        for (ShopItem item : mContent) {
            if (item.payment) {
                count++;
            }
        }
        return count;
    }

    public BigDecimal getSelectPrice() {
        BigDecimal price = new BigDecimal(0);
        for (ShopItem item : mContent) {
            if (item.payment) {
                price = price.add(new BigDecimal(Float.toString(item.price)).multiply(new BigDecimal(item.quantity)));
            }
        }
        return price;
    }
    public static ShopCart getInstance() {
        return mInstance;
    }

    public List<ShopItem> getContent() {
        return mContent;
    }

    public void setSelected(int position, boolean isSelect) {
        mContent.get(position).payment = isSelect;
    }

    public void selectAll(boolean isChecked) {
        for (ShopItem item : mContent) {
            if(!item.isAvailabel()){
                item.payment = isChecked;
            }
        }
    }

    public void removeAll(boolean isChecked) {
        for (ShopItem item : mContent) {
            item.delete = isChecked;
        }
    }

    public void setDelete(int position, boolean isDelete) {
        mContent.get(position).delete = isDelete;
    }

    public void delete(int position){
        mContent.remove(position);
    }

    public void removeSelect() {
        ArrayList<ShopItem> list = new ArrayList<>();
        for (ShopItem item : mContent) {
            if (item.delete) {
                list.add(item);
            }
        }
        if (list.isEmpty()) {
            return;
        }
        mContent.removeAll(list);
    }

    public List<ShopItem> getRemoveItems() {
        ArrayList<ShopItem> list = new ArrayList<>();
        for (ShopItem item : mContent) {
            if (item.delete) {
                list.add(item);
            }
        }
        return list;
    }
    public void add(ShopItem item) {
        mContent.add(item);
    }
}
