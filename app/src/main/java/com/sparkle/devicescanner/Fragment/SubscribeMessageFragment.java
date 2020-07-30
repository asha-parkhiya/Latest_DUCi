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
import com.sparkle.devicescanner.Adapter.DeviceTypeAdapter;
import com.sparkle.devicescanner.Adapter.TopicListAdapter;
import com.sparkle.devicescanner.Adapter.WifiMessageAdapter;
import com.sparkle.devicescanner.Model.PPID_TYPE.DeviceType;
import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.Constant;
import com.sparkle.devicescanner.Utils.ConstantMethod;
import com.sparkle.devicescanner.Utils.MyPref;
import com.sparkle.devicescanner.events.MqttStringEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SubscribeMessageFragment extends Fragment {

    private RecyclerView rv_usb_message;
    private RecyclerView rv_wifi_message;
    private RecyclerView rv_bluetooth_message;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager linearLayoutManager1;
    private LinearLayoutManager linearLayoutManager2;
    private TopicListAdapter topicListAdapter;
    private List<String> messageList;

    private List<ScanResult> wifiScanList;
    private List<BluetoothDevice> bluetoothDeviceList;
    private String wifis[];
    private List<String> wifiNAMElist;
    private List<String> bluetoothNameList;
    private BluetoothDevice bluetoothDevice;
    
    private WifiMessageAdapter wifiMessageAdapter;

    Gson gson;
    MyPref myPref;


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

    public SubscribeMessageFragment() {
        // Required empty public constructor
    }

    public static SubscribeMessageFragment newInstance() {
        SubscribeMessageFragment fragment = new SubscribeMessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribe_message, container, false);
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gson = new Gson();
        myPref = new MyPref(getContext());
        rv_usb_message = view.findViewById(R.id.rv_usb_message);
        rv_usb_message.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rv_usb_message.setLayoutManager(linearLayoutManager);

        rv_wifi_message = view.findViewById(R.id.rv_wifi_message);
        rv_wifi_message.setHasFixedSize(true);
        linearLayoutManager1 = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rv_wifi_message.setLayoutManager(linearLayoutManager1);

        rv_bluetooth_message = view.findViewById(R.id.rv_bluetooth_message);
        rv_bluetooth_message.setHasFixedSize(true);
        linearLayoutManager2 = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rv_bluetooth_message.setLayoutManager(linearLayoutManager2);
        

        messageList = new ArrayList<>();
        topicListAdapter = new TopicListAdapter(getActivity(),messageList);
        rv_usb_message.setAdapter(topicListAdapter);

    }

//    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
//    public void onCodeStringEvent(CodeStringEvent event) {
//        String device_type = event.device;
//        String codehasformate = event.code;
//        String PPID = event.ppid;
//
//        messageList = new ArrayList<>();
//        messageList.add(event.code);
//        topicListAdapter.notifyList(messageList);
//
//        EventBus.getDefault().removeAllStickyEvents();
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWifiMessageEvent(List<ScanResult> event) {

//        System.out.println("----------wifi event--------"+event);
        wifiScanList = new ArrayList<>();
        wifiNAMElist = new ArrayList<>();
        wifiScanList.clear();
        wifiScanList = event;
        wifis = new String[wifiScanList.size()];
        for(int i = 0; i < wifiScanList.size(); i++){
            wifis[i] = ((wifiScanList.get(i)).toString());
        }
        String filtered[] = new String[wifiScanList.size()];
        String filtered1[] = new String[wifiScanList.size()];

        int counter = 0;
        for (String eachWifi : wifis) {
            String[] temp = eachWifi.split(",");
            filtered[counter] = temp[0].substring(5).trim();//+"\n" + temp[2].substring(12).trim()+"\n" +temp[3].substring(6).trim();//0->SSID, 2->Key Management 3-> Strength
            filtered1[counter] = temp[1].substring(7).trim();//+"\n" + temp[2].substring(12).trim()+"\n" +temp[3].substring(6).trim();//0->SSID, 2->Key Management 3-> Strength
            wifiNAMElist.add(eachWifi);
        }
        wifiMessageAdapter = new WifiMessageAdapter(getContext(),wifiNAMElist);
        rv_wifi_message.setAdapter(wifiMessageAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBluetoothMessageEvent(String event) {

//        String temp[] = event.split("bluetooth");
//        try {
//            bluetoothNameList = new ArrayList<>();
//            bluetoothNameList.clear();
//
//            JSONArray jsonArray = new JSONArray(temp[1]);
//            for (int i = 0; i < jsonArray.length(); i++){
//                bluetoothNameList.add(String.valueOf(jsonArray.get(i)));
//                wifiMessageAdapter = new WifiMessageAdapter(getContext(),bluetoothNameList);
//                rv_bluetooth_message.setAdapter(wifiMessageAdapter);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MqttStringEvent event) {

        messageList.add(event.data);
        topicListAdapter.notifyList(messageList);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageStringEvent(String event) {
        if (event.equals("null")){
            messageList = new ArrayList<>();
            topicListAdapter = new TopicListAdapter(getActivity(),messageList);
            rv_usb_message.setAdapter(topicListAdapter);
        }

    }

//    @Override
//    protected void onListEventmessage(List<String> messageList, List<String> topicList) {
//        super.onListEventmessage(messageList,topicList);
//
//
////        Toast.makeText(getContext(), "--------------"+topicList.get(0), Toast.LENGTH_SHORT).show();
//
//    }

}
