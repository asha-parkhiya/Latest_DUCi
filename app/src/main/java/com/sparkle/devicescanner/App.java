package com.sparkle.devicescanner;

import android.app.Application;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

public class App extends Application {

    public static App appInstance;
    private SimpleDateFormat dateFormat;
    ImageView btn_scan;
    Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();


        appInstance = this;

        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


    }

//    public void afficher() {
//        Toast.makeText(appInstance, ""+dateFormat.format(new Date()), Toast.LENGTH_SHORT).show();
////        Toast.makeText(getBaseContext(), dateFormat.format(new Date())).show();
//        handler.postDelayed(runnable,5000);
//    }
//
//    public void startTimer() {
//        runnable.run();
//    }
//
//    public void stopTimer() {
//        handler.removeCallbacks(runnable);
//    }


//    Runnable runnable = new Runnable() {
//        public void run() {
//            afficher();
//        }
//    };

}