package com.sparkle.devicescanner.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sparkle.devicescanner.Adapter.DeviceTypeAdapter;
import com.sparkle.devicescanner.R;


public class CodeErrorDialog extends Dialog implements View.OnClickListener {
    private MyPref myPref;

    private OnLogout onLogout;

    private String message;
    private TextView tv_message;
    private Button btn_ok, btn_clear;
    OnButtonclick onButtonclick;

    public interface OnButtonclick {
        void OnButtonclick();
    }

    public void setOnButtonclick(OnButtonclick onButtonclick) {
        this.onButtonclick = onButtonclick;
    }
    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.btn_ok) {
//            if (message.equals("Your token is expired please login again.")) {
//                if (onLogout != null)
//                    onLogout.OnLogout(false);
//                else
//                    dismiss();
//            } else if (message.equals("Your card is not belongs from this account please write data first into card.")) {
//                if (onLogout != null)
//                    onLogout.OnLogout(true);
//                dismiss();
//            } else
//            tv_message.setText("");
//            dismiss();
//        }
        switch (v.getId()){
            case R.id.btn_ok:
                dismiss();
                break;

            case R.id.btn_clear:
                tv_message.setText("");
                onButtonclick.OnButtonclick();
                break;
        }
    }

    public CodeErrorDialog(Context context, String message, OnLogout onLogout, OnButtonclick onButtonclick) {
        super(context);
        this.message = message;
        this.onLogout = onLogout;
        this.onButtonclick = onButtonclick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_code_error);
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
        btn_clear = findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);
    }

    public interface OnLogout {
        void OnLogout(boolean msg);
    }
}
