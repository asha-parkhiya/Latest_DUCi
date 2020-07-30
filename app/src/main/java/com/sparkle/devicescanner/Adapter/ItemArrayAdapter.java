package com.sparkle.devicescanner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.sparkle.devicescanner.R;

import java.util.ArrayList;
import java.util.List;

public class ItemArrayAdapter extends ArrayAdapter<String[]> {

    private List<String[]> scoreList = new ArrayList<>();
    private List<String[]> allscoreList = new ArrayList<>();

    static class ItemViewHolder{
        TextView device;
        TextView ppid;
        TextView code;
    }
    public ItemArrayAdapter(@NonNull Context context, int resource, onItemClickListener listener) {
        super(context, resource);
        allscoreList = new ArrayList<>();
        allscoreList.addAll(scoreList);
        this.listener = listener;
    }

    public interface onItemClickListener{
        void changevalue(String string,String string1,String string2, int position);
    }
    private onItemClickListener listener;

    public void add(String[] object) {
        allscoreList.add(object);
        super.add(object);
    }

    public void notifyList(List<String[]> getPayAccountLists) {
        this.allscoreList = getPayAccountLists;
        notifyDataSetChanged();
    }

    public void filter(String charText) {

        int position = 0;
        scoreList = new ArrayList<>();
        if (charText.length() == 0) {
            allscoreList.addAll(scoreList);
        } else {
            for (String[] wp : allscoreList) {
                position = position + 1;
                if (wp[1].equals(charText)) {
                    scoreList.add(wp);
                    listener.changevalue(wp[0],wp[1],wp[2],position);
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return this.allscoreList.size();
    }

    @Nullable
    @Override
    public String[] getItem(int position) {
        return this.allscoreList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ItemViewHolder viewHolder;
        if (row == null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_list_item,parent,false);
            viewHolder = new ItemViewHolder();
            viewHolder.device = (TextView) row.findViewById(R.id.device);
            viewHolder.ppid = (TextView) row.findViewById(R.id.ppid);
            viewHolder.code = (TextView) row.findViewById(R.id.code);
            row.setTag(viewHolder);
        }else {
            viewHolder = (ItemViewHolder) row.getTag();
        }

        String[] start = getItem(position);
        viewHolder.device.setText(start[0]);
        viewHolder.ppid.setText(start[1]);
        viewHolder.code.setText(start[2]);

        return row;
    }

}
