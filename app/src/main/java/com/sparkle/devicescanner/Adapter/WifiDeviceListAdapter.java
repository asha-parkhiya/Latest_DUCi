package com.sparkle.devicescanner.Adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sparkle.devicescanner.R;

import java.util.List;

public class WifiDeviceListAdapter extends RecyclerView.Adapter<WifiDeviceListAdapter.ViewHolder> {

    Context context;
    List<ScanResult> scanResults;

    public WifiDeviceListAdapter(Context context, List<ScanResult> scanResults) {
        this.context = context;
        this.scanResults = scanResults;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi_expand_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScanResult scanResult = scanResults.get(position);
        if (scanResult.SSID.equals("")){
            holder.tv_wifi_ssid.setText("Device");
            holder.tv_wifi_ssid1.setText("Device");
        }else {
            holder.tv_wifi_ssid.setText(scanResult.SSID);
            holder.tv_wifi_ssid1.setText(scanResult.SSID);
        }
//        holder.tv_wifi_bssid.setText(scanResult.BSSID);
        holder.tv_capability.setText(scanResult.capabilities);
        holder.tv_code.setText("Code not available");

    }

    @Override
    public int getItemCount() {
        return scanResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_wifi_ssid,tv_wifi_ssid1,tv_wifi_bssid,tv_capability,tv_code;
        Button btn_write_code;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_wifi_ssid = (TextView)itemView.findViewById(R.id.tv_wifi_ssid);
            tv_wifi_ssid1 = (TextView)itemView.findViewById(R.id.tv_wifi_ssid1);
//            tv_wifi_bssid = (TextView)itemView.findViewById(R.id.tv_wifi_bssid);
            tv_capability = (TextView)itemView.findViewById(R.id.tv_capability);
            tv_code = (TextView)itemView.findViewById(R.id.tv_code);
            btn_write_code = (Button) itemView.findViewById(R.id.btn_write_code);
            btn_write_code.setEnabled(false);
            btn_write_code.setVisibility(View.GONE);
        }
    }
}
