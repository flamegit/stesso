package com.tmmt.innersect.utils;

import android.content.Context;

import com.tendcloud.tenddata.TCAgent;
import com.tmmt.innersect.App;
import com.tmmt.innersect.common.AccountInfo;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by flame on 2017/7/4.
 */

public class AnalyticsUtil {

    public static final String LOGIN_MOBILE_SELECTCOUNTRY = "login_mobile_selectcountry";
    public static final String LOGIN_MOBILE = "login_mobile";
    public static final String LOGIN_MOBILE_PASSWORD = "login_mobile_password";
    public static final String LOGIN_MOBILE_VERIFICATIONCODE = "login_mobile_Verificationcode";
    public static final String LOGIN_MOBILE_FORGETPASSWORD = "login_mobile_forgetpassword";
    public static final String LOGIN_MOBILE_FORGETPASSWORD_SETPASSWORD = "login_mobile_forgetpassword_setpassword";
    public static final String LOGIN_QUICKLOG_AUTHORIZATION = "login_quicklog_authorization";
    public static final String LOGIN_QUICKLOG_BINDMOBILE = "login_quicklog_bindmobile";
    public static final String VLONE_PREVIEW = "vlone_preview";
    public static final String VLONE_PREVIEW_PLAYVIDEO = "vlone_preview_playvideo";
    public static final String VLONE_PREVIEW_MANUAL = "vlone_preview_manual";
    public static final String VLONE_SELECTSHOP_ENTER = "vlone_selectshop_enter";
    public static final String VLONE_SELECTSHOP_CHOOSE = "vlone_selectshop_choose";
    public static final String VLONE_SHOP_MY = "vlone_shop_my";
    public static final String VLONE_SHOP_SELECTSHOP = "vlone_shop_selectshop";
    public static final String VLONE_SHOP_SHOPPINGCAR = "vlone_shop_shoppingcar";
    public static final String VLONE_SHOP_MANUAL = "vlone_shop_manual";
    public static final String VLONE_SHOP_GODRAW = "vlone_shop_godraw";
    public static final String VLONE_SHOP_PRODUCT_CLICK = "vlone_shop_product_click";
    public static final String LOGIN_GUIDE_CONFIRM = "login_guide_confirm";
    public static final String LOGIN_GUIDE_CANCEL = "login_guide_cancel";
    public static final String PRODUCT_SELECTSIZE_CLICK = "product_selectsize_click";
    public static final String PRODUCT_ADD = "product_add";
    public static final String SHOPPINGCAR_GOSHOPPING = "shoppingcar_goshopping";
    public static final String SHOPPINGCAR_EDIT = "shoppingcar_edit";
    public static final String SHOPPINGCAR_EDIT_DONE = "shoppingcar_edit_done";
    public static final String SHOPPINGCAR_EDIT_DELETE = "shoppingcar_edit_delete";
    public static final String SHOPPINGCAR_EDIT_NUMBER_ADD = "shoppingcar_edit_number_add";
    public static final String SHOPPINGCAR_EDIT_NUMBER_MIN = "shoppingcar_edit_number_min";
    public static final String SHOPPINGCAR_NEWADDRESS = "shoppingcar_newaddress";
    public static final String SHOPPINGCAR_NEWADDRESS_DONE = "shoppingcar_newaddress_done";
    public static final String SHOPPINGCAR_ADDRESS_CLICK = "shoppingcar_address_click";
    public static final String SHOPPINGCAR_ADDRESS_SELECT = "shoppingcar_address_select";
    public static final String SHOPPINGCAR_ADDRESS_EDIT = "shoppingcar_address_edit";
    public static final String SHOPPINGCAR_PRODUCT_CLICK = "shoppingcar_product_click";
    public static final String SHOPPINGCAR_CHECKOUT = "shoppingcar_checkout";
    public static final String DRAW_CLICK = "draw_click";
    public static final String DRAW_NOCHANCE_CONFIRM = "draw_nochance_confirm";
    public static final String DRAW_MYTICKET = "draw_myticket";


