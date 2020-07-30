package com.sparkle.devicescanner.Adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sparkle.devicescanner.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BluetoothDeviceListAdapter extends RecyclerView.Adapter<BluetoothDeviceListAdapter.ViewHolder> {

    private Context context;
    private List<String> topicList;

    public BluetoothDeviceListAdapter(Context context, List<String> topicList) {
        this.context = context;
        this.topicList = topicList;
    }

    public void notifyList(List<String> topicList){
        this.topicList = topicList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BluetoothDeviceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bluetooth_expand_list, parent, false);
        return new BluetoothDeviceListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothDeviceListAdapter.ViewHolder holder, int position) {
        final String bluetoothDevice = topicList.get(position);
        if (bluetoothDevice == "null"){
            holder.tv_bluetooth_name.setText("Device");
            holder.tv_bluetooth_name1.setText("Device");
        }else {
            holder.tv_bluetooth_name.setText(bluetoothDevice);
            holder.tv_bluetooth_name1.setText(bluetoothDevice);
        }
//        holder.tv_bluetooth_address.setText(bluetoothDevice.getAddress());
        holder.tv_code.setText("Code not available");
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_bluetooth_name,tv_bluetooth_name1,tv_bluetooth_address,tv_code;
        ImageView heart_symbol;
        CircleImageView bluetooth_symbol;
        Button btn_write_code;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_bluetooth_name = (TextView)itemView.findViewById(R.id.tv_bluetooth_name);
            tv_bluetooth_name1 = (TextView)itemView.findViewById(R.id.tv_bluetooth_name1);
//            tv_bluetooth_address = (TextView)itemView.findViewById(R.id.tv_bluetooth_address);
            heart_symbol = (ImageView)itemView.findViewById(R.id.heart_symbol);
            tv_code = (TextView)itemView.findViewById(R.id.tv_code);
            btn_write_code = (Button) itemView.findViewById(R.id.btn_write_code);
            btn_write_code.setEnabled(false);
            btn_write_code.setVisibility(View.GONE);
        }
    }
}
