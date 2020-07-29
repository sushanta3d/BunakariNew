package com.bunakari.sambalpurifashion.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.HomePageResponse;
import com.bunakari.sambalpurifashion.network.RetroClass;
import com.bunakari.sambalpurifashion.view.ViewAllCategoryActivity;


import java.util.ArrayList;


public class CategotyAdapter extends RecyclerView.Adapter<CategotyAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HomePageResponse> responseList;
    private SubCateRecycleAdapter subCateRecycleAdapter;
    private ItemClickListener itemClickListener;

    public CategotyAdapter(ItemClickListener itemClickListener, Context context, ArrayList<HomePageResponse> responseList) {
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.responseList = responseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.vcate_list_item,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        HomePageResponse pageResponse = responseList.get(position);
        holder.catNameTextView.setText(pageResponse.getCategory());

   holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
     //   holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 1));

        subCateRecycleAdapter = new SubCateRecycleAdapter(context,responseList.get(position).sc);
        holder.recyclerView.setAdapter(subCateRecycleAdapter);

        if (responseList.get(position).getStatus().equalsIgnoreCase("1")){
            Glide.with(context).load(RetroClass.BANNER_CATEGORY_PATH + responseList.get(position).catebanner).into(holder.bannerImageView);
            holder.bannerImageView.setVisibility(View.VISIBLE);
            holder.cardView.setVisibility(View.GONE);
        }else {
            holder.bannerImageView.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.VISIBLE);
        }

        holder.viewMoreTextView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ViewAllCategoryActivity.class);
            intent.putExtra("catname",responseList.get(position).category);
            intent.putExtra("datalist",responseList.get(position).getSc());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       private TextView catNameTextView,viewMoreTextView;
       private ImageView bannerImageView;
       private RecyclerView recyclerView;
       private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            catNameTextView = itemView.findViewById(R.id.cateNameTextView);
            bannerImageView = itemView.findViewById(R.id.bannerImgView);
            viewMoreTextView = itemView.findViewById(R.id.viewMoreTextView);

            cardView = itemView.findViewById(R.id.cardView);
            recyclerView = itemView.findViewById(R.id.hcateRecycleView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null){
                itemClickListener.onItemClick(view,getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }

}
