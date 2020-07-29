package com.bunakari.sambalpurifashion.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.marriagearts.mehndi.R;


public class BasicFunction {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager localConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (localConnectivityManager == null) return false;

        NetworkInfo[] arrayOfNetworkInfo = localConnectivityManager.getAllNetworkInfo();
        if (arrayOfNetworkInfo == null) return false;

        for (int i = 0; i < arrayOfNetworkInfo.length; i++) {
            if (arrayOfNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                return true;
        }
        return false;
    }

    public static void showSnackbar(Activity activity, String msg){
        View parentView = activity.findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentView, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showImage(String image, Context context, ImageView imageView, final ProgressBar progressBar){
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.imgbg);
        requestOptions.error(R.drawable.imgbg);

        Glide.with(context)
                .load(image)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

    public static void showImage2(String image, Context context, ImageView imageView, final ProgressBar progressBar){
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.person);
        requestOptions.error(R.drawable.person);

        Glide.with(context)
                .load(image)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

    public static void showDailogBox(Activity activity, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(msg);
        builder.setNegativeButton("Ok",null);
        builder.show();
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
