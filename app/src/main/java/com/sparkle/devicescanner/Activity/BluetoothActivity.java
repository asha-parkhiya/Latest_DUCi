package com.sparkle.devicescanner.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.Constant;
import com.sparkle.devicescanner.Utils.MyPref;
import com.sparkle.devicescanner.Utils.RippleEffect;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    ListView list;
    private List<String> mDeviceList = new ArrayList<String>();
    private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;
    private ImageView foundDevice1,foundDevice2,foundDevice3,foundDevice4,foundDevice5,foundDevice6,foundDevice7,foundDevice8;
    private Button btn_second;
    private RelativeLayout rl_foundDevice1,rl_foundDevice2,rl_foundDevice3,rl_foundDevice4,rl_foundDevice5,rl_foundDevice6,rl_foundDevice7,rl_foundDevice8;
    private TextView foundDevicetext1,foundDevicetext2,foundDevicetext3,foundDevicetext4,foundDevicetext5,foundDevicetext6,foundDevicetext7,foundDevicetext8;
    private RelativeLayout rl_final;
    MyPref myPref;
    final Handler handler=new Handler();
    final int min = 30;
    final int max = 600;
    BluetoothDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloetooth);
//        list = (ListView)findViewById(R.id.list);
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("Scanning Bluetooth");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

        final RippleEffect rippleBackground=(RippleEffect) findViewById(R.id.content);

        rippleBackground.startRippleAnimation();

        myPref.setPref(Constant.BLUETOOTH_DEVICE_LIST,mDeviceList);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable();
                mBluetoothAdapter.startDiscovery();
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter);

        }else {
                mBluetoothAdapter.startDiscovery();
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter);
        }



    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Log.d("pairDevice()", "Start Pairing...");
            Method m = device.getClass().getMethod("createBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
            Log.d("pairDevice()", "Pairing finished.");
        } catch (Exception e) {
        }
    }


    //For UnPairing
    private void unpairDevice(BluetoothDevice device) {
        try {
            Log.d("unpairDevice()", "Start Un-Pairing...");
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
            Log.d("unpairDevice()", "Un-Pairing finished.");
        } catch (Exception e) {
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @SuppressLint("ResourceType")
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothDeviceList.add(device);
                mDeviceList.add(device.getName());
//                EventBus.getDefault().post(bluetoothDeviceList);
                System.out.println("------------------"+mDeviceList.size());
                myPref.setPref(Constant.BLUETOOTH_DEVICE_LIST,mDeviceList);
//                Log.i("BT", device.getName() + "\n" + device.getAddress());
//                list.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mDeviceList));

                if (mDeviceList.size() == 0){
                    Toast.makeText(BluetoothActivity.this, "Please turn on Bluetooth..!!", Toast.LENGTH_SHORT).show();
                }
                if (mDeviceList.size() == 1){
                    foundDevice1();
                    foundDevicetext1.setText(mDeviceList.get(0));
                }else if (mDeviceList.size() == 2){
                    foundDevice1();foundDevice2();
                    foundDevicetext1.setText(mDeviceList.get(0));
                    foundDevicetext2.setText(mDeviceList.get(1));
                }else if (mDeviceList.size() == 3){
                    foundDevice1();foundDevice2();foundDevice3();
                    foundDevicetext1.setText(mDeviceList.get(0));
                    foundDevicetext2.setText(mDeviceList.get(1));
                    foundDevicetext3.setText(mDeviceList.get(2));
                }else if (mDeviceList.size() == 4){
                    foundDevice1();foundDevice2();foundDevice3();foundDevice4();
                    foundDevicetext1.setText(mDeviceList.get(0));
                    foundDevicetext2.setText(mDeviceList.get(1));
                    foundDevicetext3.setText(mDeviceList.get(2));
                    foundDevicetext4.setText(mDeviceList.get(3));
                } else if (mDeviceList.size() == 5){
                    foundDevice1();foundDevice2();foundDevice3();foundDevice4();foundDevice5();
                    foundDevicetext1.setText(mDeviceList.get(0));
                    foundDevicetext2.setText(mDeviceList.get(1));
                    foundDevicetext3.setText(mDeviceList.get(2));
                    foundDevicetext4.setText(mDeviceList.get(3));
                    foundDevicetext5.setText(mDeviceList.get(4));
                }else if (mDeviceList.size() == 6){
                    foundDevice1();foundDevice2();foundDevice3();foundDevice4();foundDevice5();foundDevice6();
                    foundDevicetext1.setText(mDeviceList.get(0));
                    foundDevicetext2.setText(mDeviceList.get(1));
                    foundDevicetext3.setText(mDeviceList.get(2));
                    foundDevicetext4.setText(mDeviceList.get(3));
                    foundDevicetext5.setText(mDeviceList.get(4));
                    foundDevicetext6.setText(mDeviceList.get(5));
                }else if (mDeviceList.size() == 7){
                    foundDevice1();foundDevice2();foundDevice3();foundDevice4();foundDevice5();foundDevice6();foundDevice7();
                    foundDevicetext1.setText(mDeviceList.get(0));
                    foundDevicetext2.setText(mDeviceList.get(1));
                    foundDevicetext3.setText(mDeviceList.get(2));
                    foundDevicetext4.setText(mDeviceList.get(3));
                    foundDevicetext5.setText(mDeviceList.get(4));
                    foundDevicetext6.setText(mDeviceList.get(5));
                    foundDevicetext7.setText(mDeviceList.get(6));
                }else if (mDeviceList.size() == 8){
                    foundDevice1();foundDevice2();foundDevice3();foundDevice4();foundDevice5();foundDevice6();foundDevice7();foundDevice8();
                    foundDevicetext1.setText(mDeviceList.get(0));
                    foundDevicetext2.setText(mDeviceList.get(1));
                    foundDevicetext3.setText(mDeviceList.get(2));
                    foundDevicetext4.setText(mDeviceList.get(3));
                    foundDevicetext5.setText(mDeviceList.get(4));
                    foundDevicetext6.setText(mDeviceList.get(5));
                    foundDevicetext7.setText(mDeviceList.get(6));
                    foundDevicetext8.setText(mDeviceList.get(7));
                }else if(mDeviceList.size() >= 9) {
                    foundDevice1();foundDevice2();foundDevice3();foundDevice4();foundDevice5();foundDevice6();foundDevice7();foundDevice8();
                    foundDevicetext1.setText(mDeviceList.get(0));
                    foundDevicetext2.setText(mDeviceList.get(1));
                    foundDevicetext3.setText(mDeviceList.get(2));
                    foundDevicetext4.setText(mDeviceList.get(3));
                    foundDevicetext5.setText(mDeviceList.get(4));
                    foundDevicetext6.setText(mDeviceList.get(5));
                    foundDevicetext7.setText(mDeviceList.get(6));
                    foundDevicetext8.setText(mDeviceList.get(7));

                    List<String> strings = new ArrayList<>();
                    strings.add(0,mDeviceList.get(0));
                    strings.add(1,mDeviceList.get(1));
                    strings.add(2,mDeviceList.get(2));
                    strings.add(3,mDeviceList.get(3));
                    strings.add(4,mDeviceList.get(4));
                    strings.add(5,mDeviceList.get(5));
                    strings.add(6,mDeviceList.get(6));
                    strings.add(7,mDeviceList.get(7));
                    strings.add(8,mDeviceList.get(8));

                    rl_final.removeAllViews();
                    for(int i = 8; i < mDeviceList.size(); i++){
                        RelativeLayout layout= new RelativeLayout(BluetoothActivity.this);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(ThreadLocalRandom.current().nextInt(min, max),ThreadLocalRandom.current().nextInt(min, max),ThreadLocalRandom.current().nextInt(min, max),ThreadLocalRandom.current().nextInt(min, max));
                        layout.setLayoutParams(layoutParams);

                        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(70,70);
                        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        ImageView imageView = new ImageView(BluetoothActivity.this);
                        imageView.setId(1);
                        imageView.setImageResource(R.drawable.ic_bluetooth_logo_with_background);

                        TextView tv3 = new TextView(BluetoothActivity.this);
                        params2.addRule(RelativeLayout.BELOW, imageView.getId());
                        tv3.setId(2);
                        tv3.setText(mDeviceList.get(i));
                        strings.add(i,tv3.getText().toString());
                        tv3.setTextSize(11);
                        tv3.setMaxLines(1);
                        tv3.setTextColor(Color.parseColor("#17a2b8"));
                        tv3.setFontFeatureSettings("sans-serif");
                        tv3.setGravity(Gravity.CENTER);

                        layout.addView(imageView, params1);
                        layout.addView(tv3, params2);
                        rl_final.addView(layout);

//                        System.out.println("----------------"+strings.get(i));
                        int finalI = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                              String ssid = finalTv.getText().toString();
                                Toast.makeText(BluetoothActivity.this,"Bluetooth ID : "+strings.get(finalI),Toast.LENGTH_SHORT).show();
                                pairDevice(bluetoothDeviceList.get(finalI));
                            }
                        });
                    }
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.foundDevice1:
                toast(mDeviceList.get(0),0);
