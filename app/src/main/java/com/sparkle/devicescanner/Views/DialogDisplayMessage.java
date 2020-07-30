package com.sparkle.devicescanner.Views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sparkle.devicescanner.Activity.EditProtocolActivity;
import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.MyPref;

public class DialogDisplayMessage extends Dialog implements View.OnClickListener {
    private MyPref myPref;

    private OnBack onBack;

    private TextView tv_message;
    private Button btn_ok;
    private String message;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (tv_message.getText().toString().equals("Please First Add Protocol")){
                    dismiss();
                    onBack.OnBack(true);
                }else{
                    dismiss();
                }
                break;
        }
    }

    public DialogDisplayMessage(Context context, String message, OnBack onBack) {
        super(context);
        this.message = message;
        this.onBack = onBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_display_message);
        Window w = getWindow();
        setCancelable(false);
        myPref = new MyPref(getContext());
        w.getAttributes().windowAnimations = R.style.DialogAnimation;
        w.setBackgroundDrawableResource(android.R.color.transparent);
        tv_message = findViewById(R.id.tv_message);

        if (message != null) {
            tv_message.setText(message);
        }

        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
    }

    public interface OnBack {
        void OnBack(boolean msg);
    }
}
