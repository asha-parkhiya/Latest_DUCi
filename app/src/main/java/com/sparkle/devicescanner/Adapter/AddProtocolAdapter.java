package com.sparkle.devicescanner.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.sparkle.devicescanner.Activity.ProtocolTableActivity;
import com.sparkle.devicescanner.R;
import com.sparkle.devicescanner.Views.DialogDisplayMessage;
import com.sparkle.devicescanner.Views.DialogProtocol;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AddProtocolAdapter extends RecyclerView.Adapter<AddProtocolAdapter.ViewHolder> {

    private Context context;
    private List<String> listData;
    private List<String> listCommand;
    private boolean isShowHide;

    private onItemClickListener listener;
    public AddProtocolAdapter(Context context, List<String> listData, List<String> listCommand, onItemClickListener listener) {
        this.context = context;
        this.listData = listData;
        this.listCommand = listCommand;
        this.listener = listener;
    }

    public interface onItemClickListener{
        void OnItemClick(String protocoldis,String jsonObject,int position,String right_wrong);
        void OnItem(String protocoldis,String name,String jsonObject,int position);
    }
    public void notifyList(List<String> topicList,List<String> listCommand){
        this.listData = topicList;
        this.listCommand = listCommand;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_protocol_expand_list, parent, false);
        return new ViewHolder(view);
    }

    public void hideRecycler(){
        isShowHide = false;
        notifyDataSetChanged();
    }
    public void showRecycler(){
        isShowHide = true;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_data.setText(listData.get(position));
        String protocolName = "{\"protocol\":\""+listData.get(position)+"\"}";


        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(protocolName,"",position+1,"minus");
            }
        });

        holder.headerIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("------adapter--------"+protocolName);
                listener.OnItem(protocolName,listData.get(position),listCommand.get(position),position);
            }
        });

//        if(isShowHide) {
//            holder.rl_right_wrong.setVisibility(View.VISIBLE);
//        } else {
//            holder.rl_right_wrong.setVisibility(View.GONE);
//        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_data;

        ImageView btn_minus;

        ImageView headerIndicator;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_data = itemView.findViewById(R.id.tv_data);
            btn_minus = itemView.findViewById(R.id.btn_minus);
            headerIndicator = itemView.findViewById(R.id.headerIndicator);





//            rl_right_wrong.setVisibility(View.GONE);
        }
    }

    public static String formatString(String text){

        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                case '[':
                    json.append( indentString + letter + "\n");
                    indentString = indentString + "\t";
                    json.append(indentString);
                    break;
                case '}':
                case ']':
                    indentString = indentString.replaceFirst("\t", "");
                    json.append("\n" + indentString + letter);
                    break;
                case ',':
                    json.append(letter + "\n" + indentString);
                    break;

                default:
                    json.append(letter);
                    break;
            }
        }

        return json.toString();
    }
}
