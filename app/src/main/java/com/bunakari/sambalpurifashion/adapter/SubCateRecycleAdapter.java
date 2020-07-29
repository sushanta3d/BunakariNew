package com.bunakari.sambalpurifashion.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.SubCategoryResponse;
import com.bunakari.sambalpurifashion.network.RetroClass;
import com.bunakari.sambalpurifashion.view.ProductActivity;

import java.util.List;


public class SubCateRecycleAdapter extends RecyclerView.Adapter<SubCateRecycleAdapter.SubViewHolder> {

    private Context context;
    private List<SubCategoryResponse> responseList;

    public SubCateRecycleAdapter(Context context, List<SubCategoryResponse> responseList) {
        this.context = context;
        this.responseList = responseList;
    }

    @NonNull
    @Override
    public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hcate_list_item,parent,false);
        return new SubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubViewHolder holder, int position) {
        holder.subCateTextView.setText(responseList.get(position).subcategory);
        BasicFunction.showImage(RetroClass.CATEGORY_PATH + responseList.get(position).img,context,holder.subCateImageView,holder.progressBar);
    }

    @Override
    public int getItemCount() {
        if (responseList.size() > 10){
            return 10;
        }else {
            return responseList.size();
        }
    }

    public class SubViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView subCateTextView;
        private ImageView subCateImageView;
        private ProgressBar progressBar;

        public SubViewHolder(@NonNull View itemView) {
            super(itemView);

            subCateImageView = itemView.findViewById(R.id.cateImgView);
            subCateTextView = itemView.findViewById(R.id.cateNameTextView);
            progressBar = itemView.findViewById(R.id.progressBar);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra("catid",responseList.get(getAdapterPosition()).getSid());
            intent.putExtra("catname",responseList.get(getAdapterPosition()).getSubcategory());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
