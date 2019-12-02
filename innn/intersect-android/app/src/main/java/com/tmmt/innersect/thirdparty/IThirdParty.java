package com.tmmt.innersect.thirdparty;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by flame on 2017/4/11.
 */

public interface IThirdParty {
    //void getPlatformInfo(SHARE_MEDIA platform);

    void share();

    void oauthVerify(SHARE_MEDIA SHARE_MEDI);
}
