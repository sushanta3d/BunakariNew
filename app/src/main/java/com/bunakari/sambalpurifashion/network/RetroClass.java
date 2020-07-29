package com.bunakari.sambalpurifashion.network;

import com.bunakari.sambalpurifashion.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClass {

    public static final String BANNER_PATH = BuildConfig.BASE_URL + "admin/images/banner/";
    public static final String CATEGORY_PATH = BuildConfig.BASE_URL + "admin/images/category/";
    public static final String BANNER_CATEGORY_PATH = BuildConfig.BASE_URL + "admin/images/category/";
    public static final String PRODUCT_PATH1 = BuildConfig.BASE_URL + "admin/images/package/";
    public static final String PRODUCT_PATH2 = BuildConfig.BASE_URL + "admin/images/package/";
    public static final String PRODUCT_PATH3 = BuildConfig.BASE_URL + "admin/images/package/";
    public static final String PRODUCT_PATH4 = BuildConfig.BASE_URL + "admin/images/package/";
    public static final String PRODUCT_PATH5 = BuildConfig.BASE_URL + "admin/images/package/";
    public static final String USER_IMG_PATH = BuildConfig.BASE_URL + "admin/images/package/";

    private static Retrofit getRetofitInstance(){

        Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setPrettyPrinting()
                .create();

        OkHttpClient innerClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES) // read timeout
                .build();

        return new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(innerClient)
                .build();

    }

    public static ApiService getApiService(){
        return getRetofitInstance().create(ApiService.class);
    }

}
