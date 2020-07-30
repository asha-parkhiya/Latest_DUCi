package com.sparkle.devicescanner.Activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.sparkle.devicescanner.Model.WifiName;
import com.sparkle.devicescanner.Model.WifiStr;
import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.Constant;
import com.sparkle.devicescanner.Utils.MyPref;
import com.sparkle.devicescanner.Utils.RippleEffect;
import com.sparkle.devicescanner.events.UsbSerialStringEvent;
import com.sparkle.devicescanner.services.MqttService;
import com.sparkle.devicescanner.services.WifiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RippleActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView foundDevice1,foundDevice2,foundDevice3,foundDevice4,foundDevice5,foundDevice6,foundDevice7,foundDevice8;
    private Button btn_second;
    private RelativeLayout rl_foundDevice1,rl_foundDevice2,rl_foundDevice3,rl_foundDevice4,rl_foundDevice5,rl_foundDevice6,rl_foundDevice7,rl_foundDevice8;
    private TextView foundDevicetext1,foundDevicetext2,foundDevicetext3,foundDevicetext4,foundDevicetext5,foundDevicetext6,foundDevicetext7,foundDevicetext8;
    private RelativeLayout rl_final;

    private final String MQTT_DEFAULT_TOPIC_SUBSCRIBE = "_OV1/GPRS/camp/";

    ListView list;
    String wifis[];
    String filtered[];

    Toolbar toolbar;

    final int min = 30;
    final int max = 600;

    EditText pass;

    MyPref myPref;
    List<String> stringList = new ArrayList<>();
    final Handler handler=new Handler();

    MqttService mService;
    boolean mBound = false;
    WifiScanReceiver wifiReciever;
    WifiService wifiService;
    boolean wifiBound = false;
    Intent mServiceIntent;
    Gson gson;
    WifiName wifiName;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MqttService.LocalBinder binder = (MqttService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

//    private ServiceConnection wifiConection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
//            WifiService.LocalBinder wifibinder = (WifiService.LocalBinder) service;
//            wifiService = wifibinder.getService();
//            wifiBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            wifiBound = false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple);

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("Scanning Wifi");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        gson = new Gson();
        rl_foundDevice1 = (RelativeLayout) findViewById(R.id.rl_foundDevice1);
        rl_foundDevice2 = (RelativeLayout) findViewById(R.id.rl_foundDevice2);
        rl_foundDevice3 = (RelativeLayout) findViewById(R.id.rl_foundDevice3);
        rl_foundDevice4 = (RelativeLayout) findViewById(R.id.rl_foundDevice4);
        rl_foundDevice5 = (RelativeLayout) findViewById(R.id.rl_foundDevice5);
        rl_foundDevice6 = (RelativeLayout) findViewById(R.id.rl_foundDevice6);
        rl_foundDevice7 = (RelativeLayout) findViewById(R.id.rl_foundDevice7);
        rl_foundDevice8 = (RelativeLayout) findViewById(R.id.rl_foundDevice8);

        foundDevice1 = (ImageView) findViewById(R.id.foundDevice1);
        foundDevice2 = (ImageView) findViewById(R.id.foundDevice2);
        foundDevice3 = (ImageView) findViewById(R.id.foundDevice3);
        foundDevice4 = (ImageView) findViewById(R.id.foundDevice4);
        foundDevice5 = (ImageView) findViewById(R.id.foundDevice5);
        foundDevice6 = (ImageView) findViewById(R.id.foundDevice6);
        foundDevice7 = (ImageView) findViewById(R.id.foundDevice7);
        foundDevice8 = (ImageView) findViewById(R.id.foundDevice8);

        foundDevicetext1 = (TextView)findViewById(R.id.foundDevicetext1);
        foundDevicetext2 = (TextView)findViewById(R.id.foundDevicetext2);
        foundDevicetext3 = (TextView)findViewById(R.id.foundDevicetext3);
        foundDevicetext4 = (TextView)findViewById(R.id.foundDevicetext4);
        foundDevicetext5 = (TextView)findViewById(R.id.foundDevicetext5);
        foundDevicetext6 = (TextView)findViewById(R.id.foundDevicetext6);
        foundDevicetext7 = (TextView)findViewById(R.id.foundDevicetext7);
        foundDevicetext8 = (TextView)findViewById(R.id.foundDevicetext8);

        foundDevice1.setOnClickListener(this);
        foundDevice2.setOnClickListener(this);
        foundDevice3.setOnClickListener(this);
        foundDevice4.setOnClickListener(this);
        foundDevice5.setOnClickListener(this);
        foundDevice6.setOnClickListener(this);
        foundDevice7.setOnClickListener(this);
        foundDevice8.setOnClickListener(this);

        rl_final = (RelativeLayout)findViewById(R.id.rl_final);
        ImageView button=(ImageView)findViewById(R.id.centerImage);
        myPref = new MyPref(this);

        wifiReciever = new WifiScanReceiver();
        wifiService = new WifiService();
        mServiceIntent = new Intent(getApplicationContext(), wifiService.getClass());

//        list=getListView();
//        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        final RippleEffect rippleBackground=(RippleEffect) findViewById(R.id.content);

        rippleBackground.startRippleAnimation();

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//        we are connected to a network
            connected = true;

            handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isMyServiceRunning(wifiService.getClass())) {
                    startService(mServiceIntent);
                    wifiService.scanWifi(RippleActivity.this);
                }else{
                    System.out.println("----------------nulll");
                }
            }
        },3000);
        }
        else{
            Toast.makeText(RippleActivity.this, "Please check network connection..", Toast.LENGTH_SHORT).show();
            connected = false;
        }



