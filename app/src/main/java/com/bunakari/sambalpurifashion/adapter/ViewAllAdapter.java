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


public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.AllCategoryView> {

    private Context context;
    private List<SubCategoryResponse> categoryList;

    public ViewAllAdapter(Context context, List<SubCategoryResponse> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public AllCategoryView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cate_grid_item,parent,false);
        return new AllCategoryView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllCategoryView holder, int position) {
        SubCategoryResponse categoryResponse = categoryList.get(position);
        holder.cateTextView.setText(categoryResponse.subcategory);
        BasicFunction.showImage(RetroClass.CATEGORY_PATH + categoryResponse.img,context,holder.cateImageView,holder.progressBar);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class AllCategoryView extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView cateImageView;
        private TextView cateTextView;
        private ProgressBar progressBar;

        public AllCategoryView(@NonNull View itemView) {
            super(itemView);

            cateImageView = itemView.findViewById(R.id.cateImgView);
            cateTextView = itemView.findViewById(R.id.cateNameTextView);
            progressBar = itemView.findViewById(R.id.progressBar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra("catid",categoryList.get(getAdapterPosition()).getSid());
            intent.putExtra("catname",categoryList.get(getAdapterPosition()).getSubcategory());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}
