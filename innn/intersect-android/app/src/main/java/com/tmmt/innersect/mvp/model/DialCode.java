package com.tmmt.innersect.mvp.model;

import com.tmmt.innersect.utils.Util;

import java.util.Locale;

public class DialCode {

    public String name;
    public String dial_code;
    public String code;

    public String getDisplayCountry(){
        Locale loc = new Locale("",code);
        return loc.getDisplayCountry();
    }

    public String getLetter(){
      return Util.getPinYinFirstLetter(getDisplayCountry());
    }

}