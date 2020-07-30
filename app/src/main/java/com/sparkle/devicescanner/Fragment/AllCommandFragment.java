package com.sparkle.devicescanner.Fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sparkle.devicescanner.Adapter.AllCommandAdapter;
import com.sparkle.devicescanner.Adapter.TopicListAdapter;
import com.sparkle.devicescanner.LocalDatabase.DatabaseHelper;
import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.Constant;
import com.sparkle.devicescanner.Utils.MyPref;
import com.sparkle.devicescanner.events.ProtocolCommandEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AllCommandFragment extends Fragment {

    private RecyclerView rv_all_command;
    private LinearLayoutManager linearLayoutManager;
    private MyPref myPref;
    private List<String> data_list;
    private List<String> command_list;
    private AllCommandAdapter allCommandAdapter;
    private DatabaseHelper mdbhelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//         Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_command, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myPref = new MyPref(getContext());
        data_list = new ArrayList<>();
        command_list = new ArrayList<>();
        rv_all_command = view.findViewById(R.id.rv_all_command);
        rv_all_command.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rv_all_command.setLayoutManager(linearLayoutManager);

        allCommandAdapter = new AllCommandAdapter(getContext(),data_list,command_list);
        rv_all_command.setAdapter(allCommandAdapter);

        mdbhelper = new DatabaseHelper(getContext());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpinnerEvent(String event) {
        UpdateUI(event);
    }

    public void UpdateUI(String string) {
        data_list = new ArrayList<>();
        command_list = new ArrayList<>();
        if (string.equalsIgnoreCase("LUMN")){
            Cursor res = mdbhelper.getAlllumnData();
            while (res.moveToNext()){
                data_list.add(res.getString(res.getColumnIndex("lumnName")));
                command_list.add(res.getString(res.getColumnIndex("lumnCommand")));
            }
            allCommandAdapter.notifyList(data_list,command_list);
        }else if (string.equalsIgnoreCase("OVCAMP")){
            Cursor res = mdbhelper.getAllovCampData();
            while (res.moveToNext()){
                data_list.add(res.getString(res.getColumnIndex("ovCampName")));
                command_list.add(res.getString(res.getColumnIndex("ovCampCommand")));
            }
            allCommandAdapter.notifyList(data_list,command_list);
        }else if (string.equalsIgnoreCase("CATCH")){
            Cursor res = mdbhelper.getAlllumnData();
            while (res.moveToNext()){
                data_list.add(res.getString(res.getColumnIndex("lumnName")));
                command_list.add(res.getString(res.getColumnIndex("lumnCommand")));
            }
            allCommandAdapter.notifyList(data_list,command_list);
        }else {
            data_list = new ArrayList<>();
            command_list = new ArrayList<>();
            allCommandAdapter.notifyList(data_list,command_list);
        }


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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProtocolCommandEvent(ProtocolCommandEvent event) {
        String string = event.command;
        String[] strings;
        strings = string.split("C5");
        String str = strings[0]+ "+ C5"+strings[1];
        mdbhelper.insertovCampData(event.name,str);
        if (myPref.getPref(Constant.EVENT,"").equals("ovCamp")){
            UpdateUI("ovCamp");
        }
    }
}
