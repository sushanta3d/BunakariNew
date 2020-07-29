package com.bunakari.sambalpurifashion.view;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.adapter.CategotyAdapter;
import com.bunakari.sambalpurifashion.model.BannerList;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.CategoryList;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.HomePageResponse;
import com.bunakari.sambalpurifashion.model.ProductList;
import com.bunakari.sambalpurifashion.model.ProductResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements CategotyAdapter.ItemClickListener {

    private ViewFlipper bannerFlipper;
    private RecyclerView cateRecyclerView,prdRecyclerView;
    private ProgressBar progressBar;
    private TextView notTextView;
    private List<HomePageResponse> bannerList;
    private ArrayList<HomePageResponse> categoryList;
    private ArrayList<HomePageResponse> responses;
    String cId,catName,mobileString,uidString;
    private CategotyAdapter categotyAdapter;
    private SliderLayout sliderLayout;
    private CategotyAdapter.ItemClickListener itemClickListener;
    String imageUrl = "http://www.marriagearts.com/admin/images/banner/VideoBanner.png";
    private ImageView banImgView;
    private NestedScrollView nestedScrollHome;
    private GridLayoutManager gridLayoutManager;
    ArrayList<ProductResponse> productResponseList;
   HomeProductAdapter homeProductAdapter;

    private SharedPreferences sharedPreferences;
    private int cartcount = 0;
    private ArrayList<Integer> wishflag = new ArrayList<>();
    private ArrayList<Integer> likeflag = new ArrayList<>();
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        initUi(v);

        if (BasicFunction.isNetworkAvailable(getActivity())) {
            GetBannerData();
           GetCategoryData();
         GetProductData();
        } else {
            BasicFunction.showSnackbar(getActivity(), "No internet connection,Please try again..!!");
        }

        return v;
    }

    private void initUi(View v) {
        nestedScrollHome = v.findViewById(R.id.nestedScrollHome);
        sliderLayout = v.findViewById(R.id.sliderLayout);
        cateRecyclerView = v.findViewById(R.id.cateRecyclerView);
        progressBar = v.findViewById(R.id.progressBar);
        notTextView = v.findViewById(R.id.notfoundTextView);
        prdRecyclerView = v.findViewById(R.id.prdRecyclerView);
        banImgView = v.findViewById(R.id.banImgView);
        sharedPreferences = getActivity().getSharedPreferences(GetPrefs.PREFS_NAME,getActivity().MODE_PRIVATE);
        uidString = sharedPreferences.getString(GetPrefs.PREFS_UID,"");
        mobileString = sharedPreferences.getString(GetPrefs.PREFS_MOBILE,"");
      //  cateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

       int numberOfColumns = 2;
        cateRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        cateRecyclerView.setNestedScrollingEnabled(false);
        itemClickListener = HomeFragment.this;
       // prdRecyclerView.setLayoutManager(gridLayoutManager);
        prdRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        prdRecyclerView.setNestedScrollingEnabled(false);

        BasicFunction.showImage(imageUrl,getContext(),banImgView,progressBar);

        banImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vintent = new Intent(getContext(),VideosActivity.class);
                startActivity(vintent);
            }
        });

    }


    private void GetBannerData() {
        ApiService bannerService = RetroClass.getApiService();
        Call<BannerList> bannerResponseCall = bannerService.getBanner();

        bannerResponseCall.enqueue(new Callback<BannerList>() {
            @Override
            public void onResponse(Call<BannerList> call, Response<BannerList> response) {
                bannerList = new ArrayList<>();
                try {
                    List<HomePageResponse> pageResponses = response.body().getBannerList();
                    for (int i = 0; i < pageResponses.size(); i++) {
                        HomePageResponse homePageResponse = pageResponses.get(i);
                        homePageResponse.setBanner(pageResponses.get(i).getBanner());
                        homePageResponse.setName(pageResponses.get(i).getName());
                        bannerList.add(homePageResponse);
                    }

                    if (bannerList.size() > 0) {

                        for (int i = 0; i < 5; i++) {
                            DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
                            int finalI = i;
                            defaultSliderView.image(RetroClass.BANNER_PATH + bannerList.get(i).banner).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {

                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    Intent intent = new Intent(getActivity(), ProductActivity.class);
                                    intent.putExtra("catid", bannerList.get(finalI).name  );
                                    intent.putExtra("catname", "Marriage Arts");
                                    startActivity(intent);
                                }
                            }); // adding images here

                            sliderLayout.addSlider(defaultSliderView);
                        }

                        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
                        sliderLayout.setDuration(3000);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<BannerList> call, Throwable t) {

            }
        });
    }

    private void GetCategoryData() {
        ApiService cateService = RetroClass.getApiService();
        Call<CategoryList> cateServiceList = cateService.getCategory();

        cateServiceList.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                progressBar.setVisibility(View.GONE);
                categoryList = new ArrayList<>();
                try {
                    List<HomePageResponse> pageResponses = response.body().getCateResponseList();

                    categoryList.addAll(pageResponses);

                    if (categoryList.size() > 0) {
                        categotyAdapter = new CategotyAdapter(itemClickListener, getActivity(), categoryList);
                        cateRecyclerView.setAdapter(categotyAdapter);
                    } else {
                        notTextView.setVisibility(View.VISIBLE);
                        notTextView.setText("Sorry, No Data Found");
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    notTextView.setVisibility(View.VISIBLE);
                    notTextView.setText("Sorry, No Data Found");
                }

            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                notTextView.setVisibility(View.VISIBLE);
                notTextView.setText("Sorry, No Data Found");
            }
        });

        nestedScrollHome.smoothScrollTo(0, 0);
    }


    private void GetProductData() {
        ApiService productService = RetroClass.getApiService();
        Call<ProductList> prodServiceList = productService.gethomeProducts("2",mobileString);

        prodServiceList.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                progressBar.setVisibility(View.GONE);
                productResponseList = new ArrayList<>();
                try {
                    List<ProductResponse> pageResponses = null;
                    if (response.body() != null) {
                        pageResponses = response.body().getProductResponseList();
                        productResponseList.addAll(pageResponses);

                        if (productResponseList.size() > 0) {
                            wishflag = new ArrayList<>();
                            likeflag = new ArrayList<>();
                            for (int i = 0; i < productResponseList.size(); i++) {
                                wishflag.add(0);
                                likeflag.add(0);
                            }

                            for (int i = 0; i < productResponseList.size(); i++) {
                                wishflag.set(i,productResponseList.get(i).getInwishlist());
                                likeflag.set(i,productResponseList.get(i).getInlikelist());
                            }


                            // Create the recyclerViewAdapter
                            homeProductAdapter = new HomeProductAdapter(getActivity(), productResponseList, wishflag, likeflag, new HomeProductAdapter.getAdapterItemLcick() {
                                @Override
                                public void getItemClick(int position, ProductResponse response) {
                                   // Toast.makeText( getActivity(), productResponseList.get(position).getId(), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity() ,ProductDetailsActivity.class);
                                    intent.putExtra("datalist", productResponseList);
                                    intent.putExtra("position", position);
                                    intent.putExtra("cId",  productResponseList.get(position).getCategoryid());
                                    intent.putExtra("catName",  productResponseList.get(position).getCategory());
                                    startActivity(intent);
                            //        Bundle bundle = new Bundle();
                                  //  bundle.putString(FirebaseAnalytics.Param.ITEM_ID, productResponseList.get(position).getId());
                                 //   bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, productResponseList.get(position).getProname());
                                 //   bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");


                                }

                                @Override
                                public void getLikeClick(int position, ProductResponse response, ImageView imageView) {
                                    homeProductAdapter.AddLikelist(getContext(),response.getId(),mobileString,imageView);
                                }
                            });
                            prdRecyclerView.setAdapter(homeProductAdapter);

                      //      prdRecyclerView.setLayoutManager(gridLayoutManager);
                        } else {
                            notTextView.setVisibility(View.VISIBLE);
                            notTextView.setText("Something Went Wrong");
                        }
                    }else {
                        notTextView.setVisibility(View.VISIBLE);
                        notTextView.setText("Sorry, No Data Found");
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                    notTextView.setVisibility(View.VISIBLE);
                    notTextView.setText("Sorry, No Data Found");
                }

            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                notTextView.setVisibility(View.VISIBLE);
                notTextView.setText("Sorry, No Data Found");
            }
        });
    }


    @Override
    public void onItemClick(View view, int position) {
        if (categoryList.get(position).getStatus().equalsIgnoreCase("1")) {

            Intent intent = new Intent(getActivity(), ViewAllCategoryActivity.class);
            intent.putExtra("catid", categoryList.get(position).getCid());
            intent.putExtra("catname", categoryList.get(position).getCategory());
            intent.putExtra("datalist", categoryList.get(position).getSc());

            startActivity(intent);
        }

    }




}
