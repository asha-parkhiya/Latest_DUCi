package com.sparkle.devicescanner.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.sparkle.devicescanner.Utils.MyPref;

import java.util.Timer;

public  class TimerService extends Service {

    private final IBinder mBinder = new LocalBinder();
    MyPref myPref;
    Timer timer = new Timer();
    Thread thread;

    WifiService wifiService;

    public class LocalBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


//        public void setRepeating(Context context){
//
//            long period = 0;
//            myPref = new MyPref(context);
//            timer = null;
//            timer = new Timer();
//            wifiService = new WifiService();
//            bluetoothService = new BluetoothService();
//            if (myPref.getPref(Constant.MINUTE,period) == Constant.TWO_MINUTE){
//                period = Constant.TWO_MINUTE;
//            }if (myPref.getPref(Constant.MINUTE,period) == Constant.FIVE_MINUTE){
//                period = Constant.FIVE_MINUTE;
//            }if (myPref.getPref(Constant.MINUTE,period) == Constant.EIGHT_MINUTE){
//                period = Constant.EIGHT_MINUTE;
//            }if (myPref.getPref(Constant.MINUTE,period) == Constant.TEN_MINUTE){
//                period = Constant.TEN_MINUTE;
//            }
//
//                TimerTask timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    thread = new Thread() {
//                        public void run() {
//
//                            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    wifiService.scanWifi(context);
//                                    bluetoothService.scanBluetooth(context);
//                                }
//                            });
//                        }
//                    };
//                    thread.start();
//                }
//            };
//            timer.schedule(timerTask,0,period);
//
//    }


    public void stoptimer(Context context){
//        context.stopService(new Intent(context, BluetoothService.class));
        context.stopService(new Intent(context, WifiService.class));
        timer.cancel();
        timer.purge();
        timer = null;
    }

}