//                unpairDevice(bluetoothDeviceList.get(0));
                pairDevice(bluetoothDeviceList.get(0));
                break;
            case R.id.foundDevice2:
                toast(mDeviceList.get(1),1);
                pairDevice(bluetoothDeviceList.get(1));
                break;
            case R.id.foundDevice3:
                toast(mDeviceList.get(2),2);
                pairDevice(bluetoothDeviceList.get(2));
                break;
            case R.id.foundDevice4:
                toast(mDeviceList.get(3),3);
                pairDevice(bluetoothDeviceList.get(3));
                break;
            case R.id.foundDevice5:
                toast(mDeviceList.get(4),4);
                pairDevice(bluetoothDeviceList.get(4));
                break;
            case R.id.foundDevice6:
                toast(mDeviceList.get(5),5);
                pairDevice(bluetoothDeviceList.get(5));
                break;
            case R.id.foundDevice7:
                toast(mDeviceList.get(6),6);
                pairDevice(bluetoothDeviceList.get(6));
                break;
            case R.id.foundDevice8:
                toast(mDeviceList.get(7),7);
                pairDevice(bluetoothDeviceList.get(7));
                break;
        }

    }

    public void toast(String string, int i){
        Toast.makeText(BluetoothActivity.this, "Bluetooth ID : "+i+" "+string, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageStringEvent(String event) {}
}
