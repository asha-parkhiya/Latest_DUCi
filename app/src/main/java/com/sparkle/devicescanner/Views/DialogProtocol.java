package com.sparkle.devicescanner.Views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.sparkle.devicescanner.Adapter.AddProtocolAdapter;
import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.MyPref;

import static com.sparkle.devicescanner.Adapter.AddProtocolAdapter.formatString;

public class DialogProtocol extends Dialog implements View.OnClickListener {
    private MyPref myPref;

    private OnBack onBack;

    private String message;
    private AppCompatButton btn_save,btn_cancel,btn_default;
    private RelativeLayout rl_right_wrong,relativeView;
    private TextView edit_text;
    private EditText edit_jsonObject;
    private TextView text_first_jsonObject;
    private TextView text_last_jsonObject;
    private String listData;
    private String listCommand;
    private String protocolName;
    private String protocol;

    public DialogProtocol(Context context, String protocolName, String s, String s1, onItemClickListener listener) {
        super(context);
        this.protocolName = protocolName;
        this.listData = s;
        this.listCommand = s1;
        this.listener = listener;
    }

    public interface onItemClickListener{
        void OnItemClick(String protocoldis,String jsonObject,int position,String right_wrong);
    }

    private onItemClickListener listener;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;

            case R.id.btn_save:

                break;
        }
    }

