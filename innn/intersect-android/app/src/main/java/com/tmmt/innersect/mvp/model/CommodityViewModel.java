package com.tmmt.innersect.mvp.model;

import android.util.SparseArray;

import com.tmmt.innersect.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flame on 2017/5/4.
 * detail
 */

public  class CommodityViewModel {
    public String msg;
    public int code;
    public Commodity data;

    public SparseArray<ImageDetail> imageGroup;

    public ImageDetail getHeadImages(int index) {
        if (imageGroup == null) {
            imageGroup = new SparseArray<>();
        }
        ImageDetail imageInfos = imageGroup.get(index);
        if (imageInfos != null) {
            return imageInfos;
        }
        if (index == 0) {
            ImageDetail first = new ImageDetail();
            first.headPics = data.headPics;
            first.detailInfo = data.detailInfo;
            imageGroup.put(0, first);
            return first;
        }
        return null;
    }

    public void saveImages(int index, ImageDetail imageInfos) {
        if (imageGroup == null) {
            imageGroup = new SparseArray<>();
        }
        imageGroup.put(index, imageInfos);
    }

    public static class Commodity {
        public ArrayList<ImageInfo> headPics;
        public BaseInfo baseInfo;
        public BrandInfo brand;
        public UseInfo size;
        public UseInfo styleDesc;
        public List<ImageInfo> detailInfo;
        public List<ColorInfo> skuColors;
        public String couponCDKey;
        public String couponName;
        public boolean isShowShareButton;

        //public List<QA> qa;
        public void parse(int first) {
            if (Util.isNotEmpty(baseInfo.desc)) {
                if (detailInfo == null) {
                    detailInfo = new ArrayList<>();
                }
                ImageInfo info = new ImageInfo();
                info.desc = baseInfo.desc;
                detailInfo.add(0, info);
            }
            //relist(first);
        }

        public String getCommodityInfo() {
            StringBuilder stringBuilder = new StringBuilder();
            if (Util.isNotEmpty(baseInfo.countryName)) {
                stringBuilder.append("产地：" + baseInfo.countryName);
            }

            if (Util.isNotEmpty(styleDesc.desc)) {
                stringBuilder.append("\n" + styleDesc.desc);
            }
            return stringBuilder.toString();
        }

        private void relist(int id) {
            int index = 0;
            for (int i = 0; i < skuColors.size(); i++) {
                if (skuColors.get(i).skuValId == id) {
                    index = i;
                }
            }
            if (index != 0 && index < skuColors.size()) {
                ColorInfo info = skuColors.remove(index);
                skuColors.add(0, info);
            }

        }
    }

        public static class ColorInfo {
            public String name;
            public String shadeUrl;
            public int skuId;
            public int skuValId;
        }

        public static class BaseInfo {
            public int productId;
            public String name;
            String sellerNo;
            String desc;
            String countryName;
            String fabric;
            int status;
        }

        public static class BrandInfo {
            public long brandId;
            public String schema;
            public String logoUrl;
            public String name;
        }

        //size and wash
        public static class UseInfo {
            public String desc;
            public String imgUrl;
        }

//    public static class QA{
//        public String q;
//        public String a;
//    }

}

