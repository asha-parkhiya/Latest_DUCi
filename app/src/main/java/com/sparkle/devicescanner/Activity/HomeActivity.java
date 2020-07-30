package com.sparkle.devicescanner.Activity;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.sparkle.devicescanner.Base.BaseActivity;
import com.sparkle.devicescanner.ContentProvider.NewAdapter;
import com.sparkle.devicescanner.ContentProvider.ToDo;
import com.sparkle.devicescanner.Fragment.SubscribeTopicFragment;
import com.sparkle.devicescanner.LocalDatabase.DatabaseHelper;
import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.Constant;
import com.sparkle.devicescanner.Utils.ConstantMethod;
import com.sparkle.devicescanner.Utils.MyPref;
import com.sparkle.devicescanner.events.MqttStringEvent;
import com.sparkle.devicescanner.events.PPIDDeviceTypeEvent;
import com.sparkle.devicescanner.services.MqttService;
import com.sparkle.devicescanner.services.WifiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.fabric.sdk.android.Fabric;



public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemSelectedListener,NewAdapter.onItemClickListener {

    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

    MqttService mService;
    boolean mBound = false;
    boolean mBluetoothBound = false;
    boolean isSecondTime = false;
    private final String MQTT_DEFAULT_TOPIC_SUBSCRIBE = "_OV1/GPRS/camp/";
//    private final String MQTT_DEFAULT_TOPIC_SUBSCRIBE = "_OV1/ovCamp/PPID";
    MyPref myPref;
    StringBuilder stringBuilder;
    StringBuilder stringBuilder1;
    String ppidFromCard;
    String ppidFromLUMN;
    String ppidFromovCAMP;

    ViewPagerAdapter viewPagerAdapter;
//    TabLayout tab_Layout;
    ViewPager view_Pager;

    ImageView btn_settings, btn_device, btn_scan;
    TextView connection_info;

    WifiService wifiService;
    final Handler handler = new Handler();
    Intent mServiceIntent;

    List<ScanResult> wifiScanList;
    List<BluetoothDevice> bluetoothDeviceList;
    BluetoothDevice device;
    String string;
    boolean ONE_TIME = false;
    boolean WRITE_CODE = false;
    boolean WRITE_OVCAMP_CODE = false;
    boolean NO_COMMAND = false;

//    public static long interval = 1000 * 3;

    //    List<String[]> scoreList;
//    private ItemArrayAdapter itemArrayAdapter;
    private ListView listView;
    private File csvfile;

    private List<ToDo> toDos;
    private List<ToDo> toDosfinal;
    //    private NewAdapter newAdapter;
    private Timer timer;
    private Timer timer1;
    private Timer timer2;

    private ContentResolver contentResolver;
    Toast mToastToShow;
    DrawerLayout drawer;
    private ImageView iv_menu,btn_usb,btn_usb1;
    private TextView tv_clear_all,tv_setting,tv_database, tv_termux, tv_edit_protocol, tv_protocol_table, tv_add_protocol;
    private ImageView im_setting,im_protocol,im_protocol_table,im_mqtt;

    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private TextView mqtt_status;
    private View bottom_sheet;
    AsyncTask<?, ?, ?> runningTask;
    private List<ToDo> toDoList;
    private NewAdapter newAdapter;
    private String str11;
    private int position;
    File csvfile1 = new File(Environment.getExternalStorageDirectory() + "/Download/Hello.sh");
    ProgressBar progress_indeterminate;

    private DatabaseHelper myDb;


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MqttService.LocalBinder binder = (MqttService.LocalBinder) service;
            HomeActivity.this.mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    public void forceCrash() {
        throw new RuntimeException("This is a crash");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitleTextColor(Color.BLACK);

//        forceCrash();
//        btn_device = (ImageView)findViewById(R.id.btn_device);
//        btn_device.setOnClickListener(this);

//        btn_scan = (ImageView)findViewById(R.id.btn_scan);
//        btn_scan.setOnClickListener(this);
        btn_usb = (ImageView)findViewById(R.id.btn_usb);
//        btn_usb.setOnClickListener(this);
        btn_usb1 = (ImageView)findViewById(R.id.btn_usb1);
//        btn_usb.setOnClickListener(this);

        bottom_sheet = findViewById(R.id.bottom_sheet);

        myDb = new DatabaseHelper(this);
        myPref = new MyPref(this);

        boolean isfirsttimerun = myPref.getPref(Constant.IS_FIRSTTIMERUN, true);
        System.out.println("---------------------"+isfirsttimerun);
        if (isfirsttimerun) {
            myPref.setPref(Constant.IS_FIRSTTIMERUN,false);
            insertDataBase();
        }


//        tab_Layout = (TabLayout) findViewById(R.id.tab_layout);
        view_Pager = (ViewPager) findViewById(R.id.view_pager);
//        progress_indeterminate = (ProgressBar) findViewById(R.id.progress_indeterminate);
        mqtt_status = findViewById(R.id.mqtt_status);
        iv_menu = findViewById(R.id.iv_menu);
        iv_menu.setOnClickListener(this);
        toDoList = new ArrayList<>();
        newAdapter = new NewAdapter(toDoList,this,this::changevalue);

        drawer = findViewById(R.id.drawer_layout);

//        System.out.println("----------------------"+Environment.getExternalStorageDirectory());
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

//        tv_clear_all = navigationView.getHeaderView(0).findViewById(R.id.tv_clear_all);
//        tv_database = navigationView.getHeaderView(0).findViewById(R.id.tv_database);
//        tv_edit_protocol = navigationView.getHeaderView(0).findViewById(R.id.tv_edit_protocol);
        tv_termux = navigationView.getHeaderView(0).findViewById(R.id.tv_termux);
        tv_protocol_table = navigationView.getHeaderView(0).findViewById(R.id.tv_protocol_table);
        tv_add_protocol = navigationView.getHeaderView(0).findViewById(R.id.tv_add_protocol);
        tv_setting = navigationView.getHeaderView(0).findViewById(R.id.tv_setting);

        im_mqtt = navigationView.getHeaderView(0).findViewById(R.id.im_mqtt);
        im_protocol_table = navigationView.getHeaderView(0).findViewById(R.id.im_protocol_table);
        im_protocol = navigationView.getHeaderView(0).findViewById(R.id.im_protocol);
        im_setting = navigationView.getHeaderView(0).findViewById(R.id.im_setting);

        im_mqtt.setOnClickListener(this);
        im_protocol_table.setOnClickListener(this);
        im_protocol.setOnClickListener(this);
        im_setting.setOnClickListener(this);
//        tv_clear_all.setOnClickListener(this);
//        tv_database.setOnClickListener(this);
//        tv_edit_protocol.setOnClickListener(this);
        tv_termux.setOnClickListener(this);
        tv_protocol_table.setOnClickListener(this);
        tv_add_protocol.setOnClickListener(this);
        tv_setting.setOnClickListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mService != null){
                    mService.connect();
                    if (mService.isConnected()){
                        mqtt_status.setBackgroundResource(R.color.green);
                        mqtt_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    }else {
                        mqtt_status.setBackgroundResource(R.color.light_grey);
                    }
                    System.out.println("------connection---------"+mService.isConnected());
                }else{
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(HomeActivity.this, "MQTT not connected", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        }, 2000);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (mService != null){
                    if (!mService.isConnected()) {
                        if (!mBound) return;
                        if (mService.connect()) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(HomeActivity.this, "MQTT connected", Toast.LENGTH_SHORT).show();
                                    mqtt_status.setBackgroundResource(R.color.green);
                                    mqtt_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                }
                            });

                        }
