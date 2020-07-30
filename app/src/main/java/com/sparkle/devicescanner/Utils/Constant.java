package com.sparkle.devicescanner.Utils;


import android.view.View;

/**
 * Created by Admin on 27-04-2018.
 */

public class Constant {
    public static final String GET_REPLY = "GET_REPLY";
    public static final String HAND_START = "HAND_START";
    public static final String LUMN_USB = "LUMN_USB";
    //    public static final String OVCAMP_USB = "OVCAMP_USB";
    public static final String OVCAMP = "OVCAMP";
    public static final String DEVICE_OVCAMP = "DEVICE_OVCAMP";
    public static final String DEVICE_LUMN = "DEVICE_LUMN";
    public static final String NO_OVCAMP_LUMN = "NO_OVCAMP_LUMN";
    public static final String LUMN = "LUMN";
    //    public static final String FIRE_CODE = "FIRE_CODE";
    public static final String ALL_TIME_HAND = "ALL_TIME_HAND";
    public static final String CALL_LUMN_CAMP = "CALL_LUMN_CAMP";
    public static final String ALL_TIME_OVCAMP_CODE = "ALL_TIME_OVCAMP_CODE";
    public static final String OVCAMP_CODE_START = "OVCAMP_CODE_START";
    public static final String GET_PPID = "GET_PPID";
    public static final String DEVICE_TYPE = "DEVICE_TYPE";
    public static final String I_DEVICE_TYPE = "I_DEVICE_TYPE";
    public static final String I_PPID = "I_PPID";
    public static final String LUMN_DEVICE = "Lumn";
    public static final String CATCH_DEVICE = "Catch";
    public static final String OVCAMP_DEVICE = "ovCamp";
    public static final String EVENT = "EVENT";
    public static final String EVENT_LUMN = "EVENT_LUMN";
    public static final String EVENT_OVCAMP = "EVENT_OVCAMP";
    public static final String IC_CARD_DEVICE = "IC_CARD_DEVICE";
    public static final String LUMN_PPID = "LUMN_PPID";
    public static final String LUMN_OK = "LUMN_OK";
    public static final String LUMN_HAND = "LUMN_HAND";
    public static final String IS_CARD = "IS_CARD";
    public static final String NETWORK_SSID = "NETWORK_SSID";
    public static final String TOGGLE_POSITION = "TOGGLE_POSITION";
    public static final String SECOND = "SECOND";
    public static final String SECOND_SP2 = "SECOND_SP2";
    public static final String BAUDRATE = "BAUDRATE";
    public static final long TWO_SECOND = 1000*2;
    public static final long FIVE_SECOND = 1000*5;
    public static final long SEVEN_SECOND = 1000*7;
    public static final long THREE_SECOND = 1000*3;
    public static final long FOUR_SECOND = 1000*4;

    public static final String ACTIVE_DEVICE = "ACTIVE_DEVICE";
//    public static final long FIVE_MINUTE = 1000*5;
//    public static final long EIGHT_MINUTE = 1000*60*8;
    public static final long ONE_SECOND = 1000;
    public static final String WIFI_DEVICE_LIST = "WIFI_DEVICE_LIST";
    public static final String BLUETOOTH_DEVICE_LIST = "BLUETOOTH_DEVICE_LIST";
    public static final String PROTOCOL_NAME_LIST = "PROTOCOL_NAME_LIST";
    public static final String ACCOUNT_DETAIL = "ACCOUNT_DETAIL";
    public static final String HELLLOOOO = "HELLLOOOO";
    public static final String IS_FIRSTTIMERUN = "IS_FIRSTTIMERUN";
    public static String READ_CLICK = "READ_CLICK";

    public static final String IMAGE_PATH = "https://midlal.com/";
    public static final String BarcodeImagePath = "http://midlal.com/uploads/barcodeImage/";


    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d$&+,:;=?@#|'<>.-^*()%!]{8,}$";
    public static final String INTRO_PREF = "introPref";
    public static final String IS_SHOW_INTRO = "isShowIntro";
    public static final String UNREAD_COMMENTS = "unreadChatComments";
    public static final String USER = "mUser";
    public static final String POSITION = "position";
    public static final String POSITION_SP2 = "POSITION_SP2";
    public static final String JSONPOSITION = "JSONPOSITION";
    public static final String PROTOCOLTABLEPOSITION = "PROTOCOLTABLEPOSITION";
    public static String USB_DISCONNECT = "USB_DISCONNECT";

    public static enum Dialogcode {
        WARNING, SUCCES, POPFEEDBACK, EDITSCREEN, UPDATE
    }

    //    public static final String URL = "https://midlal.com/webservice";
    public static final String URL = "https://dev-api.omnivoltaic.com/user/login";
    public static final String BASE_URL = "https://midlal.com/";

    public static final String BUSINESS = "mBusiness";


    public static final int UNBOUNDED = View.MeasureSpec.makeMeasureSpec(0,
            View.MeasureSpec.UNSPECIFIED);


    //Custom Gallery
    public static final String IMAGE_LIST = "image_list";
    public static final int PERMISSION_REQUEST_CODE = 1000;
    public static final int PERMISSION_GRANTED = 1001;
    public static final int REQUEST_CODE = 2000;
    public static final int FETCH_STARTED = 2001;
    public static final int FETCH_COMPLETED = 2002;
    public static final int ERROR = 2005;
    public static final String INTENT_EXTRA_ALBUM = "album";
    public static final String DEFAULT_LIMIT = "default_limit";
    public static final int LIMIT_7 = 7;
    public static final int LIMIT_3 = 3;
    public static final String IS_IMAGES = "is_images";
    public static final String CATEGORY_NAME = "category_name";

    public static final String COUNT = "boardcount";
}