//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                // selected item
//                String ssid = ((TextView) view).getText().toString();
//                connectToWifi(ssid);
//                Toast.makeText(RippleActivity.this,"Wifi SSID : "+ssid,Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//        if (mWifi.isConnected()) {
//            System.out.println("---------------------im connected...");
//            // Do whatever
//        }else{
//            System.out.println("---------------------im not connected...");
//        }

    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.foundDevice1:
                toast(stringList.get(0));
                connectToWifi(stringList.get(0));
                break;
            case R.id.foundDevice2:
                toast(stringList.get(1));
                connectToWifi(stringList.get(1));
                break;
            case R.id.foundDevice3:
                toast(stringList.get(2));
                connectToWifi(stringList.get(2));
                break;
            case R.id.foundDevice4:
                toast(stringList.get(3));
                connectToWifi(stringList.get(3));
                break;
            case R.id.foundDevice5:
                toast(stringList.get(4));
                connectToWifi(stringList.get(4));
                break;
            case R.id.foundDevice6:
                toast(stringList.get(5));
                connectToWifi(stringList.get(5));
                break;
            case R.id.foundDevice7:
                toast(stringList.get(6));
                connectToWifi(stringList.get(6));
                break;
            case R.id.foundDevice8:
                toast(stringList.get(7));
                connectToWifi(stringList.get(7));
                break;

        }
    }

    public void toast(String string){
        Toast.makeText(RippleActivity.this, "Wifi SSID : "+string, Toast.LENGTH_SHORT).show();
    }

    class WifiScanReceiver extends BroadcastReceiver {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @SuppressLint({"UseValueOf", "ResourceType"})
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> wifiScanList = wifiService.getScanResult(getApplicationContext());
//                System.out.println("---------------"+mainWifiObj.getScanResults());


                wifis = new String[wifiScanList.size()];
                for(int i = 0; i < wifiScanList.size(); i++){
                    wifis[i] = ((wifiScanList.get(i)).toString());

                }

                String filtered[] = new String[wifiScanList.size()];
                int counter = 0;
                for (String eachWifi : wifis) {
                    String[] temp = eachWifi.split(",");
                    filtered[counter] = temp[0].substring(5).trim();//+"\n" + temp[2].substring(12).trim()+"\n" +temp[3].substring(6).trim();//0->SSID, 2->Key Management 3-> Strength
                    stringList.add(counter,filtered[counter]);
                    System.out.println("-----------------"+filtered[counter]);
                    counter++;
                }
//                stringList = wifiScanList;
//
//                filtered = wifiScanList.toArray(new String[0]);

                System.out.println("------------"+wifiScanList.size());
                if (wifiScanList.size() == 0){
                    Toast.makeText(RippleActivity.this, "Please allow first location permission..!!", Toast.LENGTH_SHORT).show();
                }
                if (wifiScanList.size() == 1){
                    foundDevice1();
                    foundDevicetext1.setText(filtered[0]);
                }else if (wifiScanList.size() == 2){
                    foundDevice1();foundDevice2();
                    foundDevicetext1.setText(filtered[0]);
                    foundDevicetext2.setText(filtered[1]);
                }else if (wifiScanList.size() == 3){
                    foundDevice1();foundDevice2();foundDevice3();
                    foundDevicetext1.setText(filtered[0]);
                    foundDevicetext2.setText(filtered[1]);
                    foundDevicetext3.setText(filtered[2]);
                }else if (wifiScanList.size() == 4){
                    foundDevice1();foundDevice2();foundDevice3();foundDevice4();
                    foundDevicetext1.setText(filtered[0]);
                    foundDevicetext2.setText(filtered[1]);
                    foundDevicetext3.setText(filtered[2]);
                    foundDevicetext4.setText(filtered[3]);
                } else if (wifiScanList.size() == 5){
                    foundDevice1();foundDevice2();foundDevice3();foundDevice4();foundDevice5();
                    foundDevicetext1.setText(filtered[0]);
                    foundDevicetext2.setText(filtered[1]);
                    foundDevicetext3.setText(filtered[2]);
                    foundDevicetext4.setText(filtered[3]);
                    foundDevicetext5.setText(filtered[4]);
                }else if (wifiScanList.size() == 6){
                    foundDevice1();foundDevice2();foundDevice3();foundDevice4();foundDevice5();foundDevice6();
                    foundDevicetext1.setText(filtered[0]);
                    foundDevicetext2.setText(filtered[1]);
                    foundDevicetext3.setText(filtered[2]);
                    foundDevicetext4.setText(filtered[3]);
                    foundDevicetext5.setText(filtered[4]);
                    foundDevicetext6.setText(filtered[5]);
                }else if (wifiScanList.size() == 7){
                    foundDevice1();foundDevice2();foundDevice3();foundDevice4();foundDevice5();foundDevice6();foundDevice7();
                    foundDevicetext1.setText(filtered[0]);
                    foundDevicetext2.setText(filtered[1]);
                    foundDevicetext3.setText(filtered[2]);
                    foundDevicetext4.setText(filtered[3]);
                    foundDevicetext5.setText(filtered[4]);
                    foundDevicetext6.setText(filtered[5]);
                    foundDevicetext7.setText(filtered[6]);
                }else if (wifiScanList.size() == 8){
                    foundDevice1();foundDevice2();foundDevice3();foundDevice4();foundDevice5();foundDevice6();foundDevice7();foundDevice8();
                    foundDevicetext1.setText(filtered[0]);
                    foundDevicetext2.setText(filtered[1]);
                    foundDevicetext3.setText(filtered[2]);
                    foundDevicetext4.setText(filtered[3]);
                    foundDevicetext5.setText(filtered[4]);
                    foundDevicetext6.setText(filtered[5]);
                    foundDevicetext7.setText(filtered[6]);
                    foundDevicetext8.setText(filtered[7]);
                }else if(wifiScanList.size() >= 9) {
                    foundDevice1();foundDevice2();foundDevice3();foundDevice4();foundDevice5();foundDevice6();foundDevice7();foundDevice8();
                    foundDevicetext1.setText(filtered[0]);
                    foundDevicetext2.setText(filtered[1]);
                    foundDevicetext3.setText(filtered[2]);
                    foundDevicetext4.setText(filtered[3]);
                    foundDevicetext5.setText(filtered[4]);
                    foundDevicetext6.setText(filtered[5]);
                    foundDevicetext7.setText(filtered[6]);
                    foundDevicetext8.setText(filtered[7]);

                    List<String> strings = new ArrayList<>();
                    strings.add(0,filtered[0]);
                    strings.add(1,filtered[1]);
                    strings.add(2,filtered[2]);
                    strings.add(3,filtered[3]);
                    strings.add(4,filtered[4]);
                    strings.add(5,filtered[5]);
                    strings.add(6,filtered[6]);
                    strings.add(7,filtered[7]);
                    strings.add(8,filtered[8]);

                    rl_final.removeAllViews();
                    for(int i = 8; i < wifiScanList.size(); i++){
                        RelativeLayout layout= new RelativeLayout(RippleActivity.this);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(ThreadLocalRandom.current().nextInt(min, max),ThreadLocalRandom.current().nextInt(min, max),ThreadLocalRandom.current().nextInt(min, max),ThreadLocalRandom.current().nextInt(min, max));
                        layout.setLayoutParams(layoutParams);

                        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(70,70);
                        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        ImageView imageView = new ImageView(RippleActivity.this);
                        imageView.setId(1);
                        imageView.setImageResource(R.drawable.ic_placeholder);

                        TextView tv3 = new TextView(RippleActivity.this);
                        params2.addRule(RelativeLayout.BELOW, imageView.getId());
                        tv3.setId(2);
                        tv3.setText(filtered[i]);
                        strings.add(i,tv3.getText().toString());
                        tv3.setTextSize(11);
                        tv3.setMaxLines(1);
                        tv3.setTextColor(Color.parseColor("#17a2b8"));
                        tv3.setFontFeatureSettings("sans-serif");
                        tv3.setGravity(Gravity.CENTER);

                        layout.addView(imageView, params1);
                        layout.addView(tv3, params2);
                        rl_final.addView(layout);

                        System.out.println("----------------"+strings.get(i));
                        int finalI = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                              String ssid = finalTv.getText().toString();
                                Toast.makeText(RippleActivity.this,"Wifi SSID : "+strings.get(finalI),Toast.LENGTH_SHORT).show();
                                connectToWifi(strings.get(finalI));
                            }
                        });
                    }
                }