//                        add_PPID_CODE1("03AH2000000000","ovCamp");
                    } else {
//                        add_PPID_CODE1("03AH2000000000","ovCamp");
                    }
                }

            }
        },3000);

        startMainActivity();



    }

    public void startMainActivity(){

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new SubscribeTopicFragment(), "Subscribe Topic");
//        viewPagerAdapter.addFragment(new SubscribeMessageFragment(), "Subscribe Message");

        view_Pager.setAdapter(viewPagerAdapter);
//        tab_Layout.setupWithViewPager(view_Pager);

        stringBuilder = new StringBuilder();
        stringBuilder1 = new StringBuilder();

        ONE_TIME = false;
        WRITE_CODE = false;
        WRITE_OVCAMP_CODE = false;
        NO_COMMAND = false;
        myPref.setPref(Constant.ALL_TIME_HAND,false);
        myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE,false);
        myPref.setPref(Constant.CALL_LUMN_CAMP,false);
        myPref.setPref(Constant.GET_PPID,false);
        myPref.setPref(Constant.GET_REPLY,false);
        myPref.setPref(Constant.LUMN_USB,false);
        myPref.setPref(Constant.LUMN,false);
        myPref.setPref(Constant.OVCAMP,false);
        myPref.setPref(Constant.NO_OVCAMP_LUMN,false);
        myPref.setPref(Constant.DEVICE_OVCAMP,false);
        myPref.setPref(Constant.DEVICE_LUMN,false);
        myPref.setPref(Constant.USB_DISCONNECT,false);

        bluetoothwifisetting();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                add_PPID_CODE1("03AH2000000000","ovCamp");
                }
        },3000);
    }

    @Override
    public void onUsbDisconnect() {
        startMainActivity();
        myPref.setPref(Constant.CALL_LUMN_CAMP,false);
        myPref.setPref(Constant.NO_OVCAMP_LUMN,false);
        callLumnCamp();
    }

    public void add_PPID_CODE1(String PPID, String device_type){
        NO_COMMAND = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btn_usb.setVisibility(View.GONE);
                btn_usb1.setVisibility(View.VISIBLE);
            }
        });

        EventBus.getDefault().post(new PPIDDeviceTypeEvent(device_type,PPID));
        try {
            mService.subscribe("_OV1/"+device_type+"/" + PPID , 2);
        }catch (Exception e){
//            Toast.makeText(HomeActivity.this, ""+e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void changevalue(String str, int position) {
        myPref.setPref("string11",str);
        myPref.setPref("position11",position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageStringEvent(String event) {
        if (stringBuilder1 == null) {
            stringBuilder1 = new StringBuilder();
            stringBuilder1.append("\n"+event);
        } else
            stringBuilder1.append("\n"+event);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void handEveryTime(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                if (!myPref.getPref(Constant.ALL_TIME_HAND,false)){
                    myPref.setPref(Constant.HAND_START,true);
                    myPref.setPref(Constant.GET_REPLY,false);
                    String read = ">HAND<";
                    byte[] readtmp = ConstantMethod.stringToByteArray(read);
                    startReadingCard(readtmp);
                    myPref.setPref(Constant.LUMN_USB,false);
                    System.out.println("-------------------handlumn");
                }
            }
        });
        thread.start();
    }



    public void ovCampCodeEveryTime(){

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                if (!myPref.getPref(Constant.ALL_TIME_OVCAMP_CODE,false)){
                    myPref.setPref(Constant.OVCAMP_CODE_START,true);
                    String read = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C5 6A 29 07 08 14 CB";
                    byte[] readtmp = ConstantMethod.hexStr2Bytes(read);
                    startReadingCard(readtmp);
                    myPref.setPref(Constant.LUMN_USB,true);
                    System.out.println("-------------------hexovcamp");
                }
            }
        });
        thread.start();
    }

    public void callLumnCamp(){
        if(!myPref.getPref(Constant.CALL_LUMN_CAMP,false)){
            myPref.setPref(Constant.ALL_TIME_HAND,false);
            myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE,true);
            handEveryTime();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (!myPref.getPref(Constant.OVCAMP,false)){
                        myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE,false);
                        myPref.setPref(Constant.ALL_TIME_HAND,true);
                        ovCampCodeEveryTime();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                if(!myPref.getPref(Constant.NO_OVCAMP_LUMN,false)){
                                    callLumnCamp();
                                }
                            }
                        },myPref.getPref(Constant.SECOND,Constant.FOUR_SECOND));
                    }
                }
            },myPref.getPref(Constant.SECOND,Constant.FOUR_SECOND));
        }
    }



    //C56A291B081431324148313931333030303031323C3C3C3C3C3CFA     31324148313931333030303031323C3C3C3C3C3C
    @Override
    public void onUsbSerialMessage(String message) {
        if (stringBuilder == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(message);
        } else {
            stringBuilder.append(message);
        }

        if (stringBuilder.toString().contains("<OK>")) {
            myPref.setPref(Constant.DEVICE_LUMN, true);
            myPref.setPref(Constant.DEVICE_OVCAMP, false);
            stringBuilder1.append(" 1) DEVICE_LUMN is true.");
        }


        stringBuilder1.append("---log----" + stringBuilder.toString());
//        EventBus.getDefault().post("on USb Serial Message : " + stringBuilder.toString());

//        if (WRITE_OVCAMP_CODE) {
//            stringBuilder1.append("WRITE_OVCAMP_CODE is true........."+stringBuilder.toString());
//            if (stringBuilder.toString().length() == 14) {
//                myPref.setPref(Constant.LUMN_USB, true);
////                myPref.setPref(Constant.OVCAMP_USB,false);
//                myPref.setPref(Constant.LUMN, true);
//                myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE, true);
//                myPref.setPref(Constant.ALL_TIME_HAND, true);
//                myPref.setPref(Constant.OVCAMP_CODE_START, false);
//                myPref.setPref(Constant.CALL_LUMN_CAMP, true);
//                myPref.setPref(Constant.HAND_START, false);
//                myPref.setPref(Constant.DEVICE_OVCAMP, false);
//                if (stringBuilder.toString().equals("C56A2907FA0A41")) {
//
//                    stringBuilder1.append(stringBuilder.toString());
//                    int toastDurationInMilliSeconds = 2000;
//                    Toast mShow = Toast.makeText(HomeActivity.this, "Top up successfully for PPID " + ppidFromovCAMP, Toast.LENGTH_LONG);
//                    CountDownTimer toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
//                        public void onTick(long millisUntilFinished) {
//                            mShow.show();
//                        }
//
//                        public void onFinish() {
//                            mShow.cancel();
//                        }
//                    };
////                    mShow.show();
//                    toastCountDown.start();
//
//                    contentResolver.delete(ToDoProviderConstants.CONTENT_URI_1, ToDoProviderConstants.COLUMN_ID + " = ?", new String[]{ppidFromovCAMP});
//                    ppidFromovCAMP = "";
//                    stringBuilder = null;
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mShow.cancel();
//                            stringBuilder1.append(" 1) OVCAMP Toast cancle and start main Activity.");
//                            toastCountDown.cancel();
//                            startMainActivity();
//                            myPref.setPref(Constant.CALL_LUMN_CAMP, false);
//                            myPref.setPref(Constant.NO_OVCAMP_LUMN, false);
//                            callLumnCamp();
////                            myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE,false);
////                            ovCampCodeEveryTime();
//                        }
//                    }, 3000);
//
//                } else if (stringBuilder.toString().equals("C56A2907FA0B1F")) {
//                    stringBuilder1.append(stringBuilder.toString());
//                    Toast toast = Toast.makeText(HomeActivity.this, "Latest keycode.", Toast.LENGTH_LONG);
//                    toast.show();
//
//                    contentResolver.delete(ToDoProviderConstants.CONTENT_URI_1, ToDoProviderConstants.COLUMN_ID + " = ?", new String[]{ppidFromovCAMP});
//                    ppidFromovCAMP = "";
//                    stringBuilder = null;
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            toast.cancel();
//                            stringBuilder1.append(" 2) OVCAMP Toast cancle and start main Activity.");
//                            startMainActivity();
////                            myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE,false);
////                            ovCampCodeEveryTime();
//                            myPref.setPref(Constant.CALL_LUMN_CAMP, false);
//                            myPref.setPref(Constant.NO_OVCAMP_LUMN, false);
//                            callLumnCamp();
//                        }
//                    }, 3000);
//
//                } else if (stringBuilder.toString().equals("C56A2907FAA090")) {
//                    stringBuilder1.append(stringBuilder.toString());
//                    Toast toast = Toast.makeText(HomeActivity.this, "Wrong keycode.", Toast.LENGTH_LONG);
//                    toast.show();
//
//                    contentResolver.delete(ToDoProviderConstants.CONTENT_URI_1, ToDoProviderConstants.COLUMN_ID + " = ?", new String[]{ppidFromovCAMP});
//                    ppidFromovCAMP = "";
//                    stringBuilder = null;
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            toast.cancel();
//                            stringBuilder1.append(" 3) OVCAMP Toast cancle and start main Activity.");
//                            startMainActivity();
////                            myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE,false);
////                            ovCampCodeEveryTime();
//                            myPref.setPref(Constant.CALL_LUMN_CAMP, false);
//                            myPref.setPref(Constant.NO_OVCAMP_LUMN, false);
//                            callLumnCamp();
//                        }
//                    }, 3000);
//
//                }
//            } else {
//                stringBuilder1.append("write code reply : " + stringBuilder.toString());
//            }
//
//        }

        if (stringBuilder.toString().contains("C56A291B")) {
            myPref.setPref(Constant.NO_OVCAMP_LUMN, true);
            myPref.setPref(Constant.LUMN, true);
            myPref.setPref(Constant.LUMN_USB, true);
//                myPref.setPref(Constant.OVCAMP_USB,false);
            myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE, true);
            myPref.setPref(Constant.ALL_TIME_HAND, true);
            myPref.setPref(Constant.OVCAMP_CODE_START, false);
            myPref.setPref(Constant.HAND_START, false);
            myPref.setPref(Constant.CALL_LUMN_CAMP, true);

            myPref.setPref(Constant.DEVICE_OVCAMP, true);
            myPref.setPref(Constant.DEVICE_LUMN, false);
            stringBuilder1.append(" 2) DEVICE_OVCAMP is true.");
        }

        if (myPref.getPref(Constant.DEVICE_OVCAMP, true)) {
            stringBuilder1.append(" 3) DEVICE_OVCAMP is true.");
            if (stringBuilder.toString().length() >= 52 && stringBuilder.toString().length() <= 54) {
                stringBuilder1.append("camp if : " + stringBuilder.toString());//C56A291B081430364148323030333030303030333C3C3C3C3C3C45
                if (stringBuilder.toString().startsWith("C56A291B")) {
                    myPref.setPref(Constant.NO_OVCAMP_LUMN, true);
                    myPref.setPref(Constant.LUMN, true);
                    myPref.setPref(Constant.LUMN_USB, true);
                    myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE, true);
                    myPref.setPref(Constant.ALL_TIME_HAND, true);
                    myPref.setPref(Constant.OVCAMP_CODE_START, false);
                    myPref.setPref(Constant.HAND_START, false);
                    myPref.setPref(Constant.CALL_LUMN_CAMP, true);
                    String msg = stringBuilder.toString();
                    String data = msg.substring(12, 52);
                    stringBuilder1.append("\n " + "PPID in hex1 : " + data);//30364148323030333030303030333C3C3C3C3C3C//06AH2003000003
                    ppidFromovCAMP = ConstantMethod.convertHexToString(data);
                    ppidFromovCAMP = ppidFromovCAMP.replace("<", "");
                    System.out.println("-------ppidFromovCAMP-------" + ppidFromovCAMP);
                    WRITE_OVCAMP_CODE = true;
                    stringBuilder1.append("\n " + "Final PPID ovCamp1 : " + ppidFromovCAMP);//06AH2003000003
//                    add_PPID_CODE(ppidFromovCAMP);
                    add_PPID_CODE1(ppidFromovCAMP,Constant.OVCAMP_DEVICE);
                    stringBuilder = null;
                }
            }
        }

        if (myPref.getPref(Constant.DEVICE_LUMN, true)) {
            stringBuilder1.append(" 4) DEVICE_LUMN is true.");
            if (stringBuilder.toString().contains("<OK>")) {
                if (!myPref.getPref(Constant.ALL_TIME_HAND, false)) {
                    myPref.setPref(Constant.LUMN_USB, false);
                    myPref.setPref(Constant.OVCAMP, true);
//                myPref.setPref(Constant.OVCAMP_USB,true);
                    myPref.setPref(Constant.ALL_TIME_HAND, true);
                    myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE, true);
                    myPref.setPref(Constant.HAND_START, false);
                    myPref.setPref(Constant.OVCAMP_CODE_START, false);
                    myPref.setPref(Constant.CALL_LUMN_CAMP, true);
                    String read = ">PPID<";
                    byte[] readtmp = ConstantMethod.stringToByteArray(read);
                    startReadingCard(readtmp);
                    String string = " Pass >PPID< command ";
                    System.out.println("---------------Pass >PPID< command");
                    stringBuilder1.append("\n " + string);
                    stringBuilder = null;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!myPref.getPref(Constant.GET_PPID, false)) {
                                stringBuilder1.append("----------------back to hand function from ppid");
                                myPref.setPref(Constant.CALL_LUMN_CAMP, false);
                                myPref.setPref(Constant.NO_OVCAMP_LUMN, false);
                                callLumnCamp();
                            }
                        }
                    }, myPref.getPref(Constant.SECOND, Constant.FOUR_SECOND));
                }
            }
        }
