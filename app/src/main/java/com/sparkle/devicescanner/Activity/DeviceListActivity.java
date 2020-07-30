package com.sparkle.devicescanner.Activity;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sparkle.devicescanner.Adapter.AccountDetailAdapter;
import com.sparkle.devicescanner.Adapter.BluetoothDeviceListAdapter;
import com.sparkle.devicescanner.Adapter.DataCableListAdapter;
import com.sparkle.devicescanner.Adapter.WifiDeviceListAdapter;
import com.sparkle.devicescanner.Base.BaseActivity;
import com.sparkle.devicescanner.Fragment.SubscribeTopicFragment;
import com.sparkle.devicescanner.Model.AccountDetail;
import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.Constant;
import com.sparkle.devicescanner.Utils.ConstantMethod;
import com.sparkle.devicescanner.Utils.MyPref;
import com.sparkle.devicescanner.events.UsbSerialMessageEvent;
//import com.sparkle.devicescanner.services.BluetoothService;
import com.sparkle.devicescanner.services.MqttService;
import com.sparkle.devicescanner.services.UsbService;
import com.sparkle.devicescanner.services.WifiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DeviceListActivity extends BaseActivity implements DataCableListAdapter.OnMclick{

    RecyclerView rv_wifi_device_list;
    RecyclerView rv_bluetooth_device_list;
    RecyclerView rv_datacable_device_list;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager linearLayoutManager1;
    private LinearLayoutManager linearLayoutManager2;
    private BluetoothDeviceListAdapter bluetoothDeviceListAdapter;
    private DataCableListAdapter dataCableListAdapter;
    private MyPref myPref;
    String wifi;
    Gson gson;
    WifiService wifiService;
//    BluetoothService bluetoothService;
    Intent mServiceIntent;
    private Toolbar toolbar;
    List<String> bluetoothDeviceList;
    List<ScanResult> wifiScanList = new ArrayList<>();
    BluetoothDevice bluetoothDevice;
    ImageView btn_scan;
    AccountDetail accountDetails;
    List<AccountDetail> accountDetailList;
    StringBuilder stringBuilder;
    String ppidFromCard;

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        btn_scan = (ImageView)findViewById(R.id.btn_scan);
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("Device List");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        myPref = new MyPref(this);
        gson = new Gson();
        wifiService = new WifiService();
//        bluetoothService = new BluetoothService();
        mServiceIntent = new Intent(getApplicationContext(), wifiService.getClass());

        rv_wifi_device_list = (RecyclerView)findViewById(R.id.rv_wifi_device_list);
        rv_wifi_device_list.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        rv_wifi_device_list.setLayoutManager(linearLayoutManager);

        rv_bluetooth_device_list = (RecyclerView)findViewById(R.id.rv_bluetooth_device_list);
        rv_bluetooth_device_list.setHasFixedSize(true);
        linearLayoutManager1 = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        rv_bluetooth_device_list.setLayoutManager(linearLayoutManager1);

        rv_datacable_device_list = (RecyclerView)findViewById(R.id.rv_datacable_device_list);
        rv_datacable_device_list.setHasFixedSize(true);
        linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        rv_datacable_device_list.setLayoutManager(linearLayoutManager2);


        refresh_method();

        Animation animation = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(1000);

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_scan.clearAnimation();
                btn_scan.setAnimation(animation);
                wifiService.scanWifi(getApplicationContext());
//                bluetoothService.scanBluetooth(getApplicationContext());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh_method();
                        btn_scan.clearAnimation();
                    }
                },5000);

            }
        });

    }

    public void refresh_method(){
        wifiScanList = wifiService.getScanResult(getApplicationContext());
        WifiDeviceListAdapter wifiDeviceListAdapter = new WifiDeviceListAdapter(this,wifiScanList);
        rv_wifi_device_list.setAdapter(wifiDeviceListAdapter);



        bluetoothDeviceList = new ArrayList<>();

        String bluetoothstr = myPref.getPref(Constant.BLUETOOTH_DEVICE_LIST,"");
        if (!bluetoothstr.equals("")){
            System.out.println("--------------------------------------"+bluetoothstr);
            try {
                JSONArray jsonArray = new JSONArray(bluetoothstr);
                for (int i = 0; i < jsonArray.length(); i++){
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    bluetoothDevice = gson.fromJson(jsonObject.toString(),BluetoothDevice.class);
                    bluetoothDeviceList.add(String.valueOf(jsonArray.get(i)));
                    bluetoothDeviceListAdapter = new BluetoothDeviceListAdapter(getApplicationContext(),bluetoothDeviceList);
                    rv_bluetooth_device_list.setAdapter(bluetoothDeviceListAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String acc_detail = myPref.getPref(Constant.ACCOUNT_DETAIL,"");
        System.out.println("--------------------------------------"+acc_detail);

        if (!acc_detail.equals("")){
            try {
                accountDetailList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(acc_detail);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    accountDetails = gson.fromJson(jsonObject.toString(),AccountDetail.class);
                    accountDetailList.add(accountDetails);
                    dataCableListAdapter = new DataCableListAdapter(getApplicationContext(),accountDetailList,this);
                    rv_datacable_device_list.setAdapter(dataCableListAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMclick(int position) {
        if (accountDetailList.get(position).getCode() != null){
            String code = accountDetailList.get(position).getCode();
            byte[] datatmp1 = ConstantMethod.hexStr2Bytes(code);
            EventBus.getDefault().post(datatmp1);
            onUsbReady();

        }
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
    protected void onUsbReady() {
        super.onUsbReady();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String read = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C5 6A 29 07 01 02 39";
                byte[] readtmp = ConstantMethod.hexStr2Bytes(read);
                startReadingCard(readtmp);
                EventBus.getDefault().post("null");
            }
        }, 1000);
    }

    public void onUsbSerialMessage(String message) {
        if (stringBuilder == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(message);
        } else
            stringBuilder.append(message);

        System.out.println("len: " + stringBuilder.toString().length());

        if (stringBuilder.toString().length() > 15)
            if (stringBuilder.toString().equals("C56A290801030A02")) {
                EventBus.getDefault().post(stringBuilder.toString());
                stringBuilder = null;
            }
    }

    @Override
    protected void onBluetoothDevice(String event) {
        super.onBluetoothDevice(event);
        System.out.println("----------------device event------------------"+event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageStringEvent(String event) {

        String temp[] = event.split("bluetooth");
        if (event.equals("null")){

        }else {
            if (!temp[0].equals("")){
                Toast.makeText(DeviceListActivity.this, ""+event, Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBluetoohDeviceList(String event) {
        System.out.println("--------helllo---------"+event);
    }

}
