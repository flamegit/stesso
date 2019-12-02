package com.tmmt.innersect.ui.activity;

import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

public interface Communication {

    void setMobile(String mobile);

    String getMobile();

    void setPrevious(int previous);

    int getPrevious();

    void setThirdParty(SHARE_MEDIA platform, Map<String, String> data);

    SHARE_MEDIA getPlatform();

    Map<String, String> getDate();

    void goToTarget(int target);
}