package com.sparkle.devicescanner.Activity;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sparkle.devicescanner.Adapter.ActiveDeviceAdapter;
import com.sparkle.devicescanner.Adapter.PayloadAdapter;
import com.sparkle.devicescanner.Base.BaseActivity;
import com.sparkle.devicescanner.LocalDatabase.DatabaseHelper;
import com.sparkle.devicescanner.Model.PPID_TYPE.DeviceType;
import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.Constant;
import com.sparkle.devicescanner.Utils.ConstantMethod;
import com.sparkle.devicescanner.Utils.Crc8;
import com.sparkle.devicescanner.Utils.MyPref;
import com.sparkle.devicescanner.Views.DialogDisplayMessage;
import com.sparkle.devicescanner.events.CodeStringEvent;
import com.sparkle.devicescanner.services.MqttService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ActiveDeviceActivity extends BaseActivity implements View.OnClickListener, ActiveDeviceAdapter.OnButtonclick, DialogDisplayMessage.OnBack, PayloadAdapter.Onclick {

    private RecyclerView listView;
    private RecyclerView listView1;

    private List<DeviceType> deviceTypes;
    private Toolbar toolbar;
    private MyPref myPref;
    private String str;
    private Gson gson;
    String codehasformate,deviceType,PPID;
    private int fPosition;
    private DatabaseHelper mdbhelper;
    private TextView tv_deviceType,  tv_PPID;
    private List<String> commandList;
    private List<String> newcommandList;
    private List<String> dataList;
    private List<String> newdataList;
    private ActiveDeviceAdapter activeDeviceAdapter;
    private PayloadAdapter payloadAdapter;
    private StringBuilder stringBuilder;
    private List<String> protocolName_list;
    private StringBuilder builder;
    private Timer timer;
    MqttService mService;
    boolean mBound = false;
    private Button btn_read_code,btn_all_Status,btn_write_code,btn_capture;
    private TextView tv_read_code;
    private TextView tv_write_code;
//    private boolean updatelumnlist = true;
//    private boolean updateovcamplist = true;
    private List<String> stringList;
    private LinearLayout ll_write;
    private int i2 = 0;
    public static long SECOND = 3000;
    private int i1 = 0;
    private boolean continuebool = true;
    JSONObject jo = new JSONObject();
//    List<String> data = new ArrayList<>();

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MqttService.LocalBinder binder = (MqttService.LocalBinder) service;
            ActiveDeviceActivity.this.mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("Device Details");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        stringBuilder = new StringBuilder();
        builder = new StringBuilder();
        stringList = new ArrayList<>();

        myPref = new MyPref(this);
        SECOND = myPref.getPref(Constant.SECOND_SP2,Constant.THREE_SECOND);
        gson = new Gson();

        commandList = new ArrayList<>();
        dataList = new ArrayList<>();
        newcommandList = new ArrayList<>();
        newdataList = new ArrayList<>();

        tv_deviceType = findViewById(R.id.tv_deviceType);
        tv_read_code = findViewById(R.id.tv_read_code);
        tv_write_code = findViewById(R.id.tv_write_code);

        btn_read_code = findViewById(R.id.btn_read_code);
        btn_read_code.setOnClickListener(this);

        btn_write_code = findViewById(R.id.btn_write_code);
        btn_write_code.setOnClickListener(this);
        btn_capture = findViewById(R.id.btn_capture);
        btn_capture.setOnClickListener(this);

        btn_all_Status = findViewById(R.id.btn_all_Status);
        btn_all_Status.setOnClickListener(this);

        ll_write = findViewById(R.id.ll_write);
        ll_write.setVisibility(View.GONE);

        tv_PPID = findViewById(R.id.tv_PPID);

        deviceTypes = new ArrayList<>();
        activeDeviceAdapter = new ActiveDeviceAdapter(getApplicationContext(),commandList,dataList,ActiveDeviceActivity.this);
        payloadAdapter = new PayloadAdapter(getApplicationContext(),newcommandList,newdataList,ActiveDeviceActivity.this);
        mdbhelper = new DatabaseHelper(this);

        protocolName_list = new ArrayList<>();
        Cursor res1 = mdbhelper.getAllJsonData();
        while (res1.moveToNext()){
            protocolName_list.add(res1.getString(res1.getColumnIndex("protocolType")));
        }

        if (protocolName_list.size() == 0){
            DialogDisplayMessage dialogDisplayMessage = new DialogDisplayMessage(ActiveDeviceActivity.this,"Please First Add Protocol",this);
            dialogDisplayMessage.show();
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            deviceType = extras.getString(Constant.I_DEVICE_TYPE);
            PPID = extras.getString(Constant.I_PPID);
            tv_PPID.setText(PPID);
            tv_deviceType.setText(deviceType);
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (deviceType.equalsIgnoreCase("ovcamp")){
                    deviceType = "ovCamp";
                }else if (deviceType.equalsIgnoreCase("lumn")){
                    deviceType = "Lumn";
                }
                try {
                    if (mService != null){
                        if (!mService.isConnected()) {
                            if (!mBound) return;
                            mService.subscribe("_OV1/"+deviceType+"/" + PPID , 2);
                        } else {
                            mService.subscribe("_OV1/"+deviceType+"/" + PPID , 2);
                        }
                    }
                }catch (Exception e){
//                    Toast.makeText(ActiveDeviceActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }

            }
        },2000);

        listView = (RecyclerView) findViewById(R.id.listView);
        listView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        listView.setLayoutManager(linearLayoutManager);
        listView.setAdapter(activeDeviceAdapter);

        listView1 = (RecyclerView) findViewById(R.id.listView1);
        listView1.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        listView1.setLayoutManager(linearLayoutManager1);
        listView1.setAdapter(payloadAdapter);


