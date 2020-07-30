package com.sparkle.devicescanner.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sparkle.devicescanner.R;

import java.util.ArrayList;
import java.util.List;

public class ActiveDeviceAdapter extends RecyclerView.Adapter<ActiveDeviceAdapter.ViewHolder> {

    private Context context;
    private List<String> listData;
    private List<String> listCommand;
    private List<String> listData1;
    OnButtonclick onButtonclick;
    private boolean update = true;
    private boolean updateList = true;
    int fPos;
    String fString;

    public ActiveDeviceAdapter(Context context, List<String> listCommand,List<String> listData, OnButtonclick onButtonclick) {
        this.context = context;
        this.listData = listData;
        this.listCommand = listCommand;
        this.listData1 = new ArrayList<>();
        listData1.addAll(listCommand);
        this.onButtonclick = onButtonclick;
    }

    public interface OnButtonclick {
        void OnButtonclick(int position);
    }

    public void setOnButtonclick(OnButtonclick onButtonclick) {
        this.onButtonclick = onButtonclick;
    }

    public void notifyList(List<String> topicList,List<String> listData){
        this.listCommand = topicList;
        this.listData = listData;
        notifyDataSetChanged();
    }
    public void notifyList1(List<String> topicList){
        updateList = false;
        this.listData1 = topicList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_active_device, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.btn_fire_command.setText(listData.get(position));
//        if (listCommand.get(position).contains("HAND")){
//            holder.btn_fire_command.setText("HANDSHAKE");
//        }else if(listCommand.get(position).contains("PPID")){
//            holder.btn_fire_command.setText("Read PPID");
//        }else if (listCommand.get(position).contains("OPID")){
//            holder.btn_fire_command.setText("Read OPID");
//        }else if (listCommand.get(position).contains("OCS")){
//            holder.btn_fire_command.setText("Get Outpot Control");
//        }else if (listCommand.get(position).contains("PS")){
//            holder.btn_fire_command.setText("Get PAYG Status");
//        }else if (listCommand.get(position).contains("RPD")){
//            holder.btn_fire_command.setText("Remaining Days");
//        }else if (listCommand.get(position).contains("TDP")){
//            holder.btn_fire_command.setText("Total Days");
//        }else if (listCommand.get(position).contains("LVC")){
//            holder.btn_fire_command.setText("Last Valid Password");
//        }else if (listCommand.get(position).contains("END")){
//            holder.btn_fire_command.setText("Exit Communication");
//        }else if (listCommand.get(position).contains("INF")){
//            holder.btn_fire_command.setText("All Information");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A290604AA")){
//            holder.btn_fire_command.setText("HANDSHAKE");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A2906AF25")){
//            holder.btn_fire_command.setText("Read DevID");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A29070814CB")){
//            holder.btn_fire_command.setText("Read PPID");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A2907010E9A")){
//            holder.btn_fire_command.setText("Read OPID");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A29061108")){
//            holder.btn_fire_command.setText("Read HashTOP");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A290605F4")){
//            holder.btn_fire_command.setText("Read Remaining_PAYG_Days");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A296616")){
//            holder.btn_fire_command.setText("Read Days have been running");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A29060748")){
//            holder.btn_fire_command.setText("Read PAYG_Days");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A29060957")){
//            holder.btn_fire_command.setText("Read PAYG_State");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A29060AB5")){
//            holder.btn_fire_command.setText("Read Output_Control_State");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A29060BEB")){
//            holder.btn_fire_command.setText("Read System_Status_Code");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A29060C68")){
//            holder.btn_fire_command.setText("Read Relative_SOC");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A29060D36")){
//            holder.btn_fire_command.setText("Read Remaining_Capacity");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A29060ED4")){
//            holder.btn_fire_command.setText("Read Full_Charge_Capacity");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A29060F8A")){
//            holder.btn_fire_command.setText("Read Accu_Energy_Output");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A29061894")){
//            holder.btn_fire_command.setText("Read Solar Geberatuibt & Charge_Power");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A290617D5")){
//            holder.btn_fire_command.setText("Read ACC_Engery_Output & Load Power");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A29061056")){
//            holder.btn_fire_command.setText("Read Accu_Cycles");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A290613B4")){
//            holder.btn_fire_command.setText("Read Run_Days_Backup");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A29061437")){
//            holder.btn_fire_command.setText("Read PAYG_REV");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A2908003F020B")){
//            holder.btn_fire_command.setText("Read CellVoltage1");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A2908003E02CF")){
//            holder.btn_fire_command.setText("Read CellVoltage2");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A2908003D029A")){
//            holder.btn_fire_command.setText("Read CellVoltage3");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A2908003C025E")){
//            holder.btn_fire_command.setText("Read CellVoltage4");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A29080009028C")){
//            holder.btn_fire_command.setText("Read BatteryVoltage");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A2908000A02D9")){
//            holder.btn_fire_command.setText("Read BatteryCurrent");
//        }else if (listCommand.get(position).contains("00000000000000000000000000000000C56A290800080248")){
//            holder.btn_fire_command.setText("Read BatteryTemperature");
//        }
        if (!update){
            if (position == fPos){
                holder.tv_command_response.setText(fString);
            }
        }
        if (!updateList){
            holder.tv_command_response.setText(listData1.get(position));
        }
    }

    public void changeValue(int pos, String string){
        fPos = pos;
        fString = string;
        update = false;
        notifyItemChanged(pos);
    }
    @Override
    public int getItemCount() {
        return listCommand.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_command_response;
        Button btn_fire_command;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_command_response = (TextView)itemView.findViewById(R.id.tv_command_response);
            btn_fire_command = (Button) itemView.findViewById(R.id.btn_fire_command);

            btn_fire_command.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onButtonclick.OnButtonclick(getAdapterPosition());
                }
            });

        }
    }
}
