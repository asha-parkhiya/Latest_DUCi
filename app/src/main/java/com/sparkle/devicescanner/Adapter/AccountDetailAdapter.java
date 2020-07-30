package com.sparkle.devicescanner.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;
import com.sparkle.devicescanner.Fragment.SubscribeTopicFragment;
import com.sparkle.devicescanner.Model.AccountDetail;
import com.sparkle.devicescanner.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountDetailAdapter extends Adapter<AccountDetailAdapter.ViewHolder> {

    Context context;
    List<AccountDetail> accList;
    OnMclick onMclick;

    int i = 0;
    int color = 0;

    public AccountDetailAdapter(Context context, List<AccountDetail> accList, OnMclick onMclick) {
        this.context = context;
        this.accList = accList;
        this.onMclick = onMclick;
    }



    public interface OnMclick {
        void onMclick(int position);
    }

    public void setOnMclick(OnMclick onMclick) {
        this.onMclick = onMclick;
    }

    public void notifyList(List<AccountDetail> getPayAccountLists) {
        this.accList = getPayAccountLists;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public AccountDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payaccount, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull AccountDetailAdapter.ViewHolder holder, int position) {
        AccountDetail accountDetail = accList.get(position);

        if (accountDetail != null){
            holder.tv_ppid.setText(accountDetail.getId());
        }


        if (accountDetail.getFirstname() != null) {
            holder.tv_fname.setText(accountDetail.getFirstname());
            holder.tv_fname.setTypeface(holder.tv_fname.getTypeface(), Typeface.BOLD);
            String fname = accountDetail.getFirstname().toString();
            char firstletter = fname.charAt(0);

            getColor(String.valueOf(firstletter).toUpperCase(), holder.cv_color);
        }
        if (accountDetail.getLastname() != null){
            holder.tv_lname.setText(accountDetail.getFirstname());
            holder.tv_lname.setTypeface(holder.tv_lname.getTypeface(), Typeface.BOLD);
        }


    }





    @Override
    public int getItemCount() {
        return accList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_fname, tv_lname, tv_ppid, tv_letter;
        CircleImageView cv_color;
        RelativeLayout rl_image;
        Button btn_write_code;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_fname = (TextView) itemView.findViewById(R.id.tv_fname);
            tv_lname = (TextView) itemView.findViewById(R.id.tv_lname);
            tv_ppid = (TextView) itemView.findViewById(R.id.tv_ppid);
//            tv_letter = (TextView) itemView.findViewById(R.id.tv_letter);
            cv_color = itemView.findViewById(R.id.cv_color);
            rl_image = itemView.findViewById(R.id.rl_image);
            rl_image.setVisibility(View.VISIBLE);
            btn_write_code = itemView.findViewById(R.id.btn_write_code);

            btn_write_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMclick.onMclick(getAdapterPosition());
                }
            });

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