//        data.add("C56A290AAF0001000000");
//        data.add("C56A291B081430334148323030303030303030303C3C3C3C3C3C");
//        data.add("C56A290E11D11B17ACAE3BDFCA16");
//        data.add("C56A290805290000");
//        data.add("C56A290806030000");
//        data.add("C56A2908072C0000");
//        data.add("C56A290809000000");
//        data.add("C56A29080A010000");
//        data.add("C56A29080B000000");
//        data.add("C56A29080C2C0000");
//        data.add("C56A29080D870A00");
//        data.add("C56A29080E701700");
//        data.add("C56A29080FD060E0");
//        data.add("C56A2908102D0100");
//        data.add("C56A2908131F0000");
//        data.add("C56A290814410000");
//        data.add("C56A290A003F02CB0C79");
//        data.add("C56A290A003E02C80CA3");
//        data.add("C56A290A003D02B40CD0");
//        data.add("C56A290A003C02E00C13");
//        data.add("C56A290A0009022833E7");
//        data.add("C56A290A000A02000084");
//        data.add("C56A290A000802A40B00");
            for (int i = 0; i < protocolName_list.size(); i++){
                if (deviceType.equalsIgnoreCase(protocolName_list.get(i))){
                    deviceType = protocolName_list.get(i);
                    System.out.println("---------deviceType------------"+deviceType);
                }
            }

            if (deviceType != null){
                if (deviceType.equalsIgnoreCase("ovcamp")){
                    String string = " Device Type :" + Constant.OVCAMP_DEVICE;
                    EventBus.getDefault().post(string);
                    myPref.setPref(Constant.DEVICE_TYPE,Constant.OVCAMP_DEVICE);
//                    String code = ConstantMethod.code_string_for_card(codehasformate,PPID);

                    myPref.setPref(Constant.LUMN_USB,true);

                    Cursor res = mdbhelper.getJsonData(deviceType);
                    while (res.moveToNext()){
                        String json = res.getString(res.getColumnIndex("protocolJson"));
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONObject object= new JSONObject(jsonObject.toString());
                                try {
                                    SECOND = object.getLong("cmdIntervalms");
                                    System.out.println("-------------------"+SECOND);
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            Iterator x = object.keys();
                            JSONArray jsonArray = new JSONArray();
                            JSONArray jsonArray1 = new JSONArray();
                            while (x.hasNext()){
                                String key = (String) x.next();
                                if (String.valueOf(object.get(key)).startsWith("{")){
                                    if (object.get(key).toString().contains("payload")){
                                        newdataList.add(key);
                                        jsonArray1.put(object.get(key));
                                    }else {
                                        dataList.add(key);
                                        jsonArray.put(object.get(key));
                                    }
                                }
                            }
//                            codehasformate = "*123 456 789 123 456 159 753#";
                            for (int i = 0; i < jsonArray1.length(); i++){
                                String cmdLength = String.valueOf(jsonArray1.getJSONObject(i).get("cmdLength"));
                                cmdLength = cmdLength.substring(2);
                                String cmdWord = String.valueOf(jsonArray1.getJSONObject(i).get("cmdWord"));
                                cmdWord = cmdWord.substring(2);
                                    String str = cmdLength + cmdWord;
                                    newcommandList.add(str);
                            }
                                payloadAdapter.notifyList(newcommandList,newdataList);
                            for (int i = 0; i < jsonArray.length(); i++){
                                String str = String.valueOf(jsonArray.getJSONObject(i).get("cmdString"));
                                String CRC = ConstantMethod.Cal_CRC_8(Crc8.Params, str);
                                if (CRC.equals("0")){
                                    commandList.add(str);
                                }else { if (CRC.length() == 1){ CRC = "0"+CRC; }
                                    str = str+CRC;
                                    commandList.add(str);
                                }
                            }
                            activeDeviceAdapter.notifyList(commandList,dataList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }else if (deviceType.equalsIgnoreCase("lumn") || deviceType.equalsIgnoreCase("catch")){
                    String string = " Device Type :" + Constant.LUMN_DEVICE;
                    EventBus.getDefault().post(string);
                    myPref.setPref(Constant.DEVICE_TYPE,Constant.LUMN_DEVICE);
//                    String code = ConstantMethod.code_has_for_lumn(codehasformate);

                    myPref.setPref(Constant.LUMN_USB,false);
                    Cursor res = mdbhelper.getJsonData(deviceType);
                    while (res.moveToNext()){
                        String json = res.getString(res.getColumnIndex("protocolJson"));
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONObject object= new JSONObject(jsonObject.toString());
                            try {
                                SECOND = object.getLong("cmdIntervalms");
                                System.out.println("-------------------"+SECOND);
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Iterator x = object.keys();
                            JSONArray jsonArray = new JSONArray();
                            JSONArray jsonArray1 = new JSONArray();

                            while (x.hasNext()){
                                String key = (String) x.next();
                                if (String.valueOf(object.get(key)).startsWith("{")){
                                    if (object.get(key).toString().contains("WOTP")){
                                        newdataList.add(key);
                                        jsonArray1.put(object.get(key));
                                    }else {
                                        jsonArray.put(object.get(key));
                                        dataList.add(key);
                                    }
                                }
                            }
                            for (int i = 0; i < jsonArray1.length(); i++){
                                String str = String.valueOf(jsonArray.getJSONObject(i).get("cmdString"));
                                newcommandList.add(str);
                            }
                            payloadAdapter.notifyList(newcommandList,newdataList);
                            for (int i = 0; i < jsonArray.length(); i++){
                                String str = String.valueOf(jsonArray.getJSONObject(i).get("cmdString"));
                                commandList.add(str);
                            }
                            System.out.println("----------lumn-----------"+jsonArray);
                            activeDeviceAdapter.notifyList(commandList,dataList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
//        }
    }

    public void clickonReadAll(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                OnButtonclick(i2);
//                onUsbSerialMessage(data.get(i2));
                i2 = i2 + 1;
                if (commandList.size() != i2){
                    if (continuebool){
                        btn_all_Status.setEnabled(false);
                        clickonReadAll();
                    }
                }else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            i2 = 0;
                            btn_all_Status.setEnabled(true);
                            btn_capture.setEnabled(true);
                        }
                    });

                    System.out.println("------------------it's finish -----");
//                    lastfunction();
                }
            }
        },SECOND);
    }

