package com.bunakari.sambalpurifashion.view;

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
import com.bunakari.sambalpurifashion.model.SignupResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.HomeProductViewHolder> {
    private Context context;
    private ArrayList<ProductResponse> productList;
    private ArrayList<Integer> wishList;
    private ArrayList<Integer> likeList;

    private  getAdapterItemLcick itemLcick;

    public interface getAdapterItemLcick{
        public void getItemClick(int position, ProductResponse response);

        public void getLikeClick(int position, ProductResponse response, ImageView imageView);
    }


    public HomeProductAdapter(  Context context, ArrayList<ProductResponse> productList, ArrayList<Integer> wishList, ArrayList<Integer>likeList,getAdapterItemLcick adapterItemLcick) {
       this.itemLcick=adapterItemLcick;
        this.context = context;
        this.productList = productList;
        this.wishList = wishList;
        this.likeList = likeList;

    }


    @NonNull
    @Override
    public HomeProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pro_grid_item,parent,false);
        return new HomeProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeProductViewHolder holder, int position) {
       ProductResponse response= productList.get(position);
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


        holder.proImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemLcick.getItemClick(position, response);
            }
        });
        holder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.likeImageView.setImageResource(R.drawable.thumbupwhite);
                itemLcick.getLikeClick(position,response,holder.likeImageView);
            }
        });

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
    public class HomeProductViewHolder extends RecyclerView.ViewHolder{

        ImageView proImageView,wishImageView,likeImageView;
        TextView proTextView,priceTextView,offerPriceTextView,percentTextView,productid,likscount,watermark;
        ProgressBar progressBar;

        public HomeProductViewHolder(@NonNull View itemView ) {
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


        }


    }

    public void AddLikelist(Context  context, String id,String mobileString,ImageView likeImageView ){
     //   progressBar.setVisibility(View.VISIBLE);
        ApiService addService = RetroClass.getApiService();
        Call<SignupResponse> addResponseCall = addService.addLikelist(id,mobileString);
        addResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
               // progressBar.setVisibility(View.GONE);
                SignupResponse signupResponse = response.body();
                if (signupResponse != null) {
                    if (signupResponse.success == 1){
                     //   likeList.get(0).get = 1;
                        likeImageView.setImageResource(R.drawable.thumbupwhite);
                        BasicFunction.showToast(context,"Liked");
                    }else {
                        likeImageView.setImageResource(R.drawable.thumbupblack);
                        BasicFunction.showToast(context,"Item can't added ");
                    }
                }else {
                    likeImageView.setImageResource(R.drawable.thumbupblack);
                    BasicFunction.showToast(context,"Item can't added ");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
             //   progressBar.setVisibility(View.GONE);
                likeImageView.setImageResource(R.drawable.thumbupblack);
                BasicFunction.showToast(context,"Item can't added ");
            }
        });
    }

}
