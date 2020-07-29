package com.bunakari.sambalpurifashion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.SizeResponse;

import java.util.ArrayList;
import java.util.List;


public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<SizeResponse> responseList;
    private ArrayList<Integer> sizePosList;
    private ItemClickListener itemClickListener;

    public SizeAdapter(ItemClickListener itemClickListener, Context context, List<SizeResponse> responseList, ArrayList<Integer> sizePosList) {
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.responseList = responseList;
        this.sizePosList = sizePosList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.size_list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (sizePosList.get(position) == 1) {
            holder.sizeTextView.setText(responseList.get(position).getSize());
            holder.sizeTextView.setTextColor(context.getResources().getColor(R.color.white));
            holder.sizeTextView.setBackground(context.getResources().getDrawable(R.drawable.sizeredroundborder));
        }else {
            holder.sizeTextView.setText(responseList.get(position).getSize());
            holder.sizeTextView.setTextColor(context.getResources().getColor(R.color.appdarkcolor));
            holder.sizeTextView.setBackground(context.getResources().getDrawable(R.drawable.sizeroundborder));
        }
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public void updateSize(ArrayList<Integer> sizeList){
        sizePosList = sizeList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       private TextView sizeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sizeTextView = itemView.findViewById(R.id.sizeTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null){
                itemClickListener.onSizeItemClick(view,getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {
        void onSizeItemClick(View view, int position);
    }

}
