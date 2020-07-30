package com.sparkle.devicescanner.Activity;

import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sparkle.devicescanner.ContentProvider.ToDoProviderConstants;
import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.CodeErrorDialog;
import com.sparkle.devicescanner.Utils.Constant;
import com.sparkle.devicescanner.Utils.MyPref;
import com.sparkle.devicescanner.events.UsbSerialStringEvent;
import com.sparkle.devicescanner.services.MqttService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, CodeErrorDialog.OnButtonclick {

    private Toolbar toolbar;
    private Spinner spinner;
    private Spinner spinner1;
    private Spinner spinner2;
    private static final String[] paths = { "4000 ms", "3000 ms", "2000 ms"};
    private static final String[] paths2 = { "Lumn", "ovCamp"};
//    private ToggleButton toggleBtn;
    private MyPref myPref;

    AlarmManager manager;
    Button connection_info, btn_restart,connection_info1;
    StringBuilder stringBuilder1;
    CircleImageView image_status;

    MqttService mService;
    boolean mBound = false;
    private ContentResolver contentResolver;
    StringBuilder stringBuilder;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.connection_info:
                CodeErrorDialog codeErrorDialog = new CodeErrorDialog(SettingActivity.this, "LOG: "+stringBuilder1.toString(), null, this);
                codeErrorDialog.show();
                break;

            case R.id.btn_restart:
                mService.connect();
                if (mService.isConnected()){
                    image_status.setImageResource(R.drawable.green_icon);
                    Toast.makeText(SettingActivity.this, "Successfully connected", Toast.LENGTH_SHORT).show();
                }
                break;

//            case R.id.connection_info1:
//                if (!stringBuilder.toString().equals("")){
//                    CodeErrorDialog codeErrorDialog1 = new CodeErrorDialog(SettingActivity.this, stringBuilder.toString(), null, this);
//                    codeErrorDialog1.show();
//                }
//                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        stringBuilder = new StringBuilder();
        contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ToDoProviderConstants.CONTENT_URI_1, null,null,null,null,null);
        if(cursor!=null){
            while(cursor.moveToNext()){
                String ppid = cursor.getString(0);
                String codehasformat = cursor.getString(1);
                String devicetype = cursor.getString(2);

//                System.out.println("ppid : "+ppid+"\n"+"code : "+codehasformat+"\n"+"devicetype : "+devicetype+"\n\n");
                stringBuilder.append("ppid : "+ppid+"\n"+"code : "+codehasformat+"\n"+"devicetype : "+devicetype+"\n\n");
            }
            cursor.close();
            System.out.println("------------------database complete");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        stringBuilder1 = new StringBuilder();
        String s = getIntent().getStringExtra("log_data");
        stringBuilder1.append(s);

        connection_info = (Button) findViewById(R.id.connection_info);
        connection_info.setOnClickListener(this);
//        connection_info1 = (Button) findViewById(R.id.connection_info1);
//        connection_info1.setOnClickListener(this);
        btn_restart = (Button) findViewById(R.id.btn_restart);
        btn_restart.setOnClickListener(this);
        image_status = findViewById(R.id.image_status);

        manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        timerService = new TimerService();
//        helloIntentService = new HelloIntentService(getApplicationContext());
        myPref = new MyPref(this);
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
//        spinner2 = (Spinner) findViewById(R.id.spinner2);
//        toggleBtn = (ToggleButton)findViewById(R.id.toggle);
//        if (myPref.getPref(Constant.TOGGLE_POSITION,true)){
//            toggleBtn.toggle();
//        }

        toolbar.setTitle("Settings");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


//        else {
//            DialogDisplayMessage codeErrorDialog = new DialogDisplayMessage(SettingActivity.this, "No save codes from roam", null, null);
//            codeErrorDialog.show();
//        }
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(SettingActivity.this, R.layout.spinner_item, paths);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition("4000 ms");
        spinner.setSelection(spinnerPosition);
        if (myPref.getPref(Constant.POSITION,0) == 0) {
            spinner.setSelection(0);
        }else if (myPref.getPref(Constant.POSITION,1) == 1){
            spinner.setSelection(1);
        }else if (myPref.getPref(Constant.POSITION,2) == 2){
            spinner.setSelection(2);
        }
        spinner.setOnItemSelectedListener(this);

        spinner1.setAdapter(adapter);
        int spinnerPosition1 = adapter.getPosition("4000 ms");
        spinner1.setSelection(spinnerPosition1);
        if (myPref.getPref(Constant.POSITION_SP2,0) == 0) {
            spinner1.setSelection(0);
        }else if (myPref.getPref(Constant.POSITION_SP2,1) == 1){
            spinner1.setSelection(1);
        }else if (myPref.getPref(Constant.POSITION_SP2,2) == 2){
            spinner1.setSelection(2);
        }
        spinner1.setOnItemSelectedListener(this);

//        ArrayAdapter<String>adapter1 = new ArrayAdapter<String>(SettingActivity.this, R.layout.spinner_item, paths2);
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner2.setAdapter(adapter1);
//        int spinnerPosition2 = adapter.getPosition("Lumn");
//        spinner2.setSelection(spinnerPosition2);
//
//        spinner2.setOnItemSelectedListener(this);

//        toggleBtn.toggle();
//        toggleBtn.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
//            @Override
//            public void onToggle(boolean on) {
//                System.out.println("---------------"+on);
//                myPref.setPref(Constant.TOGGLE_POSITION,on);
//            }
//        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    mService.connect();
                    if (mService.isConnected()){
                        image_status.setImageResource(R.drawable.green_icon);
                    }else {
                        image_status.setImageResource(R.drawable.gray1);
                    }
                    System.out.println("------connection---------"+mService.isConnected());

                    if (!mService.isConnected()) {
                        if (mBound == false) return;
                        if (mService.connect()) {
                            Toast.makeText(SettingActivity.this, "Successfully connected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){

                }

            }
        }, 500);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if(adapterView.getId() == R.id.spinner)
        {
            switch (position){
                case 0:
                    myPref.setPref(Constant.POSITION,position);
                    myPref.setPref(Constant.SECOND,Constant.FOUR_SECOND);
                    break;
                case 1:
                    myPref.setPref(Constant.SECOND,Constant.THREE_SECOND);
                    myPref.setPref(Constant.POSITION,position);
                    break;
                case 2:
                    myPref.setPref(Constant.SECOND,Constant.TWO_SECOND);
                    myPref.setPref(Constant.POSITION,position);
                    break;
            }
        }
        else if(adapterView.getId() == R.id.spinner1)
        {
            switch (position){
                case 0:
                    myPref.setPref(Constant.POSITION,position);
                    myPref.setPref(Constant.SECOND_SP2,Constant.FOUR_SECOND);
                    break;
                case 1:
                    myPref.setPref(Constant.POSITION_SP2,position);
                    myPref.setPref(Constant.SECOND_SP2,Constant.THREE_SECOND);
                    break;
                case 2:
                    myPref.setPref(Constant.POSITION_SP2,position);
                    myPref.setPref(Constant.SECOND_SP2,Constant.TWO_SECOND);
                    break;
            }
        }
