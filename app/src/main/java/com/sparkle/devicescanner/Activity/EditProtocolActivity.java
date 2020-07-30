package com.sparkle.devicescanner.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sparkle.devicescanner.LocalDatabase.DatabaseHelper;
import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.Constant;
import com.sparkle.devicescanner.Utils.MyPref;
import com.sparkle.devicescanner.Views.DialogDisplayMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditProtocolActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, DialogDisplayMessage.OnBack {

    private Toolbar toolbar;
    private MyPref myPref;
    private String strJsonObject;
    private Spinner spinner;
    private static final String[] paths = { "LUMN", "ovCamp"};
    private DatabaseHelper mdbhelper;
    private EditText json_edittext;
    private TextView json_text;
    private Button btn_edit_json;
    private String[] strings = {};
    private List<String> protocolName_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_protocol);

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("Edit Protocol");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        myPref = new MyPref(this);
        spinner = (Spinner) findViewById(R.id.spinner);
        mdbhelper = new DatabaseHelper(this);
        json_text = findViewById(R.id.json_text);
        json_edittext = findViewById(R.id.json_edittext);
        btn_edit_json = findViewById(R.id.btn_edit_json);
        btn_edit_json.setOnClickListener(this);

        protocolName_list = new ArrayList<>();
        Cursor res1 = mdbhelper.getAllJsonData();
        while (res1.moveToNext()){
            protocolName_list.add(res1.getString(res1.getColumnIndex("protocolType")));
        }

        System.out.println("--------------------"+protocolName_list.toString());
//        strings = protocolName_list.toString();
//        try {
//            JSONObject jsonObject = new JSONObject("");
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
//        if (strings.length == 0){
//            DialogDisplayMessage dialogDisplayMessage = new DialogDisplayMessage(EditProtocolActivity.this,"Please First Add Protocol",this);
//            dialogDisplayMessage.show();
//        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProtocolActivity.this, R.layout.spinner_item, protocolName_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
//        int spinnerPosition = adapter.getPosition("LUMN");
//        spinner.setSelection(spinnerPosition);
        if (myPref.getPref(Constant.JSONPOSITION,0) == 0) {
            spinner.setSelection(0);
        }else if (myPref.getPref(Constant.JSONPOSITION,1) == 1){
            spinner.setSelection(1);
        }
        spinner.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        for (int i = 0; i < strings.length ; i++){
            if (position == i){
                myPref.setPref(Constant.JSONPOSITION,position);
                Cursor res = mdbhelper.getJsonData(strings[i]);
                while (res.moveToNext()){
                    strJsonObject = res.getString(res.getColumnIndex("protocolJson"));
                    System.out.println("----------strJsonObject----------"+strJsonObject);
                    json_text.setText(formatString(strJsonObject));
                    System.out.println("--------------------"+formatString(strJsonObject));
                }
            }
        }
//        switch (position){
//            case 0:
//                System.out.println("--------------------"+position);
//                myPref.setPref(Constant.JSONPOSITION,position);
//                Cursor res = mdbhelper.getJsonData("LUMN");
//                while (res.moveToNext()){
//                    strJsonObject = res.getString(res.getColumnIndex("protocolJson"));
//                    json_text.setText(formatString(strJsonObject));
//                }
//                break;
//            case 1:
//                myPref.setPref(Constant.JSONPOSITION,position);
//                Cursor res1 = mdbhelper.getJsonData("ovCamp");
//                while (res1.moveToNext()){
//                    strJsonObject = res1.getString(res1.getColumnIndex("protocolJson"));
//                    json_text.setText(formatString(strJsonObject));
//                }
//                break;
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit_json:
                if (strings.length != 0){
                    if (btn_edit_json.getText().equals("EDIT JSON")){
                        json_text.setVisibility(View.GONE);
                        json_edittext.setVisibility(View.VISIBLE);
                        strJsonObject = strJsonObject.replaceAll("[\\n\\t ]", "");
                        json_edittext.setText(formatString(strJsonObject));
                        btn_edit_json.setText("UPDATE JSON");
                        spinner.setEnabled(false);
                    }else if (btn_edit_json.getText().toString().equals("UPDATE JSON")){
                        btn_edit_json.requestFocus();
                        InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(btn_edit_json.getWindowToken(), 0);
                        String updateJsonString = json_edittext.getText().toString().replaceAll("[\\n\\t ]", "");
                        System.out.println("---------------validation--------------"+isJSONValid(updateJsonString));
                        if (isJSONValid(updateJsonString)){
                            for (int i = 0; i < strings.length; i++){
                                if (myPref.getPref(Constant.JSONPOSITION,0) == i){
//                                    mdbhelper.updateJsonData(strings[i],updateJsonString);
                                    Toast.makeText(EditProtocolActivity.this, "JSONObject Added Successfully..", Toast.LENGTH_SHORT).show();
                                    json_edittext.setVisibility(View.GONE);
                                    json_text.setVisibility(View.VISIBLE);
                                    json_text.setText((json_edittext.getText().toString()));
                                    strJsonObject = json_edittext.getText().toString().replaceAll("[\\n\\t ]", "");
                                    btn_edit_json.setText("EDIT JSON");
                                    spinner.setEnabled(true);
                                }
                            }
                        }else {
                            Toast.makeText(EditProtocolActivity.this, "JSONObject is not valid. Please enter valid JSONObject.", Toast.LENGTH_SHORT).show();
                            json_edittext.setVisibility(View.GONE);
                            json_text.setVisibility(View.VISIBLE);
                            for (int i = 0; i < strings.length ; i++){
                                if (myPref.getPref(Constant.JSONPOSITION,0) == i){
                                    Cursor res = mdbhelper.getJsonData(strings[i]);
                                    while (res.moveToNext()){
                                        strJsonObject = res.getString(res.getColumnIndex("protocolJson"));
                                        json_text.setText(formatString(strJsonObject));
                                    }
                                }
                            }
                            btn_edit_json.setText("EDIT JSON");
                            spinner.setEnabled(true);
                        }

                    }
                }else {
                    Toast.makeText(EditProtocolActivity.this, "Please First Add Protocol.", Toast.LENGTH_SHORT).show();
                }


                break;
        }
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

    public static String formatString(String text){

        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                case '[':
                    json.append("\n" + indentString + letter + "\n");
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

    @Override
    public void OnBack(boolean msg) {
        if (msg) {
            onBackPressed();
        }
    }
}
