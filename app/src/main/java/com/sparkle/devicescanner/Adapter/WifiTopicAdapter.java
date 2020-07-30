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
import com.sparkle.devicescanner.Model.AccountDetail;
import com.sparkle.devicescanner.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WifiTopicAdapter extends RecyclerView.Adapter<WifiTopicAdapter.ViewHolder> {

    Context context;
    private List<String> wifiNAMElist;
//    private List<String> wifiBSSIDlist;

    public WifiTopicAdapter(Context context, List<String> wifiNAMElist/*, List<String> wifiBSSIDlist*/) {
        this.context = context;
        this.wifiNAMElist = wifiNAMElist;
//        this.wifiBSSIDlist = wifiBSSIDlist;
    }

    public void notifyList(List<String> getPayAccountLists) {
        this.wifiNAMElist = getPayAccountLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi_topic_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (wifiNAMElist.get(position).equals("")){
            holder.tv_wifi_name.setText("Device");
            holder.tv_wifi_name.setTypeface(holder.tv_wifi_name.getTypeface(), Typeface.BOLD);
//                String firstletter = "D";
//                getColor(firstletter.toUpperCase(), holder.cv_color);


        }else {
            holder.tv_wifi_name.setText(wifiNAMElist.get(position));
            holder.tv_wifi_name.setTypeface(holder.tv_wifi_name.getTypeface(), Typeface.BOLD);
//            String fname = wifiNAMElist.get(position);
//            char firstletter = fname.charAt(0);
//
//            getColor(String.valueOf(firstletter).toUpperCase(), holder.cv_color);
        }
//        if (wifiBSSIDlist.get(position).equals("")){
//            holder.tv_wifi_bssid.setText("dc:71:37:28:d9:82");
//        }else {
//            holder.tv_wifi_bssid.setText(wifiBSSIDlist.get(position));
//        }
    }

    @Override
    public int getItemCount() {
        return wifiNAMElist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_wifi_name, tv_wifi_bssid;
        CircleImageView cv_color;
        RelativeLayout rl_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_wifi_name = (TextView)itemView.findViewById(R.id.tv_wifi_name);
//            tv_wifi_bssid = (TextView)itemView.findViewById(R.id.tv_wifi_bssid);
            cv_color = (CircleImageView) itemView.findViewById(R.id.cv_color);
            rl_image = (RelativeLayout) itemView.findViewById(R.id.rl_image);

//            tv_wifi_name.setText("helllo");
        }
    }

    public void getColor(String alphabet, CircleImageView cv_color) {

        if (alphabet.equals("A")) {
            Glide.with(context).load(R.drawable.ic_a).into(cv_color);
        } else if (alphabet.equals("B")) {
            Glide.with(context).load(R.drawable.ic_b).into(cv_color);
        } else if (alphabet.equals("C")) {
            Glide.with(context).load(R.drawable.ic_c).into(cv_color);
        } else if (alphabet.equals("D")) {
            Glide.with(context).load(R.drawable.ic_d).into(cv_color);
        } else if (alphabet.equals("E")) {
            Glide.with(context).load(R.drawable.ic_e).into(cv_color);
        } else if (alphabet.equals("F")) {
            Glide.with(context).load(R.drawable.ic_f).into(cv_color);
        } else if (alphabet.equals("G")) {
            Glide.with(context).load(R.drawable.ic_g).into(cv_color);
        } else if (alphabet.equals("H")) {
            Glide.with(context).load(R.drawable.ic_h).into(cv_color);
        } else if (alphabet.equals("I")) {
            Glide.with(context).load(R.drawable.ic_i).into(cv_color);
        } else if (alphabet.equals("J")) {
            Glide.with(context).load(R.drawable.ic_j).into(cv_color);
        } else if (alphabet.equals("K")) {
            Glide.with(context).load(R.drawable.ic_k).into(cv_color);
        } else if (alphabet.equals("L")) {
            Glide.with(context).load(R.drawable.ic_l).into(cv_color);
        } else if (alphabet.equals("M")) {
            Glide.with(context).load(R.drawable.ic_m).into(cv_color);
        } else if (alphabet.equals("N")) {
            Glide.with(context).load(R.drawable.ic_n).into(cv_color);
        } else if (alphabet.equals("O")) {
            Glide.with(context).load(R.drawable.ic_o).into(cv_color);
        } else if (alphabet.equals("P")) {
            Glide.with(context).load(R.drawable.ic_p).into(cv_color);
        } else if (alphabet.equals("Q")) {
            Glide.with(context).load(R.drawable.ic_q).into(cv_color);
        } else if (alphabet.equals("R")) {
            Glide.with(context).load(R.drawable.ic_r).into(cv_color);
        } else if (alphabet.equals("S")) {
            Glide.with(context).load(R.drawable.ic_s).into(cv_color);
        } else if (alphabet.equals("T")) {
            Glide.with(context).load(R.drawable.ic_t).into(cv_color);
        } else if (alphabet.equals("U")) {
            Glide.with(context).load(R.drawable.ic_u).into(cv_color);
        } else if (alphabet.equals("V")) {
            Glide.with(context).load(R.drawable.ic_v).into(cv_color);
        } else if (alphabet.equals("W")) {
            Glide.with(context).load(R.drawable.ic_w).into(cv_color);
        } else if (alphabet.equals("X")) {
            Glide.with(context).load(R.drawable.ic_x).into(cv_color);
        } else if (alphabet.equals("Y")) {
            Glide.with(context).load(R.drawable.ic_y).into(cv_color);
        } else if (alphabet.equals("Z")) {
            Glide.with(context).load(R.drawable.ic_z).into(cv_color);
        }

    }
}
