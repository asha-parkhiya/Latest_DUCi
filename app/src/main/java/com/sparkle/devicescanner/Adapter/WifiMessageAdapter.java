package com.sparkle.devicescanner.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sparkle.devicescanner.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WifiMessageAdapter extends RecyclerView.Adapter<WifiMessageAdapter.ViewHolder> {

    Context context;
    private List<String> wifiNAMElist;

    public WifiMessageAdapter(Context context, List<String> wifiNAMElist) {
        this.context = context;
        this.wifiNAMElist = wifiNAMElist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_topic_message.setText(wifiNAMElist.get(position));

    }

    @Override
    public int getItemCount() {
        return wifiNAMElist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_topic_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_topic_message = (TextView)itemView.findViewById(R.id.tv_topic_message);


        }
    }

}