//                list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item,R.id.label, filtered));
            }else {
                System.out.println("----------------nononono");
            }

        }
    }

    private void finallyConnect(String networkPass, String networkSSID) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

//        // remember id
//        int netId = mainWifiObj.addNetwork(wifiConfig);
//        mainWifiObj.disconnect();
//        mainWifiObj.enableNetwork(netId, true);
//        mainWifiObj.reconnect();
//
//        WifiConfiguration conf = new WifiConfiguration();
//        conf.SSID = "\"\"" + networkSSID + "\"\"";
//        conf.preSharedKey = "\"" + networkPass + "\"";
//        mainWifiObj.addNetwork(conf);
//        myPref.setPref(Constant.NETWORK_SSID,networkSSID);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getCurrentSsid(RippleActivity.this) != null){
                    Toast.makeText(RippleActivity.this, "Connect with "+networkSSID, Toast.LENGTH_SHORT).show();
                    if (myPref.getPref(Constant.TOGGLE_POSITION, true)){
                        if (!mService.isConnected()) {
                            if (mBound == false) return;
                            if (mService.connect()) {
                                Toast.makeText(RippleActivity.this, "Successfully connected", Toast.LENGTH_SHORT).show();
                            }
                            mService.publish(MQTT_DEFAULT_TOPIC_SUBSCRIBE ,"hello");
                        } else {
                            mService.publish(MQTT_DEFAULT_TOPIC_SUBSCRIBE , "hello");
                        }
                    }

                }else{
                    Toast.makeText(RippleActivity.this, "Device not connected...!!", Toast.LENGTH_SHORT).show();
                }

            }
        },2000);



    }

    private void connectToWifi(final String wifiSSID) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.connect);
        dialog.setTitle("Connect to Network");
        TextView textSSID = (TextView) dialog.findViewById(R.id.textSSID1);
        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        pass = (EditText) dialog.findViewById(R.id.textPassword);
        textSSID.setText("Connect Wifi : "+wifiSSID);

        // if button is clicked, connect to the network;
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkPassword = pass.getText().toString();
                finallyConnect(checkPassword, wifiSSID);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void foundDevice1(){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rl_foundDevice1, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rl_foundDevice1, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        rl_foundDevice1.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    private void foundDevice2(){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rl_foundDevice2, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rl_foundDevice2, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        rl_foundDevice2.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    private void foundDevice3(){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rl_foundDevice3, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rl_foundDevice3, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        rl_foundDevice3.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    private void foundDevice4(){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rl_foundDevice4, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rl_foundDevice4, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        rl_foundDevice4.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    private void foundDevice5(){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rl_foundDevice5, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rl_foundDevice5, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        rl_foundDevice5.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    private void foundDevice6(){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rl_foundDevice6, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rl_foundDevice6, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        rl_foundDevice6.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    private void foundDevice7(){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rl_foundDevice7, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rl_foundDevice7, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        rl_foundDevice7.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    private void foundDevice8(){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rl_foundDevice8, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rl_foundDevice8, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        rl_foundDevice8.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    public static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
                System.out.println("-------"+ssid);
            }
        }
        return ssid;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MqttService.class);
//        Intent intent1 = new Intent(this, WifiService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
//        bindService(intent1, wifiConection, Context.BIND_AUTO_CREATE);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UsbSerialStringEvent event) {

    }
    @Override
    public void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
//        if (wifiBound){
//            unbindService(wifiConection);
//            wifiBound = false;
//        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        super.onDestroy();
    }
}