//        else if(adapterView.getId() == R.id.spinner2)
//        {
//            switch (position){
//                case 0:
//                    String string = "\"cmdIntervalms\" : 1000,\n" +
//                            "                \"enterCode\": {\n" +
//                            "                    \"cmdString\": \">WOTP:decimal string<\",\n" +
//                            "                    \"expectReturn\": \"<RE:0000>\"\n" +
//                            "                },\n" +
//                            "                \"newSession\": {\n" +
//                            "                    \"cmdString\": \">HAND<\",\n" +
//                            "                    \"expectReturn\": \"<OK>\"\n" +
//                            "                },\n" +
//                            "                \"getOPID\": {\n" +
//                            "                    \"cmdString\": \">OPID<\",\n" +
//                            "                    \"expectReturn\": \"<OPID:12345678901233>\"                    \n" +
//                            "                },\n" +
//                            "                \"readPPID\": {\n" +
//                            "                    \"cmdString\": \">PPID<\",\n" +
//                            "                    \"expectReturn\": \"<PPID:12345678901233>\" \n" +
//                            "                },\n" +
//                            "                \"getStatus\": {\n" +
//                            "                    \"cmdString\": \">INF<\",\n" +
//                            "                    \"expectReturn\": \"<Long Sting>\"\n" +
//                            "                },\n" +
//                            "                \"getOCS\": {\n" +
//                            "                    \"cmdString\": \">OCS<\",\n" +
//                            "                    \"expectReturn\": \"<OCS:ENABLED> or <OCS:DISABLED>\"\n" +
//                            "                },\n" +
//                            "                \"getPS\": {\n" +
//                            "                    \"cmdString\": \">PS<\",\n" +
//                            "                    \"expectReturn\": \"<PS:FREE> or <PS:PAYF>\"\n" +
//                            "                },\n" +
//                            "                \"getRPD\": {\n" +
//                            "                    \"cmdString\": \">RPD<\",\n" +
//                            "                    \"expectReturn\": \"<RPD:0015>\"\n" +
//                            "                },\n" +
//                            "                \"getTRD\": {\n" +
//                            "                    \"cmdString\": \">TDP<\",\n" +
//                            "                    \"expectReturn\": \"<RPD:0350>\"\n" +
//                            "                },\n" +
//                            "                \"getLVC\": {\n" +
//                            "                    \"cmdString\": \">LVC<\",\n" +
//                            "                    \"expectReturn\": \"<LVC:A1A2A3A4A4A6A7A8>\"\n" +
//                            "                },\n" +
//                            "                \"endSession\": {\n" +
//                            "                    \"cmdString\": \">END<\",\n" +
//                            "                    \"expectReturn\": \"<INT>\"\n" +
//                            "            }";
//                    DialogJSONObject dialogJSONObject1 = new DialogJSONObject(SettingActivity.this,string,null);
//                    dialogJSONObject1.show();
//                    break;
//                case 1:
//                    String string1 = "\"busWake\" : \"00000000000000000000000000000000\",\n" +
//                            "                \"headCode\": \"C56A29\",\n" +
//                            "                \"cmdIntervalms\" : 1000,\n" +
//                            "                \"enterCode\":{\n" +
//                            "                    \"cmdLength\": x00E,\n" +
//                            "                    \"cmdWord\": x0FA,\n" +
//                            "                    \"payloadType\":\"decimalToken\",\n" +
//                            "                    \"cmdString\": \"busWake + headCode + cmdLength + cmdWord + [code payload calculated from decimal string] + [crc8 chechsum]\"\n" +
//                            "                },\n" +
//                            "                \"getOPID\": {\n" +
//                            "                    \"cmdLength\": x006, \n" +
//                            "                    \"cmdWord\": x0AF,\n" +
//                            "                    \"crc8\": x025,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A2906AF25\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getPPID\": {\n" +
//                            "                    \"cmdLength\": x007,\n" +
//                            "                    \"cmdWord\": x008,\n" +
//                            "                    \"cmdReadLength\": x014,\n" +
//                            "                    \"crc8\": CB,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29070814CB\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getHashTop\": {\n" +
//                            "                    \"cmdLength\": x006,\n" +
//                            "                    \"cmdWord\": x011,\n" +
//                            "                    \"crc8\": 08,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29061108\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getRPD\": {\n" +
//                            "                    \"cmdLength\": x006,\n" +
//                            "                    \"cmdWord\": x005,\n" +
//                            "                    \"crc8\": F4,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A290605F4\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getTRD\": {\n" +
//                            "                    \"cmdLength\": x06,\n" +
//                            "                    \"cmdWord\": x06,\n" +
//                            "                    \"crc8\": 16,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060616\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getTPD\": {\n" +
//                            "                    \"cmdLength\": x006,\n" +
//                            "                    \"cmdWord\": x007,\n" +
//                            "                    \"crc8\": 48,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060748\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getPS\": {\n" +
//                            "                    \"cmdLength\": x006,\n" +
//                            "                    \"cmdWord\": x009,\n" +
//                            "                    \"crc8\": 57,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060957\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getOCS\": {\n" +
//                            "                    \"cmdLength\": x006,\n" +
//                            "                    \"cmdWord\": x00A,\n" +
//                            "                    \"crc8\": B5,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060AB5\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getSysCode\": {\n" +
//                            "                    \"cmdLength\": x006,\n" +
//                            "                    \"cmdWord\": x00B,\n" +
//                            "                    \"crc8\": EB,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060BEB\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getRSOC\": {\n" +
//                            "                    \"cmdLength\": x006,\n" +
//                            "                    \"cmdWord\": x00C,\n" +
//                            "                    \"crc8\": 68,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060C68\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getRC\": {\n" +
//                            "                    \"cmdLength\": x006,\n" +
//                            "                    \"cmdWord\": x00D,\n" +
//                            "                    \"crc8\": 36,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060D36\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getFCC\": {\n" +
//                            "                    \"cmdLength\": x006,\n" +
//                            "                    \"cmdWord\": x00E,\n" +
//                            "                    \"crc8\": D4,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060ED4\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getAEO\": {\n" +
//                            "                    \"cmdLength\": x006,\n" +
//                            "                    \"cmdWord\": x00F,\n" +
//                            "                    \"crc8\": 8A,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060F8A\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getADC\": {\n" +
//                            "                    \"cmdLength\": x006,\n" +
//                            "                    \"cmdWord\": x010,\n" +
//                            "                    \"crc8\": 56,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29061056\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getRDBck\": {\n" +
//                            "                    \"cmdLength\": x006,\n" +
//                            "                    \"cmdWord\": x013,\n" +
//                            "                    \"crc8\": B4,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A290613B4\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getPAYG_REV\": {\n" +
//                            "                    \"cmdLength\": x006,\n" +
//                            "                    \"cmdWord\": x014,\n" +
//                            "                    \"crc8\": 37,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29061437\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getCV1\": {\n" +
//                            "                    \"cmdLength\": x008,\n" +
//                            "                    \"cmdWord\": x000,\n" +
//                            "                    \"cmd3060address \": x03F,\n" +
//                            "                    \"cmdReadLength \": x002,\n" +
//                            "                    \"crc8\": 0B,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A2908003F020B\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getCV2\": {\n" +
//                            "                    \"cmdLength\": x008,\n" +
//                            "                    \"cmdWord\": x000,\n" +
//                            "                    \"cmd3060address \": x03E,\n" +
//                            "                    \"cmdReadLength \": x002,\n" +
//                            "                    \"crc8\": CF,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A2908003E02CF\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getCV3\": {\n" +
//                            "                    \"cmdLength\": x008,\n" +
//                            "                    \"cmdWord\": x000,\n" +
//                            "                    \"cmd3060address \": x03D,\n" +
//                            "                    \"cmdReadLength \": x002,\n" +
//                            "                    \"crc8\": 9A,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A2908003D029A\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getCV4\": {\n" +
//                            "                    \"cmdLength\": x008,\n" +
//                            "                    \"cmdWord\": x000,\n" +
//                            "                    \"cmd3060address \": x03C,\n" +
//                            "                    \"cmdReadLength \": x002,\n" +
//                            "                    \"crc8\": 5E,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A2908003C025E\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getPackV\": {\n" +
//                            "                    \"cmdLength\": x008,\n" +
//                            "                    \"cmdWord\": x000,\n" +
//                            "                    \"cmd3060address \": x009,\n" +
//                            "                    \"cmdReadLength \": x002,\n" +
//                            "                    \"crc8\": 8C,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29080009028C\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getPckC\": {\n" +
//                            "                    \"cmdLength\": x008,\n" +
//                            "                    \"cmdWord\": x000,\n" +
//                            "                    \"cmd3060address \": x00A,\n" +
//                            "                    \"cmdReadLength \": x002,\n" +
//                            "                    \"crc8\": D9,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A2908000A02D9\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                },\n" +
//                            "                \"getTmp\": {\n" +
//                            "                    \"cmdLength\": x008,\n" +
//                            "                    \"cmdWord\": x000,\n" +
//                            "                    \"cmd3060address \": x008,\n" +
//                            "                    \"cmdReadLength \": x002,\n" +
//                            "                    \"crc8\": 48,\n" +
//                            "                    \"cmdString\": \"00000000000000000000000000000000C56A290800080248\",\n" +
//                            "                    \"expectReturn\": true\n" +
//                            "                }";
//                    DialogJSONObject dialogJSONObject = new DialogJSONObject(SettingActivity.this,string1,null);
//                    dialogJSONObject.show();
//                    break;
//            }
//        }

    }

    public void toast(String s){
        Toast.makeText(this, "Scanning Every "+s+" Minute.", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        super.onStart();
        Intent intent2 = new Intent(this, MqttService.class);
        bindService(intent2, mConnection, Context.BIND_AUTO_CREATE);
//        Intent intent = new Intent(this, TimerService.class);
//        Intent intent1 = new Intent(this, WifiService.class);
//        Intent intent2 = new Intent(this, BluetoothService.class);
//        bindService(intent, timerConnection, Context.BIND_AUTO_CREATE);
//        bindService(intent1, wifiConection, Context.BIND_AUTO_CREATE);
//        bindService(intent1, blutoothConection, Context.BIND_AUTO_CREATE);
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
//        if (timerBound) {
//            unbindService(timerConnection);
//            timerBound = false;
//        }
//        if (wifiBound){
//            unbindService(wifiConection);
//            wifiBound = false;
//        }
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
//        if (bluetoothBound){
//            unbindService(blutoothConection);
//            bluetoothBound = false;
//        }
//        if (timerService != null){
//            unregisterReceiver(timerService);
//        }
        EventBus.getDefault().unregister(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setAlarm(Context context, long delay) {

//        Intent startIntent = new Intent(context, WifiBroadCastReceiver.class);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//            alarmManager.set(alarmManager.RTC_WAKEUP, delay, pendingIntent);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
//            alarmManager.setExact(alarmManager.RTC_WAKEUP, delay, pendingIntent);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.setExactAndAllowWhileIdle(alarmManager.RTC_WAKEUP, delay, pendingIntent);
//        }
    }

    @Override
    public void OnButtonclick() {
//        stringBuilder1 = new StringBuilder();
    }
}
