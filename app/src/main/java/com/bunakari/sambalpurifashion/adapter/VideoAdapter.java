package com.bunakari.sambalpurifashion.adapter;

import android.content.Context;
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
import com.bunakari.sambalpurifashion.model.VideoResponse;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ProductViewHolder> {

    private Context context;
    private ArrayList<VideoResponse> videoList;
    private ArrayList<Integer> wishList;
    private ArrayList<Integer> likeList;
    private ItemClickListener itemClickListener;

    public VideoAdapter(ItemClickListener itemClickListener, Context context, ArrayList<VideoResponse> videoList) {
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.videoList = videoList;
        this.wishList = wishList;
        this.likeList = likeList;

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vid_grid_item,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        BasicFunction.showImage(videoList.get(position).getImg(),context,holder.proImageView,holder.progressBar);
        holder.proTextView.setText(videoList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void updateWishlist(ArrayList<Integer> wishflag){
        wishList = wishflag;
        notifyDataSetChanged();
    }
    public void updateLikelist(ArrayList<Integer> likeflag){
        likeList = likeflag;
        notifyDataSetChanged();
    }
    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView proImageView,wishImageView,likeImageView;
        TextView proTextView,priceTextView,offerPriceTextView,percentTextView,productid,likscount;
        ProgressBar progressBar;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            proImageView = itemView.findViewById(R.id.cateImgView);
         //   wishImageView = itemView.findViewById(R.id.wishImgView);
        //    likscount= itemView.findViewById(R.id.likscount);
         //   likeImageView = itemView.findViewById(R.id.like);
            productid= itemView.findViewById(R.id.productid);
            proTextView = itemView.findViewById(R.id.cateNameTextView);
        //    priceTextView = itemView.findViewById(R.id.priceTextView);
       //     offerPriceTextView = itemView.findViewById(R.id.offerPriceTextView);
       //     percentTextView = itemView.findViewById(R.id.percentTextView);
            progressBar = itemView.findViewById(R.id.progressBar);

            itemView.setOnClickListener(this);
//            wishImageView.setOnClickListener(this);
  //         likeImageView.setOnClickListener(this);
            itemView.setTag(1);
      //      wishImageView.setTag(2);
    //        likeImageView.setTag(3);

        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null){
                itemClickListener.onItemClick(view,getAdapterPosition());
            }
        }

    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

}

