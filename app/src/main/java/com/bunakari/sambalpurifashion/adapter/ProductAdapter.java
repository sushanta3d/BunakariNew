package com.bunakari.sambalpurifashion.adapter;

import android.content.Context;
import android.graphics.Paint;
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
import com.bunakari.sambalpurifashion.model.ProductResponse;

import java.util.ArrayList;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private ArrayList<ProductResponse> productList;
    private ArrayList<Integer> wishList;
    private ArrayList<Integer> likeList;
    private ItemClickListener itemClickListener;

    public ProductAdapter(ItemClickListener itemClickListener, Context context, ArrayList<ProductResponse> productList, ArrayList<Integer> wishList, ArrayList<Integer>likeList) {
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.productList = productList;
        this.wishList = wishList;
        this.likeList = likeList;

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pro_grid_item,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        BasicFunction.showImage(productList.get(position).getImg(),context,holder.proImageView,holder.progressBar);
        holder.proTextView.setText(productList.get(position).getProname());
        holder.productid.setText("BUN00"+productList.get(position).getId());



        if (productList.get(position).getOffer_price().length() != 0) {
            holder.offerPriceTextView.setText("\u20B9 "+productList.get(position).getPrice());
            holder.offerPriceTextView.setPaintFlags(holder.offerPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.priceTextView.setText("\u20B9 "+productList.get(position).getOffer_price());
            int percent = ((Integer.parseInt(productList.get(position).getPrice()) - Integer.parseInt(productList.get(position).getOffer_price())) * 100) / Integer.parseInt(productList.get(position).getPrice());
            if (percent != 0){
                holder.percentTextView.setText(percent+"% Off");
            }
        }else {
            holder.priceTextView.setText("MA00"+productList.get(position).getPrice());
            holder.offerPriceTextView.setVisibility(View.GONE);
            holder.percentTextView.setVisibility(View.GONE);
        }

        if (wishList.get(position) == 0){
            holder.wishImageView.setImageResource(R.drawable.ic_wishlist);
        }else {
            holder.wishImageView.setImageResource(R.drawable.ic_wishlistfill);
        }
        if (likeList.get(position) == 0){
            holder.likeImageView.setImageResource(R.drawable.thumbupblack);
        }else {
            holder.likeImageView.setImageResource(R.drawable.thumbupwhite);
        }



    }

    @Override
    public int getItemCount() {
        return productList.size();
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
        TextView proTextView,priceTextView,offerPriceTextView,percentTextView,productid,likscount,watermark;
        ProgressBar progressBar;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            proImageView = itemView.findViewById(R.id.cateImgView);
            wishImageView = itemView.findViewById(R.id.wishImgView);
            likscount= itemView.findViewById(R.id.likscount);
            likeImageView = itemView.findViewById(R.id.like);
            productid= itemView.findViewById(R.id.productid);
            proTextView = itemView.findViewById(R.id.cateNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            offerPriceTextView = itemView.findViewById(R.id.offerPriceTextView);
            percentTextView = itemView.findViewById(R.id.percentTextView);
            progressBar = itemView.findViewById(R.id.progressBar);
            watermark = itemView.findViewById(R.id.watermark);
            itemView.setOnClickListener(this);
            wishImageView.setOnClickListener(this);
            likeImageView.setOnClickListener(this);
            itemView.setTag(1);
            wishImageView.setTag(2);
            likeImageView.setTag(3);

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
