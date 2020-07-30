package com.sparkle.devicescanner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sparkle.devicescanner.R;

import java.util.List;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private Context context;
    private List<String> topicList;

    public TopicListAdapter(Context context, List<String> topicList) {
        this.context = context;
        this.topicList = topicList;
    }

    public void notifyList(List<String> topicList){
        this.topicList = topicList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_topic_message.setText(topicList.get(position));
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_topic_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_topic_message = (TextView)itemView.findViewById(R.id.tv_topic_message);
        }
    }
}
