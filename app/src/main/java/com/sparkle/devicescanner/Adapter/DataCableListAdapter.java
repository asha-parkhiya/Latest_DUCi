package com.sparkle.devicescanner.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.sparkle.devicescanner.Model.AccountDetail;
import com.sparkle.devicescanner.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DataCableListAdapter extends RecyclerView.Adapter<DataCableListAdapter.ViewHolder> {

    private Context context;
    private List<AccountDetail> topicList;
    OnMclick onMclick;

    public DataCableListAdapter(Context context, List<AccountDetail> topicList,OnMclick onMclick) {
        this.context = context;
        this.topicList = topicList;
        this.onMclick = onMclick;
    }

    public void notifyList(List<AccountDetail> topicList){
        this.topicList = topicList;
        notifyDataSetChanged();
    }
    public interface OnMclick {
        void onMclick(int position);
    }

    public void setOnMclick(OnMclick onMclick) {
        this.onMclick = onMclick;
    }

    @NonNull
    @Override
    public DataCableListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usb_expand_list, parent, false);
        return new DataCableListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataCableListAdapter.ViewHolder holder, int position) {
        final AccountDetail accountDetail = topicList.get(position);
        holder.tv_acc_name.setText(accountDetail.getFirstname()+" "+accountDetail.getLastname());
        holder.tv_first_name.setText(accountDetail.getFirstname());
        holder.tv_last_name.setText(accountDetail.getLastname());
        holder.tv_id.setText(accountDetail.getId());
        holder.tv_code.setText(accountDetail.getCode());
        if (accountDetail.getCode()!= null){
            holder.btn_write_code.setEnabled(true);
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.btn_backgroundcolor);
            holder.btn_write_code.setBackground(unwrappedDrawable);
        }
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_acc_name,tv_first_name,tv_last_name,tv_id,tv_code;
        ImageView heart_symbol;
        CircleImageView bluetooth_symbol;
        Button btn_write_code;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_acc_name = (TextView)itemView.findViewById(R.id.tv_acc_name);
            tv_first_name = (TextView)itemView.findViewById(R.id.tv_first_name);
            tv_last_name = (TextView)itemView.findViewById(R.id.tv_last_name);
            tv_id = (TextView)itemView.findViewById(R.id.tv_id);
            heart_symbol = (ImageView)itemView.findViewById(R.id.heart_symbol);
            tv_code = (TextView)itemView.findViewById(R.id.tv_code);
            btn_write_code = (Button) itemView.findViewById(R.id.btn_write_code);
            btn_write_code.setEnabled(false);

            btn_write_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMclick.onMclick(getAdapterPosition());
                }
            });
        }
    }
}