//        if (WRITE_CODE){
//            boolean one = false;
//            if (stringBuilder.toString().contains("<RE:")){
//                one = true;
//                String[] separated1 = stringBuilder.toString().split("<RE:",9);
//                String onlyforlumn = "<RE:"+ separated1[1];
//                if (onlyforlumn.length() == 9){
//                    stringBuilder1.append("<RE> state replay : "+onlyforlumn);
//                    Toast tost = Toast.makeText(HomeActivity.this, "Top up in progress", Toast.LENGTH_SHORT);
//                    String lumnreply = onlyforlumn;
//                    lumnreply = lumnreply.replace("<RE:","");
//                    lumnreply = lumnreply.replace(">","");
//                    if (lumnreply.toLowerCase().equalsIgnoreCase("FAIL")){
//                        System.out.println("---------------Invalid keycode");
//                        myPref.setPref(Constant.GET_REPLY,true);
//                        Toast.makeText(HomeActivity.this, "Invalid keycode", Toast.LENGTH_LONG).show();
//
//                        contentResolver.delete(ToDoProviderConstants.CONTENT_URI_1,ToDoProviderConstants.COLUMN_ID +" = ?",new String[]{ppidFromLUMN});
//                        ppidFromLUMN = "";
//                        stringBuilder = null;
//                        stringBuilder1.append(" 1) LUMN Toast cancle and start main Activity.");
//                        startMainActivity();
////                        handEveryTime();
//                        myPref.setPref(Constant.CALL_LUMN_CAMP,false);
//                        myPref.setPref(Constant.NO_OVCAMP_LUMN,false);
//                        callLumnCamp();
//                    }else if (lumnreply.toLowerCase().equalsIgnoreCase("WAIT")){
//                        tost.show();
//                        System.out.println("---------------Top up in progress");
//                        stringBuilder1.append(" 2) LUMN Toast cancle and wait for response.");
//                        stringBuilder = null;
//                    }else {
//                        tost.cancel();
//                        System.out.println("---------------Top up "+lumnreply+" days successfully for PPID "+ppidFromLUMN);
//                        myPref.setPref(Constant.GET_REPLY,true);
//                        int toastDurationInMilliSeconds = 2000;
//                        mToastToShow = Toast.makeText(HomeActivity.this, "Top up "+lumnreply+" days successfully for PPID "+ppidFromLUMN, Toast.LENGTH_LONG);
//                        CountDownTimer toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {public void onTick(long millisUntilFinished) { mToastToShow.show(); }public void onFinish() { mToastToShow.cancel(); }};
//                        mToastToShow.show();
//                        toastCountDown.start();
//
//                        contentResolver.delete(ToDoProviderConstants.CONTENT_URI_1,ToDoProviderConstants.COLUMN_ID +" = ?",new String[]{ppidFromLUMN});
//                        ppidFromLUMN = "";
//                        stringBuilder = null;
////                        myPref.setPref(Constant.ALL_TIME_HAND,false);
////                        handEveryTime();
//
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                stringBuilder = null;
//                                mToastToShow.cancel();
//                                stringBuilder1.append(" 3) LUMN Toast cancle and start main Activity.");
//                                startMainActivity();
//                                myPref.setPref(Constant.CALL_LUMN_CAMP,false);
//                                myPref.setPref(Constant.NO_OVCAMP_LUMN,false);
//                                callLumnCamp();
//                            }
//                        },3000);
//                    }
//                }
//            }
//            if (!one){
//                if (stringBuilder.toString().length() == 9){
//                    if (stringBuilder.toString().contains("<RE:")){
//                        stringBuilder1.append("<RE> state replay : "+stringBuilder.toString());
//                        Toast tost = Toast.makeText(HomeActivity.this, "Top up in progress", Toast.LENGTH_SHORT);
//                        String lumnreply = stringBuilder.toString();
//                        lumnreply = lumnreply.replace("<RE:","");
//                        lumnreply = lumnreply.replace(">","");
//                        if (lumnreply.toLowerCase().equalsIgnoreCase("FAIL")){
//                            System.out.println("---------------Invalid keycode");
//                            myPref.setPref(Constant.GET_REPLY,true);
//                            Toast.makeText(HomeActivity.this, "Invalid keycode", Toast.LENGTH_LONG).show();
//
//                            contentResolver.delete(ToDoProviderConstants.CONTENT_URI_1,ToDoProviderConstants.COLUMN_ID +" = ?",new String[]{ppidFromLUMN});
//                            ppidFromLUMN = "";
//                            stringBuilder = null;
//                            stringBuilder1.append(" 1) LUMN Toast cancle and start main Activity.");
//                            startMainActivity();
////                        handEveryTime();
//                            myPref.setPref(Constant.CALL_LUMN_CAMP,false);
//                            myPref.setPref(Constant.NO_OVCAMP_LUMN,false);
//                            callLumnCamp();
//                        }else if (lumnreply.toLowerCase().equalsIgnoreCase("WAIT")){
//                            tost.show();
//                            System.out.println("---------------Top up in progress");
//                            stringBuilder1.append(" 2) LUMN Toast cancle and wait for response.");
//                            stringBuilder = null;
//                        }else {
//                            tost.cancel();
//                            System.out.println("---------------Top up "+lumnreply+" days successfully for PPID "+ppidFromLUMN);
//                            myPref.setPref(Constant.GET_REPLY,true);
//                            int toastDurationInMilliSeconds = 2000;
//                            mToastToShow = Toast.makeText(HomeActivity.this, "Top up "+lumnreply+" days successfully for PPID "+ppidFromLUMN, Toast.LENGTH_LONG);
//                            CountDownTimer toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {public void onTick(long millisUntilFinished) { mToastToShow.show(); }public void onFinish() { mToastToShow.cancel(); }};
//                            mToastToShow.show();
//                            toastCountDown.start();
//
//                            contentResolver.delete(ToDoProviderConstants.CONTENT_URI_1,ToDoProviderConstants.COLUMN_ID +" = ?",new String[]{ppidFromLUMN});
//                            ppidFromLUMN = "";
//                            stringBuilder = null;
////                        myPref.setPref(Constant.ALL_TIME_HAND,false);
////                        handEveryTime();
//
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    stringBuilder = null;
//                                    mToastToShow.cancel();
//                                    stringBuilder1.append(" 3) LUMN Toast cancle and start main Activity.");
//                                    startMainActivity();
//                                    myPref.setPref(Constant.CALL_LUMN_CAMP,false);
//                                    myPref.setPref(Constant.NO_OVCAMP_LUMN,false);
//                                    callLumnCamp();
//                                }
//                            },3000);
//                        }
//                    }
//                }
//            }
//
//        }


        if (myPref.getPref(Constant.DEVICE_LUMN, true)) {
            stringBuilder1.append(" 5) DEVICE_LUMN is true.");
            stringBuilder1.append(" LUMN : "+stringBuilder.toString());
            if (stringBuilder.toString().length() >= 27 && stringBuilder.toString().length() <= 33) {//138 LUMN
                //<INF:OPID:91051510000001,PPID:91051510000001 ,OCS: ENABLED,PS:FREE,RPD: 0000,TDP:0000,LVC:4F80DE119510D208,RT:0000,LCS:0000>
                //<INF:OPID:M400180399012 ,PPID:M400180399012       ,OCS: ENABLED,PS:FREE,RPD: 1125D22H48M,TDP:9999,LVC:FB81B8B9C3725DBE,RT:00106,LCS:00004>
                //10 14 6 20 6 7 4 4 6 11 5 4 5 16 4 5 5 5 1=138
                if (stringBuilder.toString().contains("<PPID:")) {
                    myPref.setPref(Constant.LUMN_USB, false);
                    myPref.setPref(Constant.OVCAMP, true);
//                myPref.setPref(Constant.OVCAMP_USB,true);
                    myPref.setPref(Constant.GET_PPID, true);
                    myPref.setPref(Constant.ALL_TIME_HAND, true);
                    myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE, true);
                    myPref.setPref(Constant.HAND_START, false);
                    myPref.setPref(Constant.OVCAMP_CODE_START, false);
                    myPref.setPref(Constant.CALL_LUMN_CAMP, true);
                    ppidFromLUMN = stringBuilder.toString();
                    ppidFromLUMN = ppidFromLUMN.replace(">PPID<", "");
                    ppidFromLUMN = ppidFromLUMN.replace("<PPID:", "");
                    ppidFromLUMN = ppidFromLUMN.replace(">", "");
                    ppidFromLUMN = ppidFromLUMN.replace(" ", "");

                    stringBuilder1.append("\n " + "Final PPID:" + ppidFromLUMN);
                    System.out.println("--------ppidforlumn----------" + ppidFromLUMN);

                    WRITE_CODE = true;
//                    add_PPID_CODE(ppidFromLUMN);
                    add_PPID_CODE1(ppidFromLUMN,Constant.LUMN_DEVICE);
                    stringBuilder = null;
                }
            }
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.btn_scan:
//                ONE_TIME = false;
//                WRITE_CODE = false;
//                WRITE_OVCAMP_CODE = false;
//                myPref.setPref(Constant.ALL_TIME_HAND,false);
//                myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE,false);
//                myPref.setPref(Constant.GET_PPID,false);
//                myPref.setPref(Constant.GET_REPLY,false);
//                myPref.setPref(Constant.OVCAMP,false);
//                myPref.setPref(Constant.LUMN,false);
//                myPref.setPref(Constant.NO_OVCAMP_LUMN,false);
//                myPref.setPref(Constant.DEVICE_LUMN,false);
//                myPref.setPref(Constant.DEVICE_OVCAMP,false);
//                stringBuilder1 = new StringBuilder();
//
//                if (myPref.getPref(Constant.CALL_LUMN_CAMP,true)) {
//                    myPref.setPref(Constant.CALL_LUMN_CAMP, false);
//                    myPref.setPref(Constant.NO_OVCAMP_LUMN,false);
//                    callLumnCamp();
//                }
////                bluetoothwifisetting();
//                startMainActivity();
//
//                break;