    public static final String DRAW_MYTICKET_QRCODE = "draw_myticket_qrcode";
    public static final String MY_ACCOUNT = "my_account";
    public static final String MY_ORDER = "my_order";
    public static final String MY_ADDRESS = "my_address";
    public static final String MY_SHOPPINGNOTICE = "my_shoppingnotice";
    public static final String MY_SETTING = "my_setting";
    public static final String MY_SHARE = "my_share";
    public static final String MY_SHARE_CHANNEL = "my_share_channel";
    public static final String MY_SHARE_CANCEL = "my_share_cancel";
    public static final String ACCOUNT_NICKNAME_SAVE = "account_nickname_save";
    public static final String ACCOUNT_PASSWORD_SAVE = "account_password_save";
    public static final String ACCOUNT_THIRDPARTY_BIND = "account_thirdparty_bind";
    public static final String ACCOUNT_THIRDPARTY_RELIEVE = "account_thirdparty_relieve";
    public static final String ORDER_ORDERDETAILS_CLICK = "order_orderdetails_click";
    public static final String ORDER_ORDERDETAILS_SHARE_ORDER = "order_orderdetails_share_order";
    public static final String ORDER_ORDERDETAILS_AFTERSALE = "order_orderdetails_aftersale";
    public static final String ADDRESS_NEWADDRESS_ADD = "address_newaddress_add";
    public static final String ADDRESS_NEWADDRESS_SAVE = "address_newaddress_save";
    public static final String ADDRESS_EDIT = "address_edit";
    public static final String SETTING_OPENPUSH = "setting_openpush";
    public static final String SETTING_CLOSEPUSH = "setting_closepush";
    public static final String SETTING_FEEDBACK = "setting_feedback";
    public static final String SETTING_FEEDBACK_SUBMIT = "setting_feedback_submit";
    public static final String SETTING_CLEARCACHE = "setting_clearcache";
    public static final String SETTING_ABOUT = "setting_about";
    public static final String ACCOUNT_LOGOU = "account_logou";
    public static final String ACCOUNT_LOGOUT_CONFIRM = "account_logout_confirm";
    public static final String ACCOUNT_LOGOUT_CANCEL = "account_logout_cancel";
    public static final String POPUPWINDOW_GOOD = "popupwindow_good";

    public static final String POPUPWINDOW_NEGATIVE = "popupwindow_negative";
    public static final String POPUPWINDOW_NEGATIVE_SUBMIT = "popupwindow_negative_submit";
    public static final String POPUPWINDOW_CANCEL = "popupwindow_cancel";
    public static final String PUSH_ACTIVITY = "push_activity";
    public static final String PUSH_SHARE_VLONE_RANK = "push_share_vlone_rank";
    public static final String UPDATE_NORMAL_CONFIRM = "update_normal_confirm";
    public static final String UPDATE_NORMAL_CANCEL = "update_normal_cancel";
    public static final String UPDATE_FORCE_CONFIRM = "update_force_confirm";


    static String deviceId=SystemUtil.getIMEI();
    static String LABEL="VLONE";

    public static void reportEvent(String eventId,String key,String value) {

        AccountInfo account= AccountInfo.getInstance();
        Map<String,String> param=new HashMap<>();
        param.put("DeviceID",deviceId);
        param.put("time",String.valueOf(System.currentTimeMillis()));

        if(account.isLogin()){
            param.put("UUID",account.getUserId());
        }
        param.put(key,value);
        Context context= App.getAppContext();
        MobclickAgent.onEvent(context, eventId, param);
        TCAgent.onEvent(context,eventId,LABEL,param);
    }

    public static void reportEvent(String eventId,Map map) {

        AccountInfo account= AccountInfo.getInstance();
        Map<String,String> param=new HashMap<>();
        param.put("DeviceID",deviceId);
        param.put("time",String.valueOf(System.currentTimeMillis()));

        if(account.isLogin()){
            param.put("UUID",account.getUserId());
        }

        param.putAll(map);
        Context context= App.getAppContext();
        MobclickAgent.onEvent(context, eventId, param);
        TCAgent.onEvent(context,eventId,LABEL,param);
    }

    public static void reportEvent(String eventId) {

        AccountInfo account= AccountInfo.getInstance();
        Map<String,String> param=new HashMap<>();
        param.put("DeviceID",deviceId);
        param.put("time",String.valueOf(System.currentTimeMillis()));

        if(account.isLogin()){
            param.put("UUID",account.getUserId());
        }
        Context context= App.getAppContext();
        MobclickAgent.onEvent(context, eventId, param);
        TCAgent.onEvent(context,eventId,LABEL,param);
    }
}