//    public void lastfunction(){
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Onclick(i1,"");
//                i1 = i1 + 1;
//                if (newcommandList.size() != i1){
//                    lastfunction();
//                }else {
//                    System.out.println("------------------it's finish all-----");
//                }
//            }
//        },3000);
//    }


    @Override
    public void onPause() {
        super.onPause();
        continuebool = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_read_code:
                if (codehasformate != null){
                    tv_read_code.setText(codehasformate);
//                    ll_write.setVisibility(View.VISIBLE);
                }else {
                    DialogDisplayMessage dialogDisplayMessage = new DialogDisplayMessage(ActiveDeviceActivity.this,"payload Code cannot be empty",null);
                    dialogDisplayMessage.show();
                }
                break;

            case R.id.btn_all_Status:
                if (commandList.size() != 0){
                    btn_all_Status.setEnabled(false);
                    clickonReadAll();
                }else {
                    DialogDisplayMessage dialogDisplayMessage = new DialogDisplayMessage(ActiveDeviceActivity.this,"Please add proper JSONObject",null);
                    dialogDisplayMessage.show();
                }

                break;

            case R.id.btn_capture:
                if (commandList.size() != 0){
//                    Toast.makeText(ActiveDeviceActivity.this, "Coming Soon!!", Toast.LENGTH_SHORT).show();
                    System.out.println("-----------------------"+jo.toString());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                    String format = simpleDateFormat.format(new Date());
                    DialogDisplayMessage dialogDisplayMessage = new DialogDisplayMessage(ActiveDeviceActivity.this,format+"\n"+formatString(jo.toString()),null);
                    dialogDisplayMessage.show();
                }else {
                    DialogDisplayMessage dialogDisplayMessage = new DialogDisplayMessage(ActiveDeviceActivity.this,"Please add proper JSONObject",null);
                    dialogDisplayMessage.show();
                }

                break;

            case R.id.btn_write_code:
                if (deviceType.equalsIgnoreCase("ovcamp")){
                    myPref.setPref(Constant.LUMN_USB,true);
                    byte[] datatmp1 = ConstantMethod.hexStr2Bytes(codehasformate);
                    EventBus.getDefault().post(datatmp1);
                    System.out.println("-------------------write ovCamp");
                }else {
                    myPref.setPref(Constant.LUMN_USB,false);
                    byte[] datatmp1 = ConstantMethod.stringToByteArray(codehasformate);
                    EventBus.getDefault().post(datatmp1);
                    System.out.println("-------------------write Lumn");
                }
                break;
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onCodeStringEvent(CodeStringEvent event) {
        codehasformate = event.code;
        EventBus.getDefault().removeAllStickyEvents();
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
    public void Onclick(int position,String payload) {
        if (codehasformate != null){
            fPosition = position;
            if (deviceType.equalsIgnoreCase("OVCAMP")){
//                updateovcamplist = true;
                String string = ConstantMethod.write_code_for_ovCamp(codehasformate,PPID,newcommandList.get(position));
                String command = string;
                System.out.println("--------------"+string);
                myPref.setPref(Constant.LUMN_USB,true);
                command = command.replaceAll("..", "$0 ").trim();
                byte[] readtmp = ConstantMethod.hexStr2Bytes(command);
                sendJsonMessage(readtmp);
                stringBuilder = null;
            }else {
//                updatelumnlist = true;
                myPref.setPref(Constant.LUMN_USB,false);
                String string = ConstantMethod.code_has_for_lumn(codehasformate);
                System.out.println("--------------"+string);
                byte[] datatmp1 = ConstantMethod.stringToByteArray(string);
                sendJsonMessage(datatmp1);
                stringBuilder = null;
            }
        }else {
            DialogDisplayMessage dialogDisplayMessage = new DialogDisplayMessage(ActiveDeviceActivity.this,"payload Code cannot be empty",null);
            dialogDisplayMessage.show();
        }

    }

    @Override
    public void OnButtonclick(int position) {
        fPosition = position;
        String command = commandList.get(position);
        if (deviceType.equalsIgnoreCase("OVCAMP")){
//            updateovcamplist = true;
            myPref.setPref(Constant.LUMN_USB,true);
            command = command.replaceAll("..", "$0 ").trim();
            byte[] readtmp = ConstantMethod.hexStr2Bytes(command);
            sendJsonMessage(readtmp);
            stringBuilder = null;
            builder.append("_____OVCAMP______");
            System.out.println("-------"+position+"-------"+command);
        }else if (deviceType.equalsIgnoreCase("lumn") || deviceType.equalsIgnoreCase("catch")){
//            updatelumnlist = true;
            myPref.setPref(Constant.LUMN_USB,false);
            byte[] datatmp1 = ConstantMethod.stringToByteArray(command);
            sendJsonMessage(datatmp1);
            stringBuilder = null;
            builder.append("_____LUMN______");
            System.out.println("--------"+position+"-------"+command);

        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUsbSerialMessage(String message) {
        if (stringBuilder == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(message);
        } else {
            stringBuilder.append(message);
        }
        try {
            if (tv_deviceType.getText().toString().equalsIgnoreCase("LUMN") || tv_deviceType.getText().toString().equalsIgnoreCase("catch")){
                if (stringBuilder.toString().contains("OK")){//>HAND<
                    activeDeviceAdapter.changeValue(fPosition,"<OK>");
                    try {
                        jo.put(dataList.get(fPosition),"<OK>");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                }
                    stringBuilder = null;
                }

                assert stringBuilder != null;
                if (stringBuilder.toString().length() >= 27 && stringBuilder.toString().length() <= 33) {//>PPID<
                    if (stringBuilder.toString().contains("<PPID:")) {
                        String ppidFromLUMN = stringBuilder.toString();
                        ppidFromLUMN = ppidFromLUMN.replace(">PPID<", "");
                        ppidFromLUMN = ppidFromLUMN.replace("<PPID:", "");
                        ppidFromLUMN = ppidFromLUMN.replace(">", "");
                        ppidFromLUMN = ppidFromLUMN.replace(" ", "");
                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,ppidFromLUMN);
                        try {
                            jo.put(dataList.get(fPosition),ppidFromLUMN);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                assert stringBuilder != null;
                if (stringBuilder.toString().length() >= 124 && stringBuilder.toString().length() <= 143){//>INF< 138
                    //<INF:OPID:91051510000001,PPID:91051510000001 ,OCS: ENABLED,PS:FREE,RPD: 0000,TDP:0000,LVC:4F80DE119510D208,RT:0000,LCS:0000>
                    //>INF<<INF:OPID:M400180399012 ,PPID:M400180399012       ,OCS: ENABLED,PS:FREE,RPD: 1125D22H48M,TDP:9999,LVC:FB81B8B9C3725DBE,RT:00106,LCS:00004>
                    //10 14 6 20 6 7 4 4 6 11 5 4 5 16 4 5 5 5 1=138
                    if (stringBuilder.toString().contains("<INF:")) {
                        String inf = stringBuilder.toString();
                        inf = inf.replace(">INF<", "");
                        inf = inf.replace("<INF:", "");
                        inf = inf.replace(">", "");
                        inf = inf.replace(" ", "");

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,inf);
                        try {
                            jo.put(dataList.get(fPosition),inf);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                assert stringBuilder != null;
                if (stringBuilder.toString().length() >= 21 && stringBuilder.toString().length() <= 27){//>OPID<<OPID 12345678901233>
                    if (stringBuilder.toString().contains("<OPID:")){
                        String OPID = stringBuilder.toString();
                        OPID = OPID.replace(">OPID<", "");
                        OPID = OPID.replace("<OPID:", "");
                        OPID = OPID.replace(">", "");
                        OPID = OPID.replace(" ", "");

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,OPID);
                        try {
                            jo.put(dataList.get(fPosition),OPID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                assert stringBuilder != null;
                if (stringBuilder.toString().length() >= 14 && stringBuilder.toString().length() <= 19){//>OCS<<OCS: ENABLED>
                    if (stringBuilder.toString().contains("<OCS:")){
                        String OCS = stringBuilder.toString();
                        OCS = OCS.replace(">OCS<", "");
                        OCS = OCS.replace("<OCS:", "");
                        OCS = OCS.replace(">", "");
                        OCS = OCS.replace(" ", "");

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,OCS);
                        try {
                            jo.put(dataList.get(fPosition),OCS);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                assert stringBuilder != null;
                if (stringBuilder.toString().length() >= 10 && stringBuilder.toString().length() <= 14){//>PS<<PS: FREE>
                    if (stringBuilder.toString().contains("<PS:")){
                        String PS = stringBuilder.toString();
                        PS = PS.replace(">PS<", "");
                        PS = PS.replace("<PS:", "");
                        PS = PS.replace(">", "");
                        PS = PS.replace(" ", "");

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,PS);
                        try {
                            jo.put(dataList.get(fPosition),PS);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                assert stringBuilder != null;
                if (stringBuilder.toString().length() >= 11 && stringBuilder.toString().length() <= 16){//>RPD<<RPD: 0000>
                    if (stringBuilder.toString().contains("<RPD:")){
                        String RPD = stringBuilder.toString();
                        RPD = RPD.replace(">RPD<", "");
                        RPD = RPD.replace("<RPD:", "");
                        RPD = RPD.replace(">", "");
                        RPD = RPD.replace(" ", "");

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,RPD);
                        try {
                            jo.put(dataList.get(fPosition),RPD);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                assert stringBuilder != null;
                if (stringBuilder.toString().length() >= 11 && stringBuilder.toString().length() <= 16){//>TDP<<TDP: 0000>
                    if (stringBuilder.toString().contains("<TDP:")){
                        String TDP = stringBuilder.toString();
                        TDP = TDP.replace(">TDP<", "");
                        TDP = TDP.replace("<TDP:", "");
                        TDP = TDP.replace(">", "");
                        TDP = TDP.replace(" ", "");

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,TDP);
                        try {
                            jo.put(dataList.get(fPosition),TDP);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                assert stringBuilder != null;
                if (stringBuilder.toString().length() >= 22 && stringBuilder.toString().length() <= 27){//>LVC<<LVC:A1A2A3A4A4A6A7A8>
                    if (stringBuilder.toString().contains("<LVC:")){
                        String LVC = stringBuilder.toString();
                        LVC = LVC.replace(">LVC<", "");
                        LVC = LVC.replace("<LVC:", "");
                        LVC = LVC.replace(">", "");
                        LVC = LVC.replace(" ", "");

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,LVC);
                        try {
                            jo.put(dataList.get(fPosition),LVC);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (stringBuilder.toString().length() >= 5 && stringBuilder.toString().length() <= 10){
                    if (stringBuilder.toString().contains("<INT")){
                        String INT = stringBuilder.toString();
                        INT = INT.replace(">INT<", "");

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,INT);
                        try {
                            jo.put(dataList.get(fPosition),INT);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (stringBuilder.toString().length() == 9){
                    if (stringBuilder.toString().contains("<RE:")){

                        String[] separated1 = stringBuilder.toString().split("<RE:",9);
                        String onlyforlumn = "<RE:"+ separated1[1];
                        if (onlyforlumn.length() == 9){
                            String lumnreply = onlyforlumn;
                            lumnreply = lumnreply.replace("<RE:","");
                            lumnreply = lumnreply.replace(">","");
                            if (lumnreply.toLowerCase().equalsIgnoreCase("FAIL")){
                                stringBuilder = null;
                                payloadAdapter.changeValue(fPosition,"Invalid keycode");
                            }else if (lumnreply.toLowerCase().equalsIgnoreCase("WAIT")){
                                stringBuilder = null;
                                payloadAdapter.changeValue(fPosition,"Top up in progress");
                            }else {
                                payloadAdapter.changeValue(fPosition,"Top up Succesfully for "+lumnreply+" Days");
                                stringBuilder = null;
                            }
                        }
                    }
                }
//            if (!updatelumnlist){
//                if (stringList.size() == commandList.size()){
//                    activeDeviceAdapter.notifyList1(stringList);
//                }
//            }
            }else {

                if (stringBuilder.toString().length() >= 52 && stringBuilder.toString().length() <= 54) {//C56A291B081430364148323030333030303030333C3C3C3C3C3C45
                    if (stringBuilder.toString().startsWith("C56A291B0814")) {//PPID
                        String msg = stringBuilder.toString();
                        String data = msg.substring(12, 52);//30364148323030333030303030333C3C3C3C3C3C//06AH2003000003
                        String ppidFromovCAMP = ConstantMethod.convertHexToString(data);//03AH2000000000//C56A291B081430334148323030303030303030303C3C3C3C3C3C
                        ppidFromovCAMP = ppidFromovCAMP.replace("<", "");

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,ppidFromovCAMP);
                        try {
                            jo.put(dataList.get(fPosition),ppidFromovCAMP);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                assert stringBuilder != null;
                if (stringBuilder.toString().length() == 28){
                    if (stringBuilder.toString().startsWith("C56A290E11")){//C56A290E11XXXXXXXXXXXXXXXXXX  hastop  complete
                        String msg = stringBuilder.toString();//C56A290E11D11B17ACAE3BDFCA16
                        String data = msg.substring(10, 26);
                        String hashtop = ConstantMethod.makeHastop(data);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,hashtop);
                        try {
                            jo.put(dataList.get(fPosition),hashtop);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                assert stringBuilder != null;
                if (stringBuilder.toString().length() == 16){
                    if (stringBuilder.toString().startsWith("C56A290805")){//C56A290805XXXXXX    Remaining_PAYG_Days  complete
                        String msg = stringBuilder.toString();//C56A290805290000 // 410
                        String data = msg.substring(10, 14);

                        String remainingDay = ConstantMethod.makeHastop(data);
                        remainingDay = ConstantMethod.convertHextoDecimal(remainingDay);
                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,remainingDay+" Days");
                        try {
                            jo.put(dataList.get(fPosition),remainingDay+" Days");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else if (stringBuilder.toString().startsWith("C56A290806")){//C56A290806XXXXXX   Days have been running  complete
                        String msg = stringBuilder.toString();//C56A290806030000 // 30
                        String data = msg.substring(10, 14);

                        String runningDay = ConstantMethod.makeHastop(data);
                        runningDay = ConstantMethod.convertHextoDecimal(runningDay);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,runningDay + " Days");
                        try {
                            jo.put(dataList.get(fPosition),runningDay + " Days");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A290807")){//C56A290807XXXXXX    PAYG_Days  complete
                        String msg = stringBuilder.toString();//C56A2908072C0000 //440
                        String data = msg.substring(10, 14);

                        String PAYG_Days = ConstantMethod.makeHastop(data);
                        PAYG_Days = ConstantMethod.convertHextoDecimal(PAYG_Days);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,PAYG_Days+" Days");
                        try {
                            jo.put(dataList.get(fPosition),PAYG_Days+" Days");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A290809")){//C56A290809XX00XX     PAYG_State  complete
                        String msg = stringBuilder.toString();//C56A290809000000 // 00
                        String data = msg.substring(10, 12);

                        String PAYG_State = ConstantMethod.convertHextoDecimal(data);
                        if (PAYG_State.contains("1")){
                            PAYG_State = "FREE";
                        }else {
                            PAYG_State = "PAYG";
                        }

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,PAYG_State);
                        try {
                            jo.put(dataList.get(fPosition),PAYG_State);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A29080A")){//C56A29080AXX00XX       Output_Control_State  complete
                        String msg = stringBuilder.toString();//C56A29080A010000  //10
                        String data = msg.substring(10, 12);
                        String Output_Control_State = ConstantMethod.convertHextoDecimal(data);
                        if (Output_Control_State.contains("1")){
                            Output_Control_State = "ENABLED";
                        }else {
                            Output_Control_State = "DISABLED";
                        }
                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,Output_Control_State);
                        try {
                            jo.put(dataList.get(fPosition),Output_Control_State);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A29080B")){//C56A29080BXX00XX      System_Status_Code  complete
                        String msg = stringBuilder.toString();//C56A29080B000000 // 00
                        String data = msg.substring(10, 12);
                        String System_Status_Code = ConstantMethod.convertHextoDecimal(data);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,System_Status_Code);
                        try {
                            jo.put(dataList.get(fPosition),System_Status_Code);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A29080C")){//C56A29080CXX00XX     Relative_SOC  complete
                        String msg = stringBuilder.toString();//C56A29080C2C0000 //440
                        String data = msg.substring(10, 12);
                        String Relative_SOC = ConstantMethod.convertHextoDecimal(data);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,Relative_SOC+" %");
                        try {
                            jo.put(dataList.get(fPosition),Relative_SOC+" %");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A29080D")){//C56A29080DXXXXXX     Remaining_Capacity  complete
                        String msg = stringBuilder.toString();//C56A29080D870A00 // 13510
                        String data = msg.substring(10, 14);
                        String Remaining_Capacity = ConstantMethod.makeHastop(data);
                        Remaining_Capacity = ConstantMethod.convertHextoDecimal(Remaining_Capacity);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,Remaining_Capacity+ " mAH");
                        try {
                            jo.put(dataList.get(fPosition),Remaining_Capacity+ " mAH");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A29080E")){//C56A29080EXXXXXX    Full_Charge_Capacity   complete
                        String msg = stringBuilder.toString();//C56A29080E701700 // 11223//6000
                        String data = msg.substring(10, 14);
                        String Full_Charge_Capacity = ConstantMethod.makeHastop(data);
                        Full_Charge_Capacity = ConstantMethod.convertHextoDecimal(Full_Charge_Capacity);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,Full_Charge_Capacity+" mAH");
                        try {
                            jo.put(dataList.get(fPosition),Full_Charge_Capacity+" mAH");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A29080F")){//C56A29080FXXXXXX     Accu_Energy_Output   complete
                        String msg = stringBuilder.toString();//C56A29080FD060E0//20896
                        String data = msg.substring(10, 14);
                        String Accu_Energy_Output = ConstantMethod.makeHastop(data);
                        Accu_Energy_Output = ConstantMethod.convertHextoDecimal(Accu_Energy_Output);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,Accu_Energy_Output+ " Wh");
                        try {
                            jo.put(dataList.get(fPosition),Accu_Energy_Output+ " Wh");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A290810")){//C56A290810XXXXXX       Accu_Cycles   complete
                        String msg = stringBuilder.toString();//C56A2908102D0100  //301
                        String data = msg.substring(10, 14);
                        String Accu_Cycles = ConstantMethod.makeHastop(data);
                        Accu_Cycles = ConstantMethod.convertHextoDecimal(Accu_Cycles);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,Accu_Cycles);
                        try {
                            jo.put(dataList.get(fPosition),Accu_Cycles);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A290813")){//C56A290813XXXXXX      Run_Days_Backup   complete
                        String msg = stringBuilder.toString();//C56A2908131F0000  //310
                        String data = msg.substring(10, 14);
                        String Run_Days_Backup = ConstantMethod.makeHastop(data);
                        Run_Days_Backup = ConstantMethod.convertHextoDecimal(Run_Days_Backup);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,Run_Days_Backup+" Day");
                        try {
                            jo.put(dataList.get(fPosition),Run_Days_Backup+" Day");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A290814")){//C56A290814XXXXXX     PAYG_REV   complete
                        String msg = stringBuilder.toString();//C56A290814410000  //650
                        String data = msg.substring(10, 14);
                        String PAYG_REV = ConstantMethod.makeHastop(data);
                        PAYG_REV = ConstantMethod.convertHextoDecimal(PAYG_REV);
                        int rev = Integer.parseInt(PAYG_REV) / 10;
                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition, String.valueOf(rev));
                        try {
                            jo.put(dataList.get(fPosition),String.valueOf(rev));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }


                assert stringBuilder != null;
                if (stringBuilder.toString().length() == 20){
                    if (stringBuilder.toString().startsWith("C56A290A003F02")){//C56A290A003F02XXXXXX     CellVoltage1   complete
                        String msg = stringBuilder.toString();//C56A290A003F02CB0C79 //20312
                        String data = msg.substring(14, 18);
                        String CellVoltage1 = ConstantMethod.makeHastop(data);
                        CellVoltage1 = ConstantMethod.convertHextoDecimal(CellVoltage1);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,CellVoltage1+" mV");
                        try {
                            jo.put(dataList.get(fPosition),CellVoltage1+" mV");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A290A003E02")){//C56A290A003E02XXXXXX    CellVoltage2   complete
                        String msg = stringBuilder.toString();//C56A290A003E02C80CA3
                        String data = msg.substring(14,18);
                        String CellVoltage2 = ConstantMethod.makeHastop(data);
                        CellVoltage2 = ConstantMethod.convertHextoDecimal(CellVoltage2);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,CellVoltage2+" mV");
                        try {
                            jo.put(dataList.get(fPosition),CellVoltage2+" mV");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A290A003D02")){//C56A290A003D02XXXXXX     CellVoltage3   complete
                        String msg = stringBuilder.toString();//C56A290A003D02B40CD0
                        String data = msg.substring(14,18);
                        String CellVoltage3 = ConstantMethod.makeHastop(data);
                        CellVoltage3 = ConstantMethod.convertHextoDecimal(CellVoltage3);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,CellVoltage3+" mV");
                        try {
                            jo.put(dataList.get(fPosition),CellVoltage3+" mV");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A290A003C02")){//C56A290A003C02XXXXXX     CellVoltage4   complete
                        String msg = stringBuilder.toString();//C56A290A003C02E00C13//22612
                        String data = msg.substring(14,18);
                        String CellVoltage4 = ConstantMethod.makeHastop(data);
                        CellVoltage4 = ConstantMethod.convertHextoDecimal(CellVoltage4);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,CellVoltage4+" mV");
                        try {
                            jo.put(dataList.get(fPosition),CellVoltage4+" mV");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A290A000902")){//C56A290A000902XXXXXX    BatteryVoltage
                        String msg = stringBuilder.toString();//C56A290A0009022833E7// 3951
                        String data = msg.substring(14,18);
                        String BatteryVoltage = ConstantMethod.makeHastop(data);
                        BatteryVoltage = ConstantMethod.convertHextoDecimal(BatteryVoltage);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,BatteryVoltage+" mV");
                        try {
                            jo.put(dataList.get(fPosition),BatteryVoltage+" mV");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A290A000A02")){//C56A290A000A02XXXXXX     BatteryCurrent
                        String msg = stringBuilder.toString();//C56A290A000A02000084 //00
                        String data = msg.substring(14,18);
                        String BatteryCurrent = ConstantMethod.makeHastop(data);
                        BatteryCurrent = ConstantMethod.convertHextoDecimal(BatteryCurrent);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,BatteryCurrent+" mA");
                        try {
                            jo.put(dataList.get(fPosition),BatteryCurrent+" mA");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A290A000802")){//C56A290A000802XXXXXX      BatteryTemperature
                        String msg = stringBuilder.toString();//C56A290A000802A40B00 //26
                        String data = msg.substring(14,18);
                        String BatteryTemperature = ConstantMethod.makeHastop(data);
                        BatteryTemperature = ConstantMethod.convertHextoDecimal(BatteryTemperature);//2980-2731 (degree Kelvin)÷10 ≈26℃
                        int temp = Integer.parseInt(BatteryTemperature);
                        temp = temp - 2731;
                        temp = temp / 10;
                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,temp+" ℃");
                        try {
                            jo.put(dataList.get(fPosition),temp+" ℃");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (stringBuilder.toString().startsWith("C56A290AAF")){//C56A290AAFXXXXXXXXXX    DevID
                        String msg = stringBuilder.toString();//C56A290AAF0001000000 // 0100
                        String data = msg.substring(10,18);
                        String DevID = ConstantMethod.makeHastop(data);
                        DevID = ConstantMethod.convertHextoDecimal(DevID);

                        stringBuilder = null;
                        activeDeviceAdapter.changeValue(fPosition,DevID);
                        try {
                            jo.put(dataList.get(fPosition),DevID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (stringBuilder.toString().length() == 14) {
                    if (stringBuilder.toString().equals("C56A2907FA0A41")) {
                        stringBuilder = null;
                        payloadAdapter.changeValue(fPosition,"Top up successfully");
                    } else if (stringBuilder.toString().equals("C56A2907FA0B1F")) {
                        stringBuilder = null;
                        payloadAdapter.changeValue(fPosition,"Latest keycode");
                    } else if (stringBuilder.toString().equals("C56A2907FAA090")) {
                        stringBuilder = null;
                        payloadAdapter.changeValue(fPosition,"Wrong keycode");
                    }
                }

//            System.out.println("----------list size--"+commandList.size()+"----------"+stringList.size());
//            if (!updateovcamplist){
//                System.out.println("--------------"+stringList.toString());
//                if (stringList.size() == commandList.size()){
//                    activeDeviceAdapter.notifyList1(stringList);
//                }
//            }

            }
        }catch (Exception e){

        }



    }






    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MqttService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }


    @Override
    public void OnBack(boolean msg) {
        if (msg){
            onBackPressed();
        }
    }

    @Override
    protected void onUsbDisconnect() {
        super.onUsbDisconnect();
        onBackPressed();
        myPref.setPref(Constant.USB_DISCONNECT,true);
    }

    //            Cursor cursor = contentResolver.query(ToDoProviderConstants.CONTENT_URI_1, null,null,null,null,null);
//
//            if(cursor != null){
//                while(cursor.moveToNext()){
//                    String ppid = cursor.getString(0);
//                    String codehasformat = cursor.getString(1);
//                    String devicetype = cursor.getString(2);
//                    ToDo toDo = new ToDo(ppid,codehasformat,devicetype);
//                    toDos.add(toDo);
//                }
//                newAdapter.notifyList(toDos);
//                listView.setAdapter(newAdapter);
//                System.out.println("----------size------------"+toDos.size());
//                cursor.close();
//            }



    //        str = myPref.getPref1(Constant.ACTIVE_DEVICE,deviceTypes);
//        if (!str.equals("")){
//            try {
//                JSONArray jsonArray = new JSONArray(str);
//                for (int i = 0; i < jsonArray.length(); i++){
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    PPID = jsonObject.getString("PPID");
//                    codehasformate = jsonObject.getString("code");
//                    deviceType = jsonObject.getString("deviceType");
//                    if (deviceType != null){
//                        tv_PPID.setText(PPID);
//                        tv_code.setText(codehasformate);
//                        tv_deviceType.setText(deviceType);
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//

    public static String formatString(String text){

        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                case '[':
                    json.append( indentString + letter + "\n");
                    indentString = indentString + "\t";
                    json.append(indentString);
                    break;
                case '}':
                case ']':
                    indentString = indentString.replaceFirst("\t", "");
                    json.append("\n" + indentString + letter);
                    break;
                case ',':
                    json.append(letter + "\n" + indentString);
                    break;

                default:
                    json.append(letter);
                    break;
            }
        }

        return json.toString();
    }
}
