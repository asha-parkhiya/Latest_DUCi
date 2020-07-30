package com.sparkle.devicescanner.Fragment;

import android.bluetooth.BluetoothDevice;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sparkle.devicescanner.Adapter.AccountDetailAdapter;
import com.sparkle.devicescanner.Adapter.BluetoothTopicAdapter;
import com.sparkle.devicescanner.Adapter.DeviceTypeAdapter;
import com.sparkle.devicescanner.Adapter.WifiTopicAdapter;
import com.sparkle.devicescanner.Model.AccountDetail;
import com.sparkle.devicescanner.Model.PPID_TYPE.DeviceType;
import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.Constant;
import com.sparkle.devicescanner.Utils.ConstantMethod;
import com.sparkle.devicescanner.Utils.MyPref;
import com.sparkle.devicescanner.WebServices.WebRequest;
import com.sparkle.devicescanner.events.MqttStringEvent;
import com.sparkle.devicescanner.events.PPIDDeviceTypeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class SubscribeTopicFragment extends Fragment implements AccountDetailAdapter.OnMclick, DeviceTypeAdapter.OnButtonclick {

    private RecyclerView rv_usb_topic;
    private RecyclerView rv_wifi_topic;
    private RecyclerView rv_bluetooth_topic;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager linearLayoutManager1;
    private LinearLayoutManager linearLayoutManager2;
    private AccountDetailAdapter accountDetailAdapter;
    private WifiTopicAdapter wifiTopicAdapter;
    private BluetoothTopicAdapter bluetoothTopicAdapter;
    private List<AccountDetail> accountDetailArrayList;
    private List<ScanResult> wifiScanList;
    private List<String> bluetoothDeviceList;
    private List<String> accountDetaillist;
    private Gson gson;
    private AccountDetail accountDetail;
    private StringBuilder stringBuilder;
    MyPref myPref;
    private String wifis[];
    private List<String> wifiNAMElist;
    private List<String> wifiBSSIDlist;
    private BluetoothDevice bluetoothDevice;
    private String LUMN_PPID;
    private WebRequest webRequest;

    private String codehasformate;
    private String PPID;
    private List<DeviceType> deviceTypeList;
    private DeviceType deviceType;
    private DeviceTypeAdapter deviceTypeAdapter;

    public SubscribeTopicFragment() {
        // Required empty public constructor
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
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public static SubscribeTopicFragment newInstance() {
        SubscribeTopicFragment fragment = new SubscribeTopicFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribe_topic, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stringBuilder = new StringBuilder();
        gson = new Gson();
        myPref = new MyPref(getContext());
        webRequest = new WebRequest(getContext());

        rv_usb_topic = view.findViewById(R.id.rv_usb_topic);
        rv_usb_topic.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rv_usb_topic.setLayoutManager(linearLayoutManager);

        rv_wifi_topic = view.findViewById(R.id.rv_wifi_topic);
        rv_wifi_topic.setHasFixedSize(true);
        linearLayoutManager1 = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rv_wifi_topic.setLayoutManager(linearLayoutManager1);

        rv_bluetooth_topic = view.findViewById(R.id.rv_bluetooth_topic);
        rv_bluetooth_topic.setHasFixedSize(true);
        linearLayoutManager2 = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rv_bluetooth_topic.setLayoutManager(linearLayoutManager2);

        accountDetail = new AccountDetail();
        accountDetailArrayList = new ArrayList<>();
        deviceTypeList = new ArrayList<>();
        deviceType = new DeviceType();

        accountDetaillist = new ArrayList<>();
        wifiNAMElist = new ArrayList<>();
        wifiBSSIDlist = new ArrayList<>();
        myPref.setPref(Constant.ACCOUNT_DETAIL,accountDetaillist);
        accountDetailAdapter = new AccountDetailAdapter(getContext(),accountDetailArrayList,SubscribeTopicFragment.this);
        rv_usb_topic.setAdapter(accountDetailAdapter);


        rv_wifi_topic.setAdapter(wifiTopicAdapter);

//        deviceTypeList.add(new DeviceType("FEB80000000044","<WOTP:028614261102035409317>",Constant.LUMN_DEVICE));
//        deviceTypeList.add(new DeviceType("FEB80000000045","<WOTP:091614261964035409507>",Constant.LUMN_DEVICE));
//        deviceTypeList.add(new DeviceType("FEB80000000046","<WOTP:091943261964001909507>",Constant.LUMN_DEVICE));
//        deviceTypeAdapter = new DeviceTypeAdapter(getContext(),deviceTypeList,SubscribeTopicFragment.this);
//        rv_usb_topic.setAdapter(deviceTypeAdapter);

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onWifiMessageEvent(List<ScanResult> event) {
//        wifiScanList = new ArrayList<>();
//        wifiScanList = event;
//        wifiNAMElist = new ArrayList<>();
//        wifis = new String[wifiScanList.size()];
//        for(int i = 0; i < wifiScanList.size(); i++){
//            wifis[i] = ((wifiScanList.get(i)).toString());
//        }
//        String filtered[] = new String[wifiScanList.size()];
//        String filtered1[] = new String[wifiScanList.size()];
//
//        int counter = 0;
//        for (String eachWifi : wifis) {
//            String[] temp = eachWifi.split(",");
//            filtered[counter] = temp[0].substring(5).trim();//+"\n" + temp[2].substring(12).trim()+"\n" +temp[3].substring(6).trim();//0->SSID, 2->Key Management 3-> Strength
////            filtered1[counter] = temp[1].substring(7).trim();//+"\n" + temp[2].substring(12).trim()+"\n" +temp[3].substring(6).trim();//0->SSID, 2->Key Management 3-> Strength
//            wifiNAMElist.add(filtered[counter]);
////            wifiBSSIDlist.add(filtered1[counter]);
//        }
//        wifiTopicAdapter.notifyList(wifiNAMElist);
//
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onBluetoothMessageEvent(String event) {
//
//        String temp[] = event.split("bluetooth");
//
////        try {
////            bluetoothDeviceList = new ArrayList<>();
////            bluetoothDeviceList.clear();
////
////            JSONArray jsonArray = new JSONArray(temp[1]);
////            for (int i = 0; i < jsonArray.length(); i++){
////                bluetoothDeviceList.add(String.valueOf(jsonArray.get(i)));
////                bluetoothTopicAdapter = new BluetoothTopicAdapter(getContext(),bluetoothDeviceList);
////                rv_bluetooth_topic.setAdapter(bluetoothTopicAdapter);
////            }
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
//
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MqttStringEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPPIDDeviceTypeEvent(PPIDDeviceTypeEvent event) {
        deviceTypeList = new ArrayList<>();
        myPref.setPref(Constant.ALL_TIME_HAND,true);
        myPref.setPref(Constant.HAND_START,false);
        String device_type = event.device;
        String PPID = event.ppid;
        System.out.println("-----------------"+device_type);

        if (device_type.equalsIgnoreCase("ovCamp")){
            myPref.setPref(Constant.DEVICE_TYPE,Constant.OVCAMP_DEVICE);
//            String code = ConstantMethod.code_string_for_card(codehasformate,PPID);
            deviceType = new DeviceType(PPID,codehasformate,Constant.OVCAMP_DEVICE);
            deviceTypeList.add(deviceType);
            deviceTypeAdapter = new DeviceTypeAdapter(getContext(),deviceTypeList,SubscribeTopicFragment.this);
            rv_usb_topic.setAdapter(deviceTypeAdapter);

            myPref.setPref1(Constant.ACTIVE_DEVICE,deviceTypeList);
            myPref.setPref(Constant.LUMN_USB,true);
//            byte[] datatmp1 = ConstantMethod.hexStr2Bytes(code);
//            EventBus.getDefault().post(datatmp1);
        }else if (device_type.equalsIgnoreCase("lumn")){
            myPref.setPref(Constant.DEVICE_TYPE,Constant.LUMN_DEVICE);
//            String code = ConstantMethod.code_has_for_lumn(codehasformate);
            deviceType = new DeviceType(PPID,codehasformate,Constant.LUMN_DEVICE);
            deviceTypeList.add(deviceType);
            deviceTypeAdapter = new DeviceTypeAdapter(getContext(),deviceTypeList,SubscribeTopicFragment.this);
            rv_usb_topic.setAdapter(deviceTypeAdapter);

            myPref.setPref1(Constant.ACTIVE_DEVICE,deviceTypeList);
            myPref.setPref(Constant.LUMN_USB,false);
//            byte[] datatmp1 = ConstantMethod.stringToByteArray(code);
//            EventBus.getDefault().post(datatmp1);
        }else if (device_type.equalsIgnoreCase("catch")){
            myPref.setPref(Constant.DEVICE_TYPE,Constant.CATCH_DEVICE);
//            String code = ConstantMethod.code_has_for_lumn(codehasformate);
            deviceType = new DeviceType(PPID,codehasformate,Constant.CATCH_DEVICE);
            deviceTypeList.add(deviceType);
            deviceTypeAdapter = new DeviceTypeAdapter(getContext(),deviceTypeList,SubscribeTopicFragment.this);
            rv_usb_topic.setAdapter(deviceTypeAdapter);

            myPref.setPref1(Constant.ACTIVE_DEVICE,deviceTypeList);
            myPref.setPref(Constant.LUMN_USB,false);
//            byte[] datatmp1 = ConstantMethod.stringToByteArray(code);
//            EventBus.getDefault().post(datatmp1);
        }
    }

//    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
//    public void onCodeStringEvent(CodeStringEvent event) {
//        deviceTypeList = new ArrayList<>();
//        myPref.setPref(Constant.ALL_TIME_HAND,true);
//        myPref.setPref(Constant.HAND_START,false);
//        String device_type = event.device;
//        String codehasformate = event.code;
//        String PPID = event.ppid;
//
//        if (device_type.startsWith("ovCamp")){
//            String string = " Device Type :" + Constant.OVCAMP_DEVICE;
//            EventBus.getDefault().post(string);
//            myPref.setPref(Constant.DEVICE_TYPE,Constant.OVCAMP_DEVICE);
//            String code = ConstantMethod.code_string_for_card(codehasformate,PPID);
//            deviceType = new DeviceType(PPID,codehasformate,Constant.OVCAMP_DEVICE);
//            deviceTypeList.add(deviceType);
//            deviceTypeAdapter = new DeviceTypeAdapter(getContext(),deviceTypeList,SubscribeTopicFragment.this);
//            rv_usb_topic.setAdapter(deviceTypeAdapter);
//
//            EventBus.getDefault().post("When device detected pass hash code : "+code);
//            myPref.setPref1(Constant.ACTIVE_DEVICE,deviceTypeList);
//            myPref.setPref(Constant.LUMN_USB,true);
//            byte[] datatmp1 = ConstantMethod.hexStr2Bytes(code);
//            EventBus.getDefault().post(datatmp1);
//        }else {
//            String string = " Device Type :" + Constant.LUMN_DEVICE;
//            EventBus.getDefault().post(string);
//            myPref.setPref(Constant.DEVICE_TYPE,Constant.LUMN_DEVICE);
//            String code = ConstantMethod.code_has_for_lumn(codehasformate);
//            deviceType = new DeviceType(PPID,codehasformate,Constant.LUMN_DEVICE);
//            deviceTypeList.add(deviceType);
//            deviceTypeAdapter = new DeviceTypeAdapter(getContext(),deviceTypeList,SubscribeTopicFragment.this);
//            rv_usb_topic.setAdapter(deviceTypeAdapter);
//
//            EventBus.getDefault().post("When device detected pass 21 bit code .. ");
//            myPref.setPref1(Constant.ACTIVE_DEVICE,deviceTypeList);
//            myPref.setPref(Constant.LUMN_USB,false);
//            byte[] datatmp1 = ConstantMethod.stringToByteArray(code);
//            EventBus.getDefault().post(datatmp1);


//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    if (!myPref.getPref(Constant.GET_REPLY,false)){
//                        if (!myPref.getPref(Constant.HAND_START,false) && !myPref.getPref(Constant.OVCAMP_CODE_START,false)){
//                            System.out.println("-----------------------hand frag-");
//                            myPref.setPref(Constant.ALL_TIME_HAND,false);
//                            myPref.setPref(Constant.CALL_LUMN_CAMP,false);
//                            myPref.setPref(Constant.NO_OVCAMP_LUMN,false);
//                            ((HomeActivity)getActivity()).callLumnCamp();
//                        }
//                    }
//                }
//            }, 1000*30);

//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    if (!myPref.getPref(Constant.GET_REPLY,false)){
//                        if (!myPref.getPref(Constant.HAND_START,false)){
//                            EventBus.getDefault().post("When device detected pass 21 bit code second time : "+code);
//                            System.out.println("---------write--code--lumn---------"+code);
//                            byte[] datatmp1 = ConstantMethod.stringToByteArray(code);
//                            EventBus.getDefault().post(datatmp1);
//                        }
//
//                    }
//                }
//            }, 1000*30);

//        }
//        EventBus.getDefault().removeAllStickyEvents();
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageStringEvent(String event) {

        String temp[] = event.split("bluetooth");
        if (event.equals("null")){
            accountDetail = new AccountDetail();
            accountDetailArrayList = new ArrayList<>();
            accountDetailAdapter = new AccountDetailAdapter(getContext(),accountDetailArrayList,SubscribeTopicFragment.this);
            rv_usb_topic.setAdapter(accountDetailAdapter);
        }else {
            if (!temp[0].equals("")){
//                Toast.makeText(getContext(), ""+event, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMclick(int position) {
        if (accountDetailArrayList.get(position).getCode() != null){
            String code = accountDetailArrayList.get(position).getCode();
//            byte[] datatmp1 = ConstantMethod.hexStr2Bytes(code);
//            EventBus.getDefault().post(datatmp1);
        }

//        sendString(datatmp1);
    }

    @Override
    public void OnButtonclick(int position) {

        switch (myPref.getPref(Constant.DEVICE_TYPE,"")){
//            case Constant.IC_CARD_DEVICE:
////                if (deviceTypeList.get(position).getcode() != null) {
////                    String code = deviceTypeList.get(position).getcode();
////                    System.out.println("---------write--code-----------"+code);
////                    byte[] datatmp1 = ConstantMethod.hexStr2Bytes(code);
////                    EventBus.getDefault().post(datatmp1);
////                }
//                break;
//
//            case Constant.LUMN_DEVICE:
//                if (deviceTypeList.get(position).getcode() != null) {
//                    String code = deviceTypeList.get(position).getcode();
//                    EventBus.getDefault().post("When click write button pass 21 bit code : "+code);
//                    System.out.println("---------write--code--lumn---------"+code);
//                    byte[] datatmp1 = ConstantMethod.stringToByteArray(code);
//                    EventBus.getDefault().post(datatmp1);
//                }
//                break;
//
//            case Constant.OVCAMP_DEVICE:
//                if (deviceTypeList.get(position).getcode() != null) {
//                    String code = deviceTypeList.get(position).getcode();
//                    EventBus.getDefault().post("When click write button pass hash code : "+code);
//                    System.out.println("---------write--code---ovcamp--------"+code);
//                    byte[] datatmp1 = ConstantMethod.hexStr2Bytes(code);
//                    EventBus.getDefault().post(datatmp1);
//                }
//                break;
        }
    }
//
//    @Override
//    protected void onUsbSerialMessage(String message) {
//        super.onUsbSerialMessage(message);
//        System.out.println(message+"--");
//        stringBuilder.append(message);
//
//        String msg = stringBuilder.toString();
//
//        if(msg.length()>=15){
//            Toast.makeText(getContext(), "-----------------"+msg, Toast.LENGTH_SHORT).show();
//        }
//
//    }


}
