package com.sparkle.devicescanner.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.sparkle.devicescanner.Activity.HomeActivity;
import com.sparkle.devicescanner.Activity.RippleActivity;
import com.sparkle.devicescanner.Activity.SettingActivity;

import java.util.Timer;

public class WifiBroadCastReceiver extends BroadcastReceiver {
    SettingActivity settingActivity;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
//        settingActivity = new SettingActivity();
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void run() {
//                settingActivity.setAlarm(context,0);
//
//            }
//        },5000);

//        Toast.makeText(context, "hellooooooo", Toast.LENGTH_SHORT).show();
//        settingActivity.setAlarm(context,1000*10);
    }
}