//            case R.id.btn_device:

//                Intent intent1 = new Intent(HomeActivity.this, ActiveDeviceActivity.class);
//                startActivity(intent1);
//                generateNoteOnSD(getApplicationContext(),"Hello.sh","#!/data/data/com.termux/files/usr/bin/sh\n" +
//                        "/system/bin/sh /storage/emulated/0/Hello.sh\n" +
//                        "mosquitto");

//                break;
//            case R.id.btn_settings:
//
//                break;

            case R.id.iv_menu:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.tv_termux:
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.termux");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                }
                break;

            case R.id.tv_protocol_table:
                Intent intent4 = new Intent(HomeActivity.this,ProtocolTableActivity.class);
                startActivity(intent4);
                break;

            case R.id.tv_add_protocol:
                Intent intent5 = new Intent(HomeActivity.this,AddProtocolActivity.class);
                startActivity(intent5);
                break;

            case R.id.tv_setting:
                Intent intent = new Intent(HomeActivity.this,SettingActivity.class);
                intent.putExtra("log_data",stringBuilder1.toString());
                startActivity(intent);
                break;

            case R.id.im_mqtt:
                Intent launchIntent1 = getPackageManager().getLaunchIntentForPackage("com.termux");
                if (launchIntent1 != null) {
                    startActivity(launchIntent1);
                }
                break;

            case R.id.im_protocol_table:
                Intent intent41 = new Intent(HomeActivity.this,ProtocolTableActivity.class);
                startActivity(intent41);
                break;

            case R.id.im_protocol:
                Intent intent51 = new Intent(HomeActivity.this,AddProtocolActivity.class);
                startActivity(intent51);
                break;

            case R.id.im_setting:
                Intent intent1 = new Intent(HomeActivity.this,SettingActivity.class);
                intent1.putExtra("log_data",stringBuilder1.toString());
                startActivity(intent1);
                break;

            case R.id.btn_usb:
