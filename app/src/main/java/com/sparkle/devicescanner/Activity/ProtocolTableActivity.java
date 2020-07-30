package com.sparkle.devicescanner.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.tabs.TabLayout;
import com.sparkle.devicescanner.Fragment.AllCommandFragment;
import com.sparkle.devicescanner.Fragment.IndividualCommandFragment;
import com.sparkle.devicescanner.Fragment.SubscribeMessageFragment;
import com.sparkle.devicescanner.Fragment.SubscribeTopicFragment;
import com.sparkle.devicescanner.LocalDatabase.DatabaseHelper;
import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.Constant;
import com.sparkle.devicescanner.Utils.MyPref;
import com.sparkle.devicescanner.Views.DialogDisplayMessage;
import com.sparkle.devicescanner.services.MqttService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProtocolTableActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DialogDisplayMessage.OnBack {

    private Toolbar toolbar;
    private ViewPagerAdapter viewPagerAdapter;
//    private TabLayout tab_Layout;
    private ViewPager view_Pager;
    private Spinner spinner;
    private static final String[] paths = { "LUMN", "ovCamp"};
//    private String[] strings = {};
    private MyPref myPref;
    private DatabaseHelper mdbhelper;
    private String str;
    private List<String> protocolName_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol_table);

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("Protocol Table");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mdbhelper = new DatabaseHelper(this);
        protocolName_list = new ArrayList<>();
        Cursor res1 = mdbhelper.getAllJsonData();
        while (res1.moveToNext()){
            protocolName_list.add(res1.getString(res1.getColumnIndex("protocolType")));
        }

        System.out.println("--------------------"+protocolName_list.toString());

//        try {
//            JSONObject jsonObject = new JSONObject(str);
//            String s = jsonObject.get("protocolLists").toString();
//            JSONArray jsonArray = new JSONArray(s);
//            for (int i = 0; i < jsonArray.length(); i++){
//
//                String newItem = jsonArray.get(i).toString();
//                int currentSize = strings.length;
//                int newSize = currentSize + 1;
//                String[] tempArray = new String[ newSize ];
//                for (int i1=0; i1 < currentSize; i1++)
//                {
//                    tempArray[i1] = strings [i1];
//                }
//                tempArray[newSize- 1] = newItem;
//                strings = tempArray;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
        if (protocolName_list.size() == 0){
            DialogDisplayMessage dialogDisplayMessage = new DialogDisplayMessage(ProtocolTableActivity.this,"Please First Add Protocol",this);
            dialogDisplayMessage.show();
        }

        myPref = new MyPref(this);
        spinner = (Spinner) findViewById(R.id.spinner);

//        tab_Layout = (TabLayout) findViewById(R.id.tab_layout1);
        view_Pager = (ViewPager) findViewById(R.id.view_pager1);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new AllCommandFragment(), "All Command");
//        viewPagerAdapter.addFragment(new IndividualCommandFragment(), "Individual Command");

        view_Pager.setAdapter(viewPagerAdapter);
//        tab_Layout.setupWithViewPager(view_Pager);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProtocolTableActivity.this, R.layout.spinner_item, protocolName_list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
//        int spinnerPosition = adapter.getPosition("LUMN");
//        spinner.setSelection(spinnerPosition);
        if (myPref.getPref(Constant.PROTOCOLTABLEPOSITION,0) == 0) {
            spinner.setSelection(0);
        }else if (myPref.getPref(Constant.PROTOCOLTABLEPOSITION,1) == 1){
            spinner.setSelection(1);
        }
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        for (int i = 0; i < protocolName_list.size() ; i++){
            if (position == i){
                myPref.setPref(Constant.PROTOCOLTABLEPOSITION,position);
                EventBus.getDefault().post(protocolName_list.get(i));
                myPref.setPref(Constant.EVENT,protocolName_list.get(i));
            }
        }
//        switch (position){
//            case 0:
//                myPref.setPref(Constant.PROTOCOLTABLEPOSITION,position);
//                EventBus.getDefault().post("LUMN");
//                myPref.setPref(Constant.EVENT,"LUMN");
//                break;
//            case 1:
//                myPref.setPref(Constant.PROTOCOLTABLEPOSITION,position);
//                EventBus.getDefault().post("ovCamp");
//                myPref.setPref(Constant.EVENT,"ovCamp");
//                break;
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void OnBack(boolean msg) {
        if (msg){
            onBackPressed();
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWifiMessageEvent(List<ScanResult> event) {}

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
}
