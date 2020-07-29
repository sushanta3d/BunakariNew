package com.bunakari.sambalpurifashion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bunakari.sambalpurifashion.R;

import java.util.ArrayList;


public class LandsNetworkAdapter extends RecyclerView.Adapter<LandsNetworkAdapter.TransView> {

    private Context context;
    private ArrayList<String> codeArrayList;
    private Integer flagPosition = 0;

    public LandsNetworkAdapter(Context context, ArrayList<String> codeArrayList, Integer flagPosition) {
        this.context = context;
        this.codeArrayList = codeArrayList;
        this.flagPosition = flagPosition;
    }

    @NonNull
    @Override
    public TransView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.landsnw_list_item,parent,false);
        return new TransView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransView holder, int position) {
        if (flagPosition == position) {
            holder.refCodeTextView.setText(codeArrayList.get(position));
            holder.refCodeTextView.setTextColor(context.getResources().getColor(R.color.darkblue));
        }else {
            holder.refCodeTextView.setText(codeArrayList.get(position));
            holder.refCodeTextView.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return codeArrayList.size();
    }

    public class TransView extends RecyclerView.ViewHolder{

        private TextView refCodeTextView;

        public TransView(@NonNull View itemView) {
            super(itemView);
            refCodeTextView = itemView.findViewById(R.id.refCodeTextView);
        }
    }
}
