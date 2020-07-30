package com.sparkle.devicescanner.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.sparkle.devicescanner.Model.WifiName;
import com.sparkle.devicescanner.Utils.Constant;
import com.sparkle.devicescanner.Utils.MyPref;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WifiService extends Service {

    WifiManager mainWifiObj;
    private final IBinder mBinder = new LocalBinder();
    MyPref myPref;
    String wifis[];
    List<String> stringList;
    List<String> list;

    public class LocalBinder extends Binder {
        public WifiService getService() {
            return WifiService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(getApplicationContext(), "done111", Toast.LENGTH_SHORT).show();
//        Handler handler= new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(), "done111", Toast.LENGTH_SHORT).show();
//            }
//        },30000);
//        scanWifi(getApplicationContext());
        return START_STICKY;
    }

    public void scanWifi(Context context){

        myPref = new MyPref(context);
        mainWifiObj = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mainWifiObj.startScan();
//        Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();
        getScanResult(context);
//        Handler handler= new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(context, "done111", Toast.LENGTH_SHORT).show();
//            }
//        },30000);

//        System.out.println("---------------------scanning done");
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("restartservice");
//        broadcastIntent.setClass(context, Restarter.class);
//        context.sendBroadcast(broadcastIntent);

    }
    public List<ScanResult> getScanResult(Context context){

        mainWifiObj = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        stringList = new ArrayList<>();
        myPref = new MyPref(context);
        JSONObject jsonObject = new JSONObject();
        List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
        wifis = new String[wifiScanList.size()];
        for(int i = 0; i < wifiScanList.size(); i++){
            wifis[i] = ((wifiScanList.get(i)).toString());
        }

        String filtered[] = new String[wifiScanList.size()];
        list = new ArrayList<>();
        myPref.setPref(Constant.WIFI_DEVICE_LIST, list);

        int counter = 0;
        for (String eachWifi : wifis) {
            String[] temp = eachWifi.split(",");
            filtered[counter] = temp[0].substring(5).trim();//+"\n" + temp[2].substring(12).trim()+"\n" +temp[3].substring(6).trim();//0->SSID, 2->Key Management 3-> Strength
            stringList.add(counter,filtered[counter]);
            try {
                jsonObject.put("hello",filtered[counter]);

//                System.out.println("-------------------"+jsonObject);
                list.add(String.valueOf(jsonObject));

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            counter++;
        }

//        EventBus.getDefault().post(wifiScanList);
        myPref.setPref(Constant.WIFI_DEVICE_LIST, list);
//        System.out.println("----------jo---------"+list);
        return wifiScanList;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("restartservice");
//        broadcastIntent.setClass(this, Restarter.class);
//        this.sendBroadcast(broadcastIntent);
//        Toast.makeText(getApplicationContext(), "destory", Toast.LENGTH_SHORT).show();
    }
}
