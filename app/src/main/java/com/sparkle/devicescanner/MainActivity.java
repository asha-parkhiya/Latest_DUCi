package com.sparkle.devicescanner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.sparkle.devicescanner.Base.BaseActivity;
import com.sparkle.devicescanner.Utils.ConstantMethod;
import com.sparkle.devicescanner.Utils.MyPref;
import com.sparkle.devicescanner.events.MqttStringEvent;
import com.sparkle.devicescanner.services.MqttService;


public class MainActivity extends BaseActivity {

    MqttService mService;
    boolean mBound = false;
    private final String MQTT_DEFAULT_TOPIC_SUBSCRIBE = "ROAM/PPID";
    MyPref myPref;
    StringBuilder stringBuilder;
    String token = null;
    String ppidFromCard;
    Button btn_read;
    private final int MQTT_DEFAULT_QOS = 0;

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
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MqttService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPref = new MyPref(this);
        stringBuilder = new StringBuilder();

        btn_read = findViewById(R.id.btn_read);

        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String read = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C5 6A 29 07 01 02 39";
                byte[] readtmp = ConstantMethod.hexStr2Bytes(read);
                startReadingCard(readtmp);
            }
        });
    }

    public void startReadingCard(byte[] data) {
        sendJsonMessage(data);
    }

    @Override
    public void onUsbSerialMessage(String message) {
        if (stringBuilder == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(message);
        } else
            stringBuilder.append(message);

        System.out.println("len: " + stringBuilder.toString().length());

        if (stringBuilder.toString().length() >= 344) {
            Toast.makeText(this, "USbread data completed", Toast.LENGTH_SHORT).show();
            String msg = stringBuilder.toString();
            ppidFromCard = msg.substring(20, 60);
            System.out.println("ppidFromCard: " + ppidFromCard);
            if (!ppidFromCard.equals("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) {
                ppidFromCard = ppidFromCard.replaceAll("3C", " ").trim();
                String data = ConstantMethod.convertHexToString(ppidFromCard);
                System.out.println(data);

                if (!mService.isConnected()) {
                    if (mBound == false) return;

                    if (mService.connect()) {
                        Toast.makeText(this, "Successfully connected", Toast.LENGTH_SHORT).show();
                    }
//                    mService.publish(MQTT_DEFAULT_TOPIC_SUBSCRIBE, data);
                    mService.subscribe(MQTT_DEFAULT_TOPIC_SUBSCRIBE, 0);

                } else {
//                    mService.publish(MQTT_DEFAULT_TOPIC_SUBSCRIBE, data);
                    mService.subscribe(MQTT_DEFAULT_TOPIC_SUBSCRIBE, 0);
                }
                ppidFromCard = "";
                stringBuilder = null;
            } else {
//                ppidFromCard = "";
//                stringBuilder = null;
//                DialogDisplayMessage codeErrorDialog = new DialogDisplayMessage(HomeActivity.this, "The card is Empty Please write first.", null);
//                codeErrorDialog.show();
            }
        }
    }

    @Override
    protected void onMqttMessage(MqttStringEvent event) {
        Toast.makeText(this, "Topic: " + event.topic + " Message(PPID): " + event.data, Toast.LENGTH_LONG).show();
    }

}