//                showBottomSheetDialog();
                break;

        }
    }
    private void showBottomSheetDialog() {
//        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        }

        final View view = getLayoutInflater().inflate(R.layout.sheet_floating, null);
        ((TextView) view.findViewById(R.id.tv_app_name)).setText("Device Scanner");
        ((TextView) view.findViewById(R.id.tv_app_brief)).setText("789454321");
//        ((TextView) view.findViewById(R.id.tv_device_name)).setText("LUMN");
//        ((TextView) view.findViewById(R.id.tv_device_brief)).setText("12456876954");
        (view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.hide();
            }
        });


        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // set background transparent
        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });

    }


    @Override
    protected void onUsbReady() {
        super.onUsbReady();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                String read = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C5 6A 29 07 01 02 39";
//                byte[] readtmp = ConstantMethod.hexStr2Bytes(read);
//                startReadingCard(readtmp);
////                EventBus.getDefault().post("null");
//            }
//        }, 1000);

//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (!myPref.getPref(Constant.ALL_TIME_HAND,true)){
//                    String read = ">HAND<";
//                    byte[] readtmp = ConstantMethod.stringToByteArray(read);
//                    startReadingCard(readtmp);
////                    String string = "\n" + " Start scanning and Pass >HAND< command ";
//                    System.out.println("-------hand---------"+read);
//                    stringBuilder1.append(string);
//                    onUsbReady();
//                }
//            }
//        }, myPref.getPref(Constant.SECOND,Constant.THREE_SECOND));

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                String read = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C5 6A 29 07 08 14 CB";
//                byte[] readtmp = ConstantMethod.hexStr2Bytes(read);
//                startReadingCard(readtmp);
//                String string = format + ": Start scanning and Pass HEX command ";
//                stringBuilder1.append(string);
//            }
//        }, 5000);

//         startMainActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(HomeActivity.this,SettingActivity.class));
//            return true;
//        }else if (id == R.id.action_devices){
//            startActivity(new Intent(HomeActivity.this,DeviceListActivity.class));
//            return true;
//        }else if (id == R.id.action_help){
//            Toast.makeText(HomeActivity.this, "Coming soon...!!", Toast.LENGTH_SHORT).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_home) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_tools) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onMqttMessage(MqttStringEvent event) {

    }

    private void bluetoothwifisetting() {
//        bluetoothDeviceList = new ArrayList<>();
//        wifiScanList = new ArrayList<>();
//        Animation animation = new RotateAnimation(0.0f, 360.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                0.5f);
//        animation.setRepeatCount(-1);
//        animation.setDuration(1000);
//
//        btn_scan.clearAnimation();
//        btn_scan.setAnimation(animation);
//
//        wifiService = new WifiService();
//        mServiceIntent = new Intent(getApplicationContext(), wifiService.getClass());
//        wifiService.scanWifi(getApplicationContext());
//
//        bluetoothService = new BluetoothService();
//        bluetoothService.scanBluetooth(HomeActivity.this);
//
//        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
////        we are connected to a network
//
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    wifiScanList = wifiService.getScanResult(getApplicationContext());
//                    EventBus.getDefault().post(wifiScanList);
//                    btn_scan.clearAnimation();
//                }
//            },6000);
//        }
//        else{
//            Toast.makeText(HomeActivity.this, "Please check network connection..!!", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onByteMessage(byte[] event) {
        super.onByteMessage(event);
        sendJsonMessage(event);
    }

    public void startReadingCard(byte[] data) {
        sendJsonMessage(data);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        myPref.setPref(Constant.BAUDRATE, baudrate[position]);
//        if (isFirstTime){
//            EventBus.getDefault().post(new UsbSerialActionEvent(ACTION_USB_PERMISSION_GRANTED));
//            connection = usbManager.openDevice(usbdevice);
//            new UsbService.ConnectionThread().run();
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    public void insertDataBase(){
//        myDb.insertJsonData("ovCamp","{ \"protocol\": \"ovCamp\", \"version\": 1.1, \"releaseDate\": \"2014-06-25T00:00:00.000Z\", \"busWake\" : \"00000000000000000000000000000000\", \"headCode\": \"C5 6A 29 \", \"getDeviceID\": { \"cmdLength\": x006, \"cmdWord\": x004, \"ckSum\": x0AA, \"expectReturn\": true }, \"readPPID\": { \"cmdLength\": x007, \"cmdWord\": x008, \"readLength\": x014, \"ckSum\": x0CB, \"writeCMD\": \"00000000000000000000000000000000C56A29070814CB\", \"expectedACK\": \"PPID=AH123123129648\", \"expectReturn\": true } }");
//        myDb.insertJsonData("LUMN","{ \"protocol\": \"LUMN\", \"version\": 1.1, \"releaseDate\": \"2014-06-25T00:00:00.000Z\", \"busWake\" : \">NEW<\", \"getDeviceID\": { \"writeCMD\": \">HAND<\", \"expectedACK\": \"\", \"expectReturn\": true }, \"readPPID\": { \"writeCMD\": \">PPID<\", \"expectedACK\": \"PPID:AH123123129648\", \"expectReturn\": true } }");
//        myDb.insertovPtotocolType("type","");
        myDb.insertlumnData("Handshake",">HAND<");
        myDb.insertlumnData("Read PPID",">PPID<");
        myDb.insertlumnData("Read OEM ID",">OPID<");
        myDb.insertlumnData("Get O/p Control",">OCS<");
        myDb.insertlumnData("Get PAYG Status",">PS<");
        myDb.insertlumnData("Get Remaining Days",">RPD<");
        myDb.insertlumnData("Get Total Number of Days",">TDP<");
        myDb.insertlumnData("Get Last Valid password",">LVC<");
        myDb.insertlumnData("For Exit State",">END<");
        myDb.insertlumnData("ALL Information",">INF<");
        myDb.insertovCampData("Handshake","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 04 AA");
        myDb.insertovCampData("DEVID","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 AF 25");
        myDb.insertovCampData("PPID","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 07 08 14 CB");
        myDb.insertovCampData("HashTop","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 11 08");
        myDb.insertovCampData("OPID","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 07 01 0E 9A");
        myDb.insertovCampData("Remaining_PAYG_Days","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 05 FA");
        myDb.insertovCampData("Days_have_been_running","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 06 16");
        myDb.insertovCampData("PAYG_Days","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 07 48");
        myDb.insertovCampData("PAYG_State","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 09 57");
        myDb.insertovCampData("Output_Control_State","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 0A B5");
        myDb.insertovCampData("System_Status_Code","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 0B EB");
        myDb.insertovCampData("Relative_SOC","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 0C 68");
        myDb.insertovCampData("Remaining_Capacity","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 0D 36");
        myDb.insertovCampData("Full_Charge_Capacity","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 0E D4");
        myDb.insertovCampData("Accu_Energy_Output","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 0F 8A");
        myDb.insertovCampData("Solar_Geberatuibt & Charge_Power","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 18 94");
        myDb.insertovCampData("ACC_Engery_Output & Load_Power","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 17 D5");
        myDb.insertovCampData("Accu_Cycles","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 10 56");
        myDb.insertovCampData("Run_Days_Backup","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 13 B4");
        myDb.insertovCampData("PAYG_REV","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 14 37");
        myDb.insertovCampData("CellVoltage1","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 08 00 3F 02 0B");
        myDb.insertovCampData("CellVoltage2","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 08 00 3E 02 CF");
        myDb.insertovCampData("CellVoltage3","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 08 00 3D 02 9A");
        myDb.insertovCampData("CellVoltage4","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 08 00 3C 02 5E");
        myDb.insertovCampData("BatteryVoltage","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 08 00 09 02 8C");
        myDb.insertovCampData("BatteryCurrent","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 08 00 0A 02 D9");
        myDb.insertovCampData("BatteryTemperature","00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 08 00 08 02 48");
    }

    //        handEveryTime();

//        boolean isConnected = isNetworkConnected();
//        if (!isConnected){


//        }

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                String data = "M4001912000000      ";
//                data = data.replace(" ","");
//                if (!mService.isConnected()) {
//                    if (mBound == false) return;
//                    if (mService.connect()) {
//                        Toast.makeText(HomeActivity.this, "Successfully connected", Toast.LENGTH_SHORT).show();
//                    }
//
//                    mService.subscribe(MQTT_DEFAULT_TOPIC_SUBSCRIBE + data, 2);
//                } else {
//                    mService.subscribe(MQTT_DEFAULT_TOPIC_SUBSCRIBE + data, 2);
//                }
//            }
//        }, 3000);




    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MqttService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

//        bluetoothService.khalas(this);
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, filter);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

