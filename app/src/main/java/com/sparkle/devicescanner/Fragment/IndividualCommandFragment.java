package com.sparkle.devicescanner.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.events.ProtocolCommandEvent;

import org.greenrobot.eventbus.EventBus;

public class IndividualCommandFragment extends Fragment implements View.OnClickListener{

    private String header,length,command,read_length,crc8;
    private EditText edit_command_name, edit_command_length,edit_command_command, edit_command_read_length, edit_command_crc8;
    private TextView text_command_header;
    private Button btn_add_command;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_individual_command, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edit_command_name = view.findViewById(R.id.edit_command_name);
        edit_command_length = view.findViewById(R.id.edit_command_length);
        edit_command_command = view.findViewById(R.id.edit_command_command);
        edit_command_read_length = view.findViewById(R.id.edit_command_read_length);
        edit_command_crc8 = view.findViewById(R.id.edit_command_crc8);
        text_command_header = view.findViewById(R.id.text_command_header);
        btn_add_command = view.findViewById(R.id.btn_add_command);
        btn_add_command.setOnClickListener(this::onClick);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_command:
                header = text_command_header.getText().toString();
                length = edit_command_length.getText().toString();
                command = edit_command_command.getText().toString();
                read_length = edit_command_read_length.getText().toString();
                crc8 = edit_command_crc8.getText().toString();

                if (!edit_command_name.getText().toString().equals("")){
                    if (!length.equals("")){
                        String finalCommand = header+length;
                        if (!command.equals("")){
                            finalCommand = finalCommand + command+read_length;
                            if (!crc8.equals("")){
                                btn_add_command.requestFocus();
                                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(btn_add_command.getWindowToken(), 0);
                                finalCommand = finalCommand + crc8;
                                finalCommand = finalCommand.replace(" ", "");
                                finalCommand = finalCommand.replaceAll("..", "$0 ").trim();
                                Toast.makeText(getContext(), edit_command_name.getText().toString()+" Command Added Successfully.", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new ProtocolCommandEvent(edit_command_name.getText().toString(),finalCommand));
                            }else {
                                Toast.makeText(getContext(), "Please Enter CRC8", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getContext(), "Please Enter Command", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(getContext(), "Please Enter Length", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Please Enter Command Name", Toast.LENGTH_SHORT).show();
                }


                break;

        }
    }
}
