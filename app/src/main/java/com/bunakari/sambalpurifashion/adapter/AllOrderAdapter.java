package com.bunakari.sambalpurifashion.adapter;

import android.content.Context;
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
import com.bunakari.sambalpurifashion.model.OrderDetailsResponse;
import com.bunakari.sambalpurifashion.network.RetroClass;

import java.util.List;


public class AllOrderAdapter extends RecyclerView.Adapter<AllOrderAdapter.ProductViewHolder> {

    private Context context;
    private List<OrderDetailsResponse> orderDetailsList;
    private ItemClickListener itemClickListener;

    public AllOrderAdapter(ItemClickListener itemClickListener, Context context, List<OrderDetailsResponse> orderDetailsList) {
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.orderDetailsList = orderDetailsList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orderdetails_list_item,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, int position) {

        OrderDetailsResponse detailsResponse = orderDetailsList.get(position);

        holder.proTextView.setText(detailsResponse.getProname());
       /* if (cartList.get(position).getOffer_price().length() != 0) {
            holder.offerPriceTextView.setText("\u20B9 "+cartList.get(position).getPrice());
            holder.offerPriceTextView.setPaintFlags(holder.offerPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.priceTextView.setText("\u20B9 "+cartList.get(position).getOffer_price());
        }else {
            holder.priceTextView.setText("\u20B9 "+cartList.get(position).getPrice());
            holder.offerPriceTextView.setVisibility(View.GONE);
        }*/
        holder.priceTextView.setText("\u20B9 "+detailsResponse.getProprice());
        holder.qtyTextView.setText(detailsResponse.getQty());

   /*     if (detailsResponse.getSize().length() != 0) {
            holder.sizeTextView.setText(detailsResponse.getSize());
        }else {
            holder.sizeTitleTextView.setVisibility(View.GONE);
            holder.sizeTextView.setVisibility(View.GONE);
        }*/

       /* if (detailsResponse.getColorcode().length() != 0) {
            holder.colorCardView.setCardBackgroundColor(Color.parseColor(detailsResponse.getColorcode()));
        }else {
            holder.colorTitleTextView.setVisibility(View.GONE);
            holder.colorCardView.setVisibility(View.GONE);
        }*/

        BasicFunction.showImage(RetroClass.PRODUCT_PATH1 + detailsResponse.getProimg(),context,holder.proImageView,holder.progressBar);

    }

    @Override
    public int getItemCount() {
        return orderDetailsList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView proImageView;
        TextView proTextView,priceTextView,offerPriceTextView,sizeTitleTextView,sizeTextView,qtyTextView,colorTitleTextView;
        ProgressBar progressBar;
        CardView colorCardView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            proImageView = itemView.findViewById(R.id.proImgView);
            proTextView = itemView.findViewById(R.id.pNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            offerPriceTextView = itemView.findViewById(R.id.offerPriceTextView);
            sizeTitleTextView = itemView.findViewById(R.id.sizeTitleTextView);
            sizeTextView = itemView.findViewById(R.id.sizeValueTextView);
            colorTitleTextView = itemView.findViewById(R.id.colorTitleTextView);
            colorCardView = itemView.findViewById(R.id.colorCardView);
            qtyTextView = itemView.findViewById(R.id.qtyTextView);
            progressBar = itemView.findViewById(R.id.progressBar);
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
