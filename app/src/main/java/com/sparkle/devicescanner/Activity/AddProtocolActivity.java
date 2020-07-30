package com.sparkle.devicescanner.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sparkle.devicescanner.Adapter.AddProtocolAdapter;
import com.sparkle.devicescanner.Adapter.AllCommandAdapter;
import com.sparkle.devicescanner.LocalDatabase.DatabaseHelper;
import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.Constant;
import com.sparkle.devicescanner.Utils.MyPref;
import com.sparkle.devicescanner.Views.DialogProtocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddProtocolActivity extends AppCompatActivity implements View.OnClickListener, AddProtocolAdapter.onItemClickListener {

    private EditText edit_protocolType;
    private ImageView btn_add_protocol;
    private DatabaseHelper mdbhelper;
    private Toolbar toolbar;
//    private String typeJson;
//    private FloatingActionButton fab_add;
    private AddProtocolAdapter addProtocolAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<String> protocolName_list;
    private List<String> jsonObject_list;
    private MyPref myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_protocol);

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("Protocols");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        protocolName_list = new ArrayList<>();
        jsonObject_list = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.requestFocus();
        InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
        addProtocolAdapter = new AddProtocolAdapter(getApplicationContext(),protocolName_list,jsonObject_list,this);
        recyclerView.setAdapter(addProtocolAdapter);

        edit_protocolType = findViewById(R.id.edit_protocolType);
//        fab_add = findViewById(R.id.fab_add);
        btn_add_protocol = findViewById(R.id.btn_add_protocol);
        btn_add_protocol.setOnClickListener(this);
        mdbhelper = new DatabaseHelper(this);
        myPref = new MyPref(this);
//        Cursor res = mdbhelper.getAllProtocolType();
//        while (res.moveToNext()){
//            typeJson = res.getString(res.getColumnIndex("typeJson"));
//        }

        String valid = "\"enterCode\": {\n" +"\"cmdString\": \">WOTP:decimalstring<\"\n" + "\"expectReturn\": \"<RE:0000>\"\n" + "}";
        System.out.println("-----------------"+valid);
        System.out.println("-----------------"+isJSONValid(valid));
        Cursor res1 = mdbhelper.getAllJsonData();
        while (res1.moveToNext()){
            protocolName_list.add(res1.getString(res1.getColumnIndex("protocolType")));
            jsonObject_list.add(res1.getString(res1.getColumnIndex("protocolJson")));
        }
        addProtocolAdapter.notifyList(protocolName_list,jsonObject_list);


        System.out.println("---------name list------------------"+myPref.getPref(Constant.PROTOCOL_NAME_LIST,protocolName_list));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_protocol:

                if (!edit_protocolType.getText().toString().equals("")){
//                    String protocol = "{\"protocol\":\""+edit_protocolType.getText().toString()+"\"}";
                    btn_add_protocol.requestFocus();
                    InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(btn_add_protocol.getWindowToken(), 0);
                    if (edit_protocolType.getText().toString().equalsIgnoreCase("ovcamp") || edit_protocolType.getText().toString().equalsIgnoreCase("lumn") || edit_protocolType.getText().toString().equalsIgnoreCase("catch")){
                        if (protocolName_list.size() == 0){
                            System.out.println("-------------------1");
                            mdbhelper.insertJsonData(edit_protocolType.getText().toString(),"");
                            protocolName_list.add(edit_protocolType.getText().toString());
                            jsonObject_list.add("");
                            myPref.setPref(Constant.PROTOCOL_NAME_LIST,protocolName_list);
                            addProtocolAdapter.notifyList(protocolName_list,jsonObject_list);
                            Toast.makeText(AddProtocolActivity.this, edit_protocolType.getText().toString()+" Protocol added.", Toast.LENGTH_SHORT).show();
                        }else {
                            System.out.println("---------2----------"+myPref.getPref(Constant.PROTOCOL_NAME_LIST,protocolName_list));
                            System.out.println("---------2----------"+edit_protocolType.getText().toString());
                            String protocolList = myPref.getPref(Constant.PROTOCOL_NAME_LIST,protocolName_list);
                            if (protocolList.toLowerCase().contains(edit_protocolType.getText().toString().toLowerCase())){
                                Toast.makeText(AddProtocolActivity.this, edit_protocolType.getText().toString()+" Protocol already exists", Toast.LENGTH_SHORT).show();
                            }else {
                                mdbhelper.insertJsonData(edit_protocolType.getText().toString(),"");
                                protocolName_list.add(edit_protocolType.getText().toString());
                                jsonObject_list.add("");
                                addProtocolAdapter.notifyList(protocolName_list,jsonObject_list);
                                myPref.setPref(Constant.PROTOCOL_NAME_LIST,protocolName_list);
                                Toast.makeText(AddProtocolActivity.this, edit_protocolType.getText().toString()+" Protocol added.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        Toast.makeText(AddProtocolActivity.this, "You can't add "+edit_protocolType.getText().toString()+" protocol", Toast.LENGTH_SHORT).show();
                    }

                    edit_protocolType.setText("");
                }else {
                    Toast.makeText(AddProtocolActivity.this, "Please Add Protocol Type.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void OnItemClick(String protocoldis, String jsonObject, int position,String right_wrong) {

        System.out.println("---------protocoldis--------"+protocoldis);
        if (right_wrong.equals("right")){
            try {
                JSONObject jsonObject1 = new JSONObject(protocoldis);
                System.out.println("-----------jsonObject----------"+jsonObject);
                if (isJSONValid(jsonObject)){
                    mdbhelper.updateJsonData(jsonObject1.getString("protocol"),jsonObject);
                    Toast.makeText(AddProtocolActivity.this, "JSONObject updated.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AddProtocolActivity.this, "JSONObject is not valid. Please enter valid JSONObject.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (right_wrong.equals("wrong")){

        }else {
            try {
                JSONObject jsonObject1 = new JSONObject(protocoldis);
                Toast.makeText(AddProtocolActivity.this, jsonObject1.getString("protocol")+" protocol Deleted.", Toast.LENGTH_SHORT).show();
                mdbhelper.deleteJsonData(jsonObject1.getString("protocol"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



        protocolName_list = new ArrayList<>();
        jsonObject_list = new ArrayList<>();
        Cursor res1 = mdbhelper.getAllJsonData();
        while (res1.moveToNext()){
            protocolName_list.add(res1.getString(res1.getColumnIndex("protocolType")));
            jsonObject_list.add(res1.getString(res1.getColumnIndex("protocolJson")));
        }
        addProtocolAdapter = new AddProtocolAdapter(getApplicationContext(),protocolName_list,jsonObject_list,this);
        recyclerView.setAdapter(addProtocolAdapter);
        myPref.setPref(Constant.PROTOCOL_NAME_LIST,protocolName_list);


    }

    @Override
    public void OnItem(String protocoldis, String name, String jsonObject, int position) {
        DialogProtocol dialogProtocol = new DialogProtocol(AddProtocolActivity.this,protocoldis,name,jsonObject,this::OnItemClick);
        dialogProtocol.show();
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);

        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}
