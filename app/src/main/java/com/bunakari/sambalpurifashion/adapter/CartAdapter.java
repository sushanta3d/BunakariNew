package com.bunakari.sambalpurifashion.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.CartResponse;

import java.util.List;



public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ProductViewHolder> {

    private Context context;
    private List<CartResponse> cartList;
    private ItemClickListener itemClickListener;

    public CartAdapter(ItemClickListener itemClickListener, Context context, List<CartResponse> cartList) {
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_item,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, int position) {

        Log.d("SizeColor",cartList.get(position).getSize()+"\n"+cartList.get(position).getColorcode());

        holder.proTextView.setText(cartList.get(position).getProname());
        if (cartList.get(position).getOffer_price().length() != 0) {
            holder.offerPriceTextView.setText("\u20B9 "+cartList.get(position).getPrice());
            holder.offerPriceTextView.setPaintFlags(holder.offerPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.priceTextView.setText("\u20B9 "+cartList.get(position).getOffer_price());
        }else {
            holder.priceTextView.setText("\u20B9 "+cartList.get(position).getPrice());
            holder.offerPriceTextView.setVisibility(View.GONE);
        }
        holder.qtyTextView.setText(cartList.get(position).getQty());

        if (cartList.get(position).getSize().length() != 0) {
            holder.sizeTextView.setText(cartList.get(position).getSize());
        }else {
            holder.sizeTitleTextView.setVisibility(View.GONE);
            holder.sizeTextView.setVisibility(View.GONE);
        }

        if (cartList.get(position).getColorcode().length() != 0) {
            holder.colorCardView.setCardBackgroundColor(Color.parseColor(cartList.get(position).getColorcode()));
        }else {
            holder.colorTitleTextView.setVisibility(View.GONE);
            holder.colorCardView.setVisibility(View.GONE);
        }

        BasicFunction.showImage(cartList.get(position).getImg(),context,holder.proImageView,holder.progressBar);

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView proImageView,cancelImageView,minusImageView,plusImageView;
        TextView proTextView,priceTextView,offerPriceTextView,sizeTitleTextView,sizeTextView,qtyTextView,colorTitleTextView;
        ProgressBar progressBar;
        CardView colorCardView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            proImageView = itemView.findViewById(R.id.proImgView);
            cancelImageView = itemView.findViewById(R.id.cancelImgView);
            minusImageView = itemView.findViewById(R.id.minusImgView);
            plusImageView = itemView.findViewById(R.id.plusImgView);
            proTextView = itemView.findViewById(R.id.pNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            offerPriceTextView = itemView.findViewById(R.id.offerPriceTextView);
            sizeTitleTextView = itemView.findViewById(R.id.sizeTitleTextView);
            sizeTextView = itemView.findViewById(R.id.sizeValueTextView);
            colorTitleTextView = itemView.findViewById(R.id.colorTitleTextView);
            colorCardView = itemView.findViewById(R.id.colorCardView);
            qtyTextView = itemView.findViewById(R.id.qtyTextView);
            progressBar = itemView.findViewById(R.id.progressBar);

            cancelImageView.setOnClickListener(this);
            minusImageView.setOnClickListener(this);
            plusImageView.setOnClickListener(this);
            cancelImageView.setTag(1);
            minusImageView.setTag(2);
            plusImageView.setTag(3);
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
