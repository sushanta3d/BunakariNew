package com.bunakari.sambalpurifashion.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.adapter.VideoAdapter;
import com.bunakari.sambalpurifashion.model.VideoList;
import com.bunakari.sambalpurifashion.model.VideoResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabFragment extends Fragment implements VideoAdapter.ItemClickListener {

    int position;
    String catId,SubId;
    TextView catTitleTextView,notfoundTextView,textCartItemCount;
    RecyclerView productRecyclerView;
    ProgressBar progressBar;
    ArrayList<VideoResponse> productResponseList;
    VideoAdapter videoAdapter;
    private VideoAdapter.ItemClickListener itemClickListener;
    private SharedPreferences sharedPreferences;


    private GridLayoutManager gridLayoutManager;
    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);

        TabFragment tabFragment = new TabFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        catTitleTextView = (TextView)view.findViewById(R.id.cateNameTextView);
        notfoundTextView = view.findViewById(R.id.notfoundTextView);
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        //cateNameTextView.setText(catId);
         GetVideoData();
        gridLayoutManager = new GridLayoutManager(getContext(), 1);

        itemClickListener = TabFragment.this;


    }


    private void GetVideoData() {
        ApiService productService = RetroClass.getApiService();
        Call<VideoList> prodServiceList = productService.getVideos(String.valueOf(position+1));

        prodServiceList.enqueue(new Callback<VideoList>() {
            @Override
            public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                progressBar.setVisibility(View.GONE);
                productResponseList = new ArrayList<>();
                try {
                    List<VideoResponse> pageResponses = null;
                    if (response.body() != null) {
                        pageResponses = response.body().getProductResponseList();
                        productResponseList.addAll(pageResponses);

                        if (productResponseList.size() > 0) {



                            videoAdapter = new VideoAdapter(itemClickListener,getContext(), productResponseList);
                            productRecyclerView.setAdapter(videoAdapter);
                            productRecyclerView.setLayoutManager(gridLayoutManager);
                        } else {
                            notfoundTextView.setVisibility(View.VISIBLE);
                            notfoundTextView.setText("Something Went Wrong");
                        }
                    }else {
                        notfoundTextView.setVisibility(View.VISIBLE);
                        notfoundTextView.setText("Sorry, No Data Found");
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                    notfoundTextView.setVisibility(View.VISIBLE);
                    notfoundTextView.setText("Sorry, No Data Found");
                }

            }

            @Override
            public void onFailure(Call<VideoList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                notfoundTextView.setVisibility(View.VISIBLE);
                notfoundTextView.setText("Sorry, No Data Found");
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getTag().equals(1)) {
            Intent intent = new Intent(getContext(), VideoDetailsActivity.class);
            intent.putExtra("datalist", productResponseList);
            intent.putExtra("position", position);
            intent.putExtra("title", productResponseList.get(position).getTitle());
            intent.putExtra("img", productResponseList.get(position).getImg());
            intent.putExtra("id", productResponseList.get(position).getId());
            intent.putExtra("subcat", productResponseList.get(position).getSubcategory());
            intent.putExtra("VideoID", productResponseList.get(position).getVideoId());
            intent.putExtra("viewcount", productResponseList.get(position).getViews());

            Bundle bundle = new Bundle();
            startActivity(intent);

        }
    }
}