//    public DialogProtocol(Context context, onItemClickListener listener) {
//        super(context);
//        this.listener = listener;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_protocol);
        Window w = getWindow();
        setCancelable(false);
        myPref = new MyPref(getContext());
        w.getAttributes().windowAnimations = R.style.DialogAnimation;
        w.setBackgroundDrawableResource(android.R.color.transparent);
        edit_text = findViewById(R.id.edit_text);
        edit_jsonObject = findViewById(R.id.edit_jsonObject);
        text_first_jsonObject = findViewById(R.id.text_first_jsonObject);
        text_last_jsonObject = findViewById(R.id.text_last_jsonObject);
        rl_right_wrong = findViewById(R.id.rl_right_wrong);
        relativeView = findViewById(R.id.relativeView);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_default = findViewById(R.id.btn_default);


        System.out.println("-------------------"+protocolName);
        edit_text.setText(protocolName);

        if (!listCommand.equals("")){
            if (listData.equalsIgnoreCase("ovcamp")){
                String finalstr = "{\"protocol\":\""+listData+"\",\"version\":0.0,\"createDate\":\"2020-07-15\",";
                finalstr = listCommand.replace(finalstr,"");
                finalstr = finalstr.replaceFirst(".$","");
                finalstr = finalstr.replaceAll("[\\n\\t ]", "");
//                System.out.println("-----------------------"+finalstr);
//                System.out.println("-----------------------"+formatString(finalstr));
                edit_jsonObject.setText(formatString(finalstr));
            }else if (listData.equalsIgnoreCase("lumn")){
                String finalstr = "{\"protocol\":\""+listData+"\",\"version\":0.0,\"createDate\":\"2020-07-15\",";
                finalstr = listCommand.replace(finalstr,"");
                finalstr = finalstr.replaceFirst(".$","");
                finalstr = finalstr.replaceAll("[\\n\\t ]", "");
                edit_jsonObject.setText(formatString(finalstr));
            }else if (listData.equalsIgnoreCase("catch")){
                String finalstr = "{\"protocol\":\""+listData+"\",\"version\":0.0,\"createDate\":\"2020-07-15\",";
                finalstr = listCommand.replace(finalstr,"");
                finalstr = finalstr.replaceFirst(".$","");
                finalstr = finalstr.replaceAll("[\\n\\t ]", "");
                edit_jsonObject.setText(formatString(finalstr));
            }else {
                edit_jsonObject.setText(formatString(listCommand));
            }
        }

        if (listData.equalsIgnoreCase("ovcamp")){
            String finalstr = "{\n" +
                    "  \"protocol\": \""+listData+"\",\n" +
                    "  \"version\": 0.0,\n" +
                    "  \"createDate\": \"2020-07-15\"," +
                    "}";
            finalstr = finalstr.replaceFirst(".$","");
            text_first_jsonObject.setText(finalstr);
            text_last_jsonObject.setText("}");
        }else if (listData.equalsIgnoreCase("lumn")){
            String finalstr ="{\n" +
                    "  \"protocol\": \""+listData+"\",\n" +
                    "  \"version\": 0.0,\n" +
                    "  \"createDate\": \"2020-07-15\"," +
                    "}";
            finalstr = finalstr.replaceFirst(".$","");
            text_first_jsonObject.setText(finalstr);
            text_last_jsonObject.setText("}");
        }else if (listData.equalsIgnoreCase("catch")){
            String finalstr ="{\n" +
                    "  \"protocol\": \""+listData+"\",\n" +
                    "  \"version\": 0.0,\n" +
                    "  \"createDate\": \"2020-07-15\"," +
                    "}";
            finalstr = finalstr.replaceFirst(".$","");
            text_first_jsonObject.setText(finalstr);
            text_last_jsonObject.setText("}");
        }else {
            text_first_jsonObject.setText("");
            text_last_jsonObject.setText("");
        }

        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edit_jsonObject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                showRecycler();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendrightsign = text_first_jsonObject.getText().toString() + edit_jsonObject.getText().toString() + text_last_jsonObject.getText().toString();
                sendrightsign = sendrightsign.replaceAll("[\\n\\t ]", "");
                sendrightsign = sendrightsign.replaceAll("\\s+", "");
                listener.OnItemClick(edit_text.getText().toString(),sendrightsign,0+1,"right");
                dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick("","",0,"wrong");
                dismiss();
            }
        });

        btn_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listData.equalsIgnoreCase("ovCamp")){
                    protocol = "\"busWake\" : \"00000000000000000000000000000000\",\n" +
                            "                \"headCode\": \"C56A29\",\n" +
                            "                \"cmdIntervalms\" : 1000,\n" +
                            "                \"enterCode\":{\n" +
                            "                    \"cmdLength\": x00E,\n" +
                            "                    \"cmdWord\": x0FA,\n" +
                            "                    \"payloadType\":\"decimalToken\",\n" +
                            "                    \"cmdString\": \"busWake + headCode + cmdLength + cmdWord + [code payload calculated from decimal string] + [crc8 chechsum]\"\n" +
                            "                },\n" +
                            "                \"getDevID\": {\n" +
                            "                    \"cmdLength\": x006,\n" +
                            "                    \"cmdWord\": x0AF,\n" +
                            "                    \"crc8\": 25,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A2906AF25\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getPPID\": {\n" +
                            "                    \"cmdLength\": x007,\n" +
                            "                    \"cmdWord\": x008,\n" +
                            "                    \"cmdReadLength\": x014,\n" +
                            "                    \"crc8\": CB,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29070814CB\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getHashTop\": {\n" +
                            "                    \"cmdLength\": x006,\n" +
                            "                    \"cmdWord\": x011,\n" +
                            "                    \"crc8\": 08,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29061108\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getRPD\": {\n" +
                            "                    \"cmdLength\": x006,\n" +
                            "                    \"cmdWord\": x005,\n" +
                            "                    \"crc8\": F4,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A290605F4\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getTRD\": {\n" +
                            "                    \"cmdLength\": x06,\n" +
                            "                    \"cmdWord\": x06,\n" +
                            "                    \"crc8\": 16,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060616\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getTPD\": {\n" +
                            "                    \"cmdLength\": x006,\n" +
                            "                    \"cmdWord\": x007,\n" +
                            "                    \"crc8\": 48,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060748\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getPS\": {\n" +
                            "                    \"cmdLength\": x006,\n" +
                            "                    \"cmdWord\": x009,\n" +
                            "                    \"crc8\": 57,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060957\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getOCS\": {\n" +
                            "                    \"cmdLength\": x006,\n" +
                            "                    \"cmdWord\": x00A,\n" +
                            "                    \"crc8\": B5,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060AB5\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getSysCode\": {\n" +
                            "                    \"cmdLength\": x006,\n" +
                            "                    \"cmdWord\": x00B,\n" +
                            "                    \"crc8\": EB,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060BEB\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getRSOC\": {\n" +
                            "                    \"cmdLength\": x006,\n" +
                            "                    \"cmdWord\": x00C,\n" +
                            "                    \"crc8\": 68,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060C68\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getRC\": {\n" +
                            "                    \"cmdLength\": x006,\n" +
                            "                    \"cmdWord\": x00D,\n" +
                            "                    \"crc8\": 36,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060D36\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getFCC\": {\n" +
                            "                    \"cmdLength\": x006,\n" +
                            "                    \"cmdWord\": x00E,\n" +
                            "                    \"crc8\": D4,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060ED4\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getAEO\": {\n" +
                            "                    \"cmdLength\": x006,\n" +
                            "                    \"cmdWord\": x00F,\n" +
                            "                    \"crc8\": 8A,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29060F8A\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getADC\": {\n" +
                            "                    \"cmdLength\": x006,\n" +
                            "                    \"cmdWord\": x010,\n" +
                            "                    \"crc8\": 56,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29061056\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getRDBck\": {\n" +
                            "                    \"cmdLength\": x006,\n" +
                            "                    \"cmdWord\": x013,\n" +
                            "                    \"crc8\": B4,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A290613B4\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getPAYG_REV\": {\n" +
                            "                    \"cmdLength\": x006,\n" +
                            "                    \"cmdWord\": x014,\n" +
                            "                    \"crc8\": 37,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29061437\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getCV1\": {\n" +
                            "                    \"cmdLength\": x008,\n" +
                            "                    \"cmdWord\": x000,\n" +
                            "                    \"cmd3060address \": x03F,\n" +
                            "                    \"cmdReadLength \": x002,\n" +
                            "                    \"crc8\": 0B,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A2908003F020B\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getCV2\": {\n" +
                            "                    \"cmdLength\": x008,\n" +
                            "                    \"cmdWord\": x000,\n" +
                            "                    \"cmd3060address \": x03E,\n" +
                            "                    \"cmdReadLength \": x002,\n" +
                            "                    \"crc8\": CF,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A2908003E02CF\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getCV3\": {\n" +
                            "                    \"cmdLength\": x008,\n" +
                            "                    \"cmdWord\": x000,\n" +
                            "                    \"cmd3060address \": x03D,\n" +
                            "                    \"cmdReadLength \": x002,\n" +
                            "                    \"crc8\": 9A,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A2908003D029A\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getCV4\": {\n" +
                            "                    \"cmdLength\": x008,\n" +
                            "                    \"cmdWord\": x000,\n" +
                            "                    \"cmd3060address \": x03C,\n" +
                            "                    \"cmdReadLength \": x002,\n" +
                            "                    \"crc8\": 5E,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A2908003C025E\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getPackV\": {\n" +
                            "                    \"cmdLength\": x008,\n" +
                            "                    \"cmdWord\": x000,\n" +
                            "                    \"cmd3060address \": x009,\n" +
                            "                    \"cmdReadLength \": x002,\n" +
                            "                    \"crc8\": 8C,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A29080009028C\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getPckC\": {\n" +
                            "                    \"cmdLength\": x008,\n" +
                            "                    \"cmdWord\": x000,\n" +
                            "                    \"cmd3060address \": x00A,\n" +
                            "                    \"cmdReadLength \": x002,\n" +
                            "                    \"crc8\": D9,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A2908000A02D9\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                },\n" +
                            "                \"getTmp\": {\n" +
                            "                    \"cmdLength\": x008,\n" +
                            "                    \"cmdWord\": x000,\n" +
                            "                    \"cmd3060address \": x008,\n" +
                            "                    \"cmdReadLength \": x002,\n" +
                            "                    \"crc8\": 48,\n" +
                            "                    \"cmdString\": \"00000000000000000000000000000000C56A290800080248\",\n" +
                            "                    \"expectReturn\": true\n" +
                            "                }";
                }else if (listData.equalsIgnoreCase("Lumn") || listData.equalsIgnoreCase("catch")){
                    protocol = "\"cmdIntervalms\" : 1000,\n" +
                            "                \"enterCode\": {\n" +
                            "                    \"cmdString\": \">WOTP:decimal string<\",\n" +
                            "                    \"expectReturn\": \"<RE:0000>\"\n" +
                            "                },\n" +
                            "                \"newSession\": {\n" +
                            "                    \"cmdString\": \">HAND<\",\n" +
                            "                    \"expectReturn\": \"<OK>\"\n" +
                            "                },\n" +
                            "                \"getOPID\": {\n" +
                            "                    \"cmdString\": \">OPID<\",\n" +
                            "                    \"expectReturn\": \"<OPID:12345678901233>\"                    \n" +
                            "                },\n" +
                            "                \"readPPID\": {\n" +
                            "                    \"cmdString\": \">PPID<\",\n" +
                            "                    \"expectReturn\": \"<PPID:12345678901233>\" \n" +
                            "                },\n" +
                            "                \"getStatus\": {\n" +
                            "                    \"cmdString\": \">INF<\",\n" +
                            "                    \"expectReturn\": \"<Long Sting>\"\n" +
                            "                },\n" +
                            "                \"getOCS\": {\n" +
                            "                    \"cmdString\": \">OCS<\",\n" +
                            "                    \"expectReturn\": \"<OCS:ENABLED> or <OCS:DISABLED>\"\n" +
                            "                },\n" +
                            "                \"getPS\": {\n" +
                            "                    \"cmdString\": \">PS<\",\n" +
                            "                    \"expectReturn\": \"<PS:FREE> or <PS:PAYF>\"\n" +
                            "                },\n" +
                            "                \"getRPD\": {\n" +
                            "                    \"cmdString\": \">RPD<\",\n" +
                            "                    \"expectReturn\": \"<RPD:0015>\"\n" +
                            "                },\n" +
                            "                \"getTRD\": {\n" +
                            "                    \"cmdString\": \">TDP<\",\n" +
                            "                    \"expectReturn\": \"<RPD:0350>\"\n" +
                            "                },\n" +
                            "                \"getLVC\": {\n" +
                            "                    \"cmdString\": \">LVC<\",\n" +
                            "                    \"expectReturn\": \"<LVC:A1A2A3A4A4A6A7A8>\"\n" +
                            "                },\n" +
                            "                \"endSession\": {\n" +
                            "                    \"cmdString\": \">END<\",\n" +
                            "                    \"expectReturn\": \"<INT>\"\n" +
                            "            }";
                }
                String sendrightsign = text_first_jsonObject.getText().toString() + protocol + text_last_jsonObject.getText().toString();
                sendrightsign = sendrightsign.replaceAll("[\\n\\t ]", "");
                sendrightsign = sendrightsign.replaceAll("\\s+", "");
                listener.OnItemClick(edit_text.getText().toString(),sendrightsign,0+1,"right");
                dismiss();
            }
        });

    }

    public interface OnBack {
        void OnBack(boolean msg);
    }
}
