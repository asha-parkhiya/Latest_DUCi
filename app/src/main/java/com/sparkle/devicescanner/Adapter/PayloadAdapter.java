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

public class PayloadAdapter extends RecyclerView.Adapter<PayloadAdapter.ViewHolder> {

    private Context context;
    private List<String> listData;
    private List<String> listCommand;
    private List<String> listData1;
    Onclick onclick;
    private boolean update = true;
    private boolean updateList = true;
    int fPos;
    String fString;

    public PayloadAdapter(Context context, List<String> listCommand, List<String> listData, Onclick onclick) {
        this.context = context;
        this.listData = listData;
        this.listCommand = listCommand;
        this.listData1 = new ArrayList<>();
        listData1.addAll(listCommand);
        this.onclick = onclick;
    }

    public interface Onclick {
        void Onclick(int position, String payload);
    }

    public void setOnButtonclick(Onclick onButtonclick) {
        this.onclick = onButtonclick;
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
                    onclick.Onclick(getAdapterPosition(),"payload");
                }
            });
        }
    }
}