//        System.out.println("----------start----------lifecycle"+"onStart invoked");
    }


    @Override
    public void onResume() {
        super.onResume();
        System.gc();
        if (!NO_COMMAND){
            myPref.setPref(Constant.CALL_LUMN_CAMP,false);
            myPref.setPref(Constant.NO_OVCAMP_LUMN,false);
            callLumnCamp();
        }
        if (myPref.getPref(Constant.USB_DISCONNECT,true)){
            startMainActivity();
            myPref.setPref(Constant.CALL_LUMN_CAMP,false);
            myPref.setPref(Constant.NO_OVCAMP_LUMN,false);
            callLumnCamp();
        }

//        String str = "hand start.";
//        stringBuilder1.append("\n"+str);
        System.out.println("--------resume------------lifecycle"+"onResume invoked");
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }

        EventBus.getDefault().unregister(this);
        myPref.setPref(Constant.ALL_TIME_HAND,true);
        myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE,true);
        myPref.setPref(Constant.CALL_LUMN_CAMP,true);
    }

    @Override
    public void onPause() {
        super.onPause();
        myPref.setPref(Constant.ALL_TIME_HAND,true);
        myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE,true);
        myPref.setPref(Constant.CALL_LUMN_CAMP,true);
        System.out.println("--------pause------------lifecycle"+"onPause invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }

        myPref.setPref(Constant.ALL_TIME_HAND,true);
        myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE,true);
        myPref.setPref(Constant.CALL_LUMN_CAMP,true);
    }

    private void goToPermissionCheck() {
        if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[1])) {

                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission

                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Permissions");
                builder.setMessage("Needs storage permissions. Please allow permissions to work application properly.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(HomeActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
                builder.setCancelable(false);
                builder.show();
            }
            else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Permissions");
                builder.setMessage("Needs storage permissions. Please allow permissions to work application properly.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions and grant permission to let App works properly.", Toast.LENGTH_LONG).show();
                    }
                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
                builder.setCancelable(false);
                builder.show();
            }
            else {
                //just request the permission
                ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            startMainActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                startMainActivity();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[1])) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Permissions");
                builder.setMessage("Needs storage permissions. Please allow permissions to work application properly.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(HomeActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });*/
                builder.setCancelable(false);
                builder.show();
            } else {
                this.finish();
                Toast.makeText(getBaseContext(), "Sorry, you have denied permissions.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                startMainActivity();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                startMainActivity();
            }
        }
    }


}
//else {
//        stringBuilder1.append("camp else : " + stringBuilder.toString());
//        String[] separated = stringBuilder.toString().split("C56A291B",54);
//        String onlyforovcamp = "C56A291B"+ separated[1];
//        if (onlyforovcamp.length() == 54){
//        myPref.setPref(Constant.NO_OVCAMP_LUMN, true);
//        myPref.setPref(Constant.LUMN, true);
//        myPref.setPref(Constant.LUMN_USB, true);
//        myPref.setPref(Constant.ALL_TIME_OVCAMP_CODE, true);
//        myPref.setPref(Constant.ALL_TIME_HAND, true);
//        myPref.setPref(Constant.OVCAMP_CODE_START, false);
//        myPref.setPref(Constant.HAND_START, false);
//        myPref.setPref(Constant.CALL_LUMN_CAMP, true);
//        String data = onlyforovcamp.substring(12, 52);
//        stringBuilder1.append("\n " + "PPID in hex1 : " + data);//30364148323030333030303030333C3C3C3C3C3C//06AH2003000003
//        ppidFromovCAMP = ConstantMethod.convertHexToString(data);
//        ppidFromovCAMP = ppidFromovCAMP.replace("<", "");
//        System.out.println("-------ppidFromovCAMP-------" + ppidFromovCAMP);
//        WRITE_OVCAMP_CODE = true;
//        stringBuilder1.append("\n " + "Final PPID ovCamp : " + ppidFromovCAMP);//06AH2003000003
////                    add_PPID_CODE(ppidFromovCAMP);
//        add_PPID_CODE1(ppidFromovCAMP,"ovCamp");
//        stringBuilder = null;
//        }
//        }




