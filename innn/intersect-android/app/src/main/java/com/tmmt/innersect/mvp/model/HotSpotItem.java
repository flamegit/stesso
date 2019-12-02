package com.tmmt.innersect.mvp.model;

import java.util.List;

/**
 * Created by flame on 2017/9/19.
 */

public class HotSpotItem {

   public Common brands;
   public Common category;
   public List<String> hot;


    public static class SearchDate{
        public String schema;
        public String picUrl;
        public String name;
    }


    public static class Common extends AdapterItem{
        public String more;
        public List<SearchDate> content;
    }

}
