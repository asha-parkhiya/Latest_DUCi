package com.sparkle.devicescanner.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;
import com.sparkle.devicescanner.Activity.ActiveDeviceActivity;
import com.sparkle.devicescanner.Activity.HomeActivity;
import com.sparkle.devicescanner.Model.AccountDetail;
import com.sparkle.devicescanner.Model.PPID_TYPE.DeviceType;
import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Utils.Constant;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class DeviceTypeAdapter extends Adapter<DeviceTypeAdapter.ViewHolder> {

    Context context;
    List<DeviceType> accList;
    OnButtonclick onButtonclick;

    int i = 0;
    int color = 0;

    public DeviceTypeAdapter(Context context, List<DeviceType> accList, OnButtonclick onButtonclick) {
        this.context = context;
        this.accList = accList;
        this.onButtonclick = onButtonclick;
    }



    public interface OnButtonclick {
        void OnButtonclick(int position);
    }

    public void setOnButtonclick(OnButtonclick onButtonclick) {
        this.onButtonclick = onButtonclick;
    }

    public void notifyList(List<DeviceType> getPayAccountLists) {
        this.accList = getPayAccountLists;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public DeviceTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pay_account_expand_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull DeviceTypeAdapter.ViewHolder holder, int position) {
        DeviceType deviceType = accList.get(position);

        if (deviceType != null){
            holder.tv_ppid.setText(deviceType.getPPID());
            holder.tv_device_type.setText(deviceType.getDeviceType());
//            holder.tv_code.setText(deviceType.getcode());
        }


        if (deviceType.getDeviceType() != null) {
            holder.tv_fname.setText(deviceType.getDeviceType());
//            holder.tv_lname.setText("");
            holder.tv_fname.setTypeface(holder.tv_fname.getTypeface(), Typeface.BOLD);
            String fname = deviceType.getDeviceType().toString();
            char firstletter = fname.charAt(0);

//            getColor(String.valueOf(firstletter).toUpperCase(), holder.cv_color);
        }
//        if (deviceType.getDeviceType() != null){
//            holder.tv_lname.setText(deviceType.getDeviceType());
//            holder.tv_lname.setTypeface(holder.tv_lname.getTypeface(), Typeface.BOLD);
//        }

        holder.btn_active_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(context, ActiveDeviceActivity.class);
                intent1.putExtra(Constant.I_PPID,deviceType.getPPID());
                intent1.putExtra(Constant.I_DEVICE_TYPE,deviceType.getDeviceType());
                context.startActivity(intent1);
            }
        });


    }





    @Override
    public int getItemCount() {
        return accList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_fname, tv_lname, tv_ppid, tv_letter,tv_device_type,tv_code;
        CircleImageView cv_color;
        RelativeLayout rl_image;
        AppCompatButton btn_active_device;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_fname = (TextView) itemView.findViewById(R.id.tv_fname);
            tv_ppid = (TextView) itemView.findViewById(R.id.tv_ppid);
            tv_device_type = (TextView) itemView.findViewById(R.id.tv_device_type);
//            tv_code = (TextView) itemView.findViewById(R.id.tv_code);
            cv_color = itemView.findViewById(R.id.cv_color);
//            rl_image = itemView.findViewById(R.id.rl_image);
//            rl_image.setVisibility(View.VISIBLE);
            btn_active_device = itemView.findViewById(R.id.btn_active_device);
//


//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onMclick.onMclick(getAdapterPosition());
//                }
//            });
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
