package com.sparkle.devicescanner.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sparkle.devicescanner.Utils.Constant;
import com.sparkle.devicescanner.Utils.MyPref;

public class HelloIntentService extends Service {

    // 5 seconds by default, can be changed later
    private Handler mHandler;
    Context context;
    MyPref myPref;

    public HelloIntentService(Context context) {
        this.context = context;
        myPref = new MyPref(context);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        startRepeatingTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
//                Toast.makeText(context, "hellllo", Toast.LENGTH_SHORT).show();
//                updateStatus(); //this function can change value of mInterval.
            } finally {
//                Toast.makeText(context, "hellllo11111", Toast.LENGTH_SHORT).show();
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
//                mHandler.postDelayed(mStatusChecker, myPref.getPref(Constant.HELLLOOOO,0));
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
}