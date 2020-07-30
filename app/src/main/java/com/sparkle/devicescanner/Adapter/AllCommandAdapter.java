package com.sparkle.devicescanner.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.Tools;
import com.sparkle.devicescanner.Utils.ViewAnimation;

import java.util.List;

import static com.sparkle.devicescanner.Utils.Tools.toggleArrow;

public class AllCommandAdapter extends RecyclerView.Adapter<AllCommandAdapter.ViewHolder> {

    private Context context;
    private List<String> listData;
    private List<String> listCommand;

    public AllCommandAdapter(Context context, List<String> listData, List<String> listCommand) {
        this.context = context;
        this.listData = listData;
        this.listCommand = listCommand;
    }

    public void notifyList(List<String> topicList,List<String> listCommand){
        this.listData = topicList;
        this.listCommand = listCommand;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_command_expand_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_data.setText(listData.get(position));
        holder.tv_command.setText("["+listCommand.get(position)+"]");
//        if (listData.get(position).equals("LUMN Handshake")){
//            holder.tv_command.setText("[>HAND<]");
//        }else if (listData.get(position).equals("Read PPID")){
//            holder.tv_command.setText("[>PPID<]");
//        }else if (listData.get(position).contains("Read OEM ID")){
//            holder.tv_command.setText("[>OPID<]");
//        }else if (listData.get(position).contains("Get O/p Control")){
//            holder.tv_command.setText("[>OCS<]");
//        }else if (listData.get(position).contains("Get PAYG Status")){
//            holder.tv_command.setText("[>PS<]");
//        }else if (listData.get(position).contains("Get Remaining Days")){
//            holder.tv_command.setText("[>RPD<]");
//        }else if (listData.get(position).contains("Get Total Number of Days")){
//            holder.tv_command.setText("[>TDP<]");
//        }else if (listData.get(position).contains("Get Last Valid password")){
//            holder.tv_command.setText("[>LVS<]");
//        }else if (listData.get(position).contains("For Exit State")){
//            holder.tv_command.setText("[>END<]");
//        }else if (listData.get(position).contains("ALL Information")){
//            holder.tv_command.setText("[>INF<]");
//        }else if (listData.get(position).contains("DAVID")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 AF 25]");
//        }else if (listData.get(position).contains("Handshake")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 04 AA]");
//        }else if (listData.get(position).contains("PPID")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 07 08 14 CB]");
//        }else if (listData.get(position).contains("OPID")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 07 01 0E 9A]");
//        }else if (listData.get(position).contains("HashTop")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 11 08]");
//        }else if (listData.get(position).contains("Remaining_PAYG_Days")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 05 FA]");
//        }else if (listData.get(position).contains("Days_have_been_running")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 6 6 16]");
//        }else if (listData.get(position).contains("PAYG_Days")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 07 48]");
//        }else if (listData.get(position).contains("PAYG_State")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 09 57]");
//        }else if (listData.get(position).contains("Output_Control_State")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 0A B5]");
//        }else if (listData.get(position).contains("System_Status_Code")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 0B EB]");
//        }else if (listData.get(position).contains("Relative_SOC")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 0C 68]");
//        }else if (listData.get(position).contains("Remaining_Capacity")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 0D 36]");
//        }else if (listData.get(position).contains("Full_Charge_Capacity")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 0E D4]");
//        }else if (listData.get(position).contains("Accu_Energy_Output")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 0F 8A]");
//        }else if (listData.get(position).contains("Solar_Geberatuibt & Charge_Power")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 18 94]");
//        }else if (listData.get(position).contains("ACC_Engery_Output & Load_Power")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 17 D5]");
//        }else if (listData.get(position).contains("Accu_Cycles")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 10 56]");
//        }else if (listData.get(position).contains("Run_Days_Backup")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 13 B4]");
//        }else if (listData.get(position).contains("PAYG_REV")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 06 14 37]");
//        } else if (listData.get(position).contains("CellVoltage1")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 08 00 3F 02 0B]");
//        }else if (listData.get(position).contains("CellVoltage2")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 08 00 3E 02 CF]");
//        }else if (listData.get(position).contains("CellVoltage3")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 08 00 3D 02 9A]");
//        }else if (listData.get(position).contains("CellVoltage4")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 08 00 3C 02 5E]");
//        }else if (listData.get(position).contains("BatteryVoltage")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 08 00 09 02 8C]");
//        }else if (listData.get(position).contains("BatteryCurrent")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 08 00 0A 02 D9]");
//        }else if (listData.get(position).contains("BatteryTemperature")){
//            holder.tv_command.setText("[00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 + C5 6A 29 08 00 08 02 48]");
//        }
    }

    private void toggleSectionText(View view) {

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_data;
        TextView tv_command;
        AppCompatImageView headerIndicator;
        LinearLayout container;
        NestedScrollView nested_scroll_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_data = (TextView)itemView.findViewById(R.id.tv_data);
            tv_command = (TextView)itemView.findViewById(R.id.tv_command);
//            container = itemView.findViewById(R.id.container);
//            headerIndicator = itemView.findViewById(R.id.headerIndicator);
//            nested_scroll_view = itemView.findViewById(R.id.nested_scroll_view);
//            headerIndicator.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    boolean show = toggleArrow(v);
//                    if (show) {
//                        ViewAnimation.expand(container, new ViewAnimation.AnimListener() {
//                            @Override
//                            public void onFinish() {
//                                Tools.nestedScrollTo(nested_scroll_view, container);
//                            }
//                        });
//                    } else {
//                        ViewAnimation.collapse(container);
//                    }
//                }
//            });
        }
    }
}
