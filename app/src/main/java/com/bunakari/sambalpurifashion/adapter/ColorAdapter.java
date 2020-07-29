package com.bunakari.sambalpurifashion.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.ColorResponse;

import java.util.ArrayList;
import java.util.List;


public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<ColorResponse> responseList;
    private ArrayList<Integer> colorPosList;
    private ItemClickListener itemClickListener;

    public ColorAdapter(ItemClickListener itemClickListener, Context context, List<ColorResponse> responseList, ArrayList<Integer> colorPosList) {
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.responseList = responseList;
        this.colorPosList = colorPosList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.color_list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (colorPosList.get(position) == 1) {
            holder.colorTextView.setBackgroundColor(Color.parseColor(responseList.get(position).getColorcode()));
            holder.checkImageView.setVisibility(View.VISIBLE);
        }else {
            holder.colorTextView.setBackgroundColor(Color.parseColor(responseList.get(position).getColorcode()));
            holder.checkImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public void updateColor(ArrayList<Integer> colorList){
        colorPosList = colorList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       private TextView colorTextView;
       private ImageView checkImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            colorTextView = itemView.findViewById(R.id.colorTextView);
            checkImageView = itemView.findViewById(R.id.checkImgView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null){
                itemClickListener.onColorItemClick(view,getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {
        void onColorItemClick(View view, int position);
    }

}