//    public void add_PPID_CODE(String PPID){
//        Cursor cursor = contentResolver.query(ToDoProviderConstants.CONTENT_URI_1, null,null,null,null,null);
//
//        String string = "\n" + "Start cursor. ";
//        stringBuilder1.append(string);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                boolean findppid = false;
//                int size = 0;
//                if(cursor != null){
//                    while(cursor.moveToNext()){
//                        size = size + 1;
//                        String ppid = cursor.getString(0);
//                        String codehasformat = cursor.getString(1);
//                        String devicetype = cursor.getString(2);
//                        if (ppid.equals(PPID)){
//                            findppid = true;
//                            EventBus.getDefault().postSticky(new CodeStringEvent(devicetype,ppid,codehasformat));
//                        }
//                    }
//                    if (size == cursor.getCount()){
//                        if (!findppid){
//                            Toast.makeText(HomeActivity.this, "PPID not found.", Toast.LENGTH_LONG).show();
//
//                            if(myPref.getPref(Constant.ALL_TIME_HAND,true) && myPref.getPref(Constant.ALL_TIME_OVCAMP_CODE,true)){
//                                myPref.setPref(Constant.CALL_LUMN_CAMP,false);
//                                myPref.setPref(Constant.NO_OVCAMP_LUMN,false);
//                                stringBuilder1.append("  PPID not found and start main Activity.");
//                                callLumnCamp();
//                            }
//
//                        }
//                        cursor.close();
//                        String string1 = "\n" + " close cursor. ";
//                        stringBuilder1.append(string1);
//                    }
//                }else {
//                    Toast.makeText(HomeActivity.this, "First save code.", Toast.LENGTH_SHORT).show();
//                    String string6 = "\n" + " cursor is nulll ";
//                    stringBuilder1.append(string6);
//                }
//
//            }
//        },2000);

