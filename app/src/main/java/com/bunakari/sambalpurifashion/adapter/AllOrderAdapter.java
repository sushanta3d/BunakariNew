package com.bunakari.sambalpurifashion.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.OrderDetailsResponse;
import com.bunakari.sambalpurifashion.network.RetroClass;
import com.bunakari.sambalpurifashion.view.EditAllReviewActivity;

import java.util.List;



public class AllOrderAdapter extends RecyclerView.Adapter<AllOrderAdapter.ProductViewHolder> {

    private Context context;
    private List<OrderDetailsResponse> orderDetailsList;
    private ItemClickListener itemClickListener;
    private String ostatusString,oidString;

    public AllOrderAdapter(ItemClickListener itemClickListener, Context context, List<OrderDetailsResponse> orderDetailsList, String ostatusString, String oidString) {
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.orderDetailsList = orderDetailsList;
        this.ostatusString = ostatusString;
        this.oidString = oidString;
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

        if (detailsResponse.getSize().length() != 0) {
            holder.sizeTextView.setText(detailsResponse.getSize());
        }else {
            holder.sizeTitleTextView.setVisibility(View.GONE);
            holder.sizeTextView.setVisibility(View.GONE);
        }

        if (detailsResponse.getColorcode().length() != 0) {
            holder.colorCardView.setCardBackgroundColor(Color.parseColor(detailsResponse.getColorcode()));
        }else {
            holder.colorTitleTextView.setVisibility(View.GONE);
            holder.colorCardView.setVisibility(View.GONE);
        }

        BasicFunction.showImage(RetroClass.PRODUCT_PATH1 + detailsResponse.getProimg(),context,holder.proImageView,holder.progressBar);

        if (ostatusString.equalsIgnoreCase("2")) {
              holder.rateBar.setRating(Float.parseFloat(orderDetailsList.get(position).getRatting()));

        }

        //0 Ordered
        //1 Confirmed
        //2 Dispatched
        //9 Cancelled
        //8 Refunded
        //6 Delivered
        //99 Returned

        if (detailsResponse.getStatus().equals("0")){
            holder.oStatusTextView.setText("# Ordered");
            holder.oStatusTextView.setTextColor(context.getResources().getColor(R.color.lorange));
            holder.rateTitleTextView.setVisibility(View.GONE);
            holder.cancelTextView.setVisibility(View.VISIBLE);
        }else if (detailsResponse.getStatus().equals("1")){
            holder.oStatusTextView.setText("# Confirmed");
            holder.oStatusTextView.setTextColor(context.getResources().getColor(R.color.lred));
            holder.rateTitleTextView.setVisibility(View.GONE);
            holder.cancelTextView.setVisibility(View.VISIBLE);
        }else if (detailsResponse.getStatus().equals("2")){
            holder.oStatusTextView.setText("# Dispatched");
            holder.oStatusTextView.setTextColor(context.getResources().getColor(R.color.lgreen));
            holder.rateTitleTextView.setVisibility(View.GONE);
            holder.cancelTextView.setVisibility(View.GONE);
            holder.returnTextView.setVisibility(View.GONE);
        }else if (detailsResponse.getStatus().equals("9")){
            holder.oStatusTextView.setText("# Cancelled");
            holder.oStatusTextView.setTextColor(context.getResources().getColor(R.color.lblue));
            holder.rateTitleTextView.setVisibility(View.GONE);
            holder.cancelTextView.setVisibility(View.GONE);
            holder.returnTextView.setVisibility(View.GONE);
        }else if (detailsResponse.getStatus().equals("8")){
            holder.oStatusTextView.setText("# Refunded");
            holder.oStatusTextView.setTextColor(context.getResources().getColor(R.color.flat_yellow_1));
            holder.rateTitleTextView.setVisibility(View.GONE);
            holder.cancelTextView.setVisibility(View.GONE);
            holder.returnTextView.setVisibility(View.GONE);
        }else if (detailsResponse.getStatus().equals("6")){
            holder.oStatusTextView.setText("# Delivered");
            holder.oStatusTextView.setTextColor(context.getResources().getColor(R.color.flat_yellow_1));
            holder.rateTitleTextView.setVisibility(View.VISIBLE);
            holder.cancelTextView.setVisibility(View.GONE);
            holder.returnTextView.setVisibility(View.VISIBLE);
        }else if (detailsResponse.getStatus().equals("99")){
            holder.oStatusTextView.setText("# Returned");
            holder.oStatusTextView.setTextColor(context.getResources().getColor(R.color.flat_yellow_1));
            holder.rateTitleTextView.setVisibility(View.GONE);
            holder.cancelTextView.setVisibility(View.GONE);
            holder.returnTextView.setVisibility(View.GONE);
        }

        holder.cancelTextView.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditAllReviewActivity.class);
            intent.putExtra("from","0");
            intent.putExtra("crid1",detailsResponse.getPid());
            intent.putExtra("crid2",oidString);
            intent.putExtra("crid3",detailsResponse.getOrderproductid());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        holder.returnTextView.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditAllReviewActivity.class);
            intent.putExtra("from","1");
            intent.putExtra("crid1",detailsResponse.getPid());
            intent.putExtra("crid2",oidString);
            intent.putExtra("crid3",detailsResponse.getOrderproductid());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        holder.rateTitleTextView.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditAllReviewActivity.class);
            intent.putExtra("from","2");
            intent.putExtra("crid1",detailsResponse.getPid());
            intent.putExtra("crid2",detailsResponse.getRatting());
            intent.putExtra("crid3",detailsResponse.getOrderproductid());
            intent.putExtra("msg",detailsResponse.getRatingmsg());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.e("Alldatares",detailsResponse.getRatingmsg()+" "+detailsResponse.getOrderproductid());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return orderDetailsList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView proImageView;
        TextView proTextView,priceTextView,offerPriceTextView,sizeTitleTextView,sizeTextView,rateTitleTextView,
                qtyTextView,colorTitleTextView,oStatusTextView,refundTextView,cancelTextView,returnTextView;
        ProgressBar progressBar;
        CardView colorCardView,rateCardView;
        RatingBar rateBar;
        LinearLayout rrLayout;

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
            rateCardView = itemView.findViewById(R.id.rateCardView);
            rateBar = itemView.findViewById(R.id.rateBar);
            qtyTextView = itemView.findViewById(R.id.qtyTextView);
            progressBar = itemView.findViewById(R.id.progressBar);
            oStatusTextView = itemView.findViewById(R.id.statusTextView);
            cancelTextView = itemView.findViewById(R.id.cancelTextView);
            returnTextView = itemView.findViewById(R.id.returnTextView);
            rateTitleTextView = itemView.findViewById(R.id.rateTitleTextView);
            rrLayout = itemView.findViewById(R.id.rrLayout);

        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null){
                itemClickListener.onItemClick(view,getAdapterPosition(),rateBar.getRating());
            }
        }

    }

    public interface ItemClickListener{
        void onItemClick(View view, int position, Float rate);
    }

}
