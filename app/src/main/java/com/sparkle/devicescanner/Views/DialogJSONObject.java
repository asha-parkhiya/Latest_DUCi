package com.sparkle.devicescanner.Views;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.MyPref;

public class DialogJSONObject extends Dialog implements View.OnClickListener {
    private MyPref myPref;

    private OnBack onBack;

    private TextView tv_message;
    private Button btn_ok;
    private String message;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                System.out.println("---------------------"+tv_message.getText().toString());
                String finalstr = tv_message.getText().toString();
//                finalstr = finalstr.replaceFirst(".$","");
//                finalstr = finalstr.replaceAll("[\\n\\t ]", "");
                setClipboard(getContext(),finalstr);
                dismiss();
                break;
        }
    }

    public DialogJSONObject(Context context, String message, OnBack onBack) {
        super(context);
        this.message = message;
        this.onBack = onBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_display_json);
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

    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }
}