//        String str1 = myPref.getPref3("todolist",toDoList);
//        if (str1 != null){
//            try {
//                boolean findppid = false;
//                toDoList = new ArrayList<>();
//                JSONArray jsonArray = new JSONArray(str1);
//                for (int i = 0; i < jsonArray.length(); i++){
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    String ppid = jsonObject.getString("PPID");
//                    String codehasformate = jsonObject.getString("codehasformate");
//                    String type = jsonObject.getString("type");
//                    ToDo toDo = new ToDo(ppid,codehasformate,type);
//                    toDoList.add(toDo);
//                    if (ppid.equals(PPID)){
//                        findppid = true;
//                        EventBus.getDefault().postSticky(new CodeStringEvent(type,ppid,codehasformate));
//                    }
//                }
//                newAdapter.notifyList(toDoList);
//
//                if (!findppid){
//                    Toast.makeText(HomeActivity.this, "PPID not found.", Toast.LENGTH_LONG).show();
//                    if(myPref.getPref(Constant.ALL_TIME_HAND,true) && myPref.getPref(Constant.ALL_TIME_OVCAMP_CODE,true)){
//                        myPref.setPref(Constant.CALL_LUMN_CAMP,false);
//                        myPref.setPref(Constant.NO_OVCAMP_LUMN,false);
//                        stringBuilder1.append("  PPID not found and start main Activity.");
//                        callLumnCamp();
//                    }
//                }
//
//
//            } catch (JSONException e) {
//
//            }
//        }

//    }

//    File csvfile = new File(Environment.getExternalStorageDirectory() + "test.sh");

//    CSVReader reader = new CSVReader(new FileReader(csvfile.getAbsolutePath()));

//    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
//        try {
//            File root = new File(Environment.getExternalStorageDirectory()+"/Download");
//            if (!root.exists()) {
//                root.mkdirs();
//            }
//            File gpxfile = new File(root, sFileName);
//            FileWriter writer = new FileWriter(gpxfile);
//            writer.append(sBody);
//            writer.flush();
//            writer.close();
//            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


//    int toastDurationInMilliSeconds = 2000;
//    Toast mShow = Toast.makeText(ActiveDeviceActivity.this, "Top up successfully for PPID " + PPID, Toast.LENGTH_LONG);
//    CountDownTimer toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
//        public void onTick(long millisUntilFinished) {
//            mShow.show();
//        }
//        public void onFinish() {
//            mShow.cancel();
//        }
//    };
//                    toastCountDown.start();