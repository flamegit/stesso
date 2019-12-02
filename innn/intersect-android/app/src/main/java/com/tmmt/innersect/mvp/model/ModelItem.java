package com.tmmt.innersect.mvp.model;

import com.tmmt.innersect.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flame on 2017/11/24.
 */

public class ModelItem extends BaseItem{

    public String more;
    public String subtitle;
    public String title;
    public List<CommonItem> content;

    public ModelItem(){
        mSpanSize=3;
    }

//    public List<BaseItem> getItem(){
//        List<BaseItem> list=null;
//        if("discounts".equals(type)){
//            list=new ArrayList<>();
//            CommonItem item=content.get(0);
//            CommodityTitle commodityTitle=new CommodityTitle();
//            commodityTitle.headerBgUrl=item.headerBgUrl;
//            commodityTitle.headerText=item.headerText;
//            commodityTitle.title=title;
//            commodityTitle.more=more;
//            commodityTitle.type="discounts_title";
//            list.add(commodityTitle);
//            if(item.plist!=null){
//                for(CommodityItem2 commodity: item.plist){
//                    commodity.type=type;
//                    list.add(commodity);
//                }
//
//                MoreItem moreItem=new MoreItem();
//                moreItem.type="show_all";
//                moreItem.more=more;
//                list.add(moreItem);
//            }
//        }
//        return list;
//    }


    public List<BaseItem> getItem(){
        List<BaseItem> list=null;
        if("discounts".equals(type)){
            list=new ArrayList<>();
            CommonItem item=content.get(0);

            CommodityTitle commodityTitle=new CommodityTitle();
            commodityTitle.headerBgUrl=item.headerBgUrl;
            commodityTitle.headerText=item.headerText;
            commodityTitle.headerSubText=item.headerSubText;
            commodityTitle.title=title;
            commodityTitle.subtitle=subtitle;
            commodityTitle.more=more;
            commodityTitle.type="discounts_title";
            list.add(commodityTitle);

            if(item.plist!=null && !item.plist.isEmpty()){

                if(!Util.isNotEmpty(title)){
                    commodityTitle.title="  ";
                }
                for(CommodityItem2 commodity: item.plist){
                    commodity.type=type;
                    list.add(commodity);
                }
                MoreItem moreItem=new MoreItem();
                moreItem.type="show_all";
                moreItem.more=more;
                list.add(moreItem);
            }
        }
        return list;
    }
}
