package com.bunakari.sambalpurifashion.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.adapter.ReletedVideoAdapter;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.RelVideoResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoDetailsActivity extends AppCompatActivity implements ReletedVideoAdapter.ItemClickListener    {

    private TextView vTextView;


    private final static String API_KEY = "AIzaSyA8HMKn-mIvPJBmK8J0zyIgLz-xzUGL9xU";
    private RecyclerView reletedRecyclerView;
    private int pos = 0 ;
    private  String VideoId,Title,Id,subcat,img,viewcount;
    private ProgressBar progressBar;
    private ImageView shareImgView,proImgView;

    private TextView notTextView,likscount;
    ArrayList<RelVideoResponse> relproductResponseList;
    ReletedVideoAdapter reletedVideoAdapter;

    private ReletedVideoAdapter.ItemClickListener itemClickListener;
    private GridLayoutManager gridLayoutManager;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);

        initUi();
        setViewData();
        GetReletedData();
        AddLikelist();
    }
    private void initUi() {
        if(!BasicFunction.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        likscount = findViewById(R.id.likscount);
        vTextView = findViewById(R.id.txtvideoid);
        reletedRecyclerView = findViewById(R.id.relRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        notTextView = findViewById(R.id.notfoundTextView);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        itemClickListener = VideoDetailsActivity.this;
        reletedRecyclerView.setNestedScrollingEnabled(false);
        shareImgView = findViewById(R.id.shareImgView);

        proImgView = (ImageView)findViewById(R.id.proImgView);


        final StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }
    private void setViewData(){

        //  pos = getIntent().getIntExtra("VideoID",0);

        VideoId = getIntent().getStringExtra("VideoID");
        Title = getIntent().getStringExtra("title");
        Id = getIntent().getStringExtra("id");
        subcat = getIntent().getStringExtra("subcat");
        img = getIntent().getStringExtra("img");
        viewcount = getIntent().getStringExtra("viewcount");
        vTextView.setText(Title);
        likscount.setText(viewcount);
        BasicFunction.showImage(img,getApplicationContext(),proImgView, progressBar);

        shareImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  ImageView proImgView = (ImageView) findViewWithTag(viewPager.getCurrentItem());
                proImgView.setDrawingCacheEnabled(true);

                Uri bmpUri = getLocalBitmapUri(proImgView);
                if (bmpUri != null) {
                    Spanned spanned = Html.fromHtml(Html.fromHtml(Title).toString());
                    String boldHeader = "*Video*";

                    // Construct a ShareIntent with link to image
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "\n"+  spanned + "\n\n" + "* Download app for view Mehndi Design Video :* \n\n" + "https://play.google.com/store/apps/details?id=com.marriagearts.mehndi" );
                    shareIntent.setType("image*//*");
                    //  shareIntent.setType("text/plain");

                    // Launch sharing dialog for image
                    startActivity(Intent.createChooser(shareIntent, "Share Video " + Title));
                } else {
                    Toast.makeText(getApplicationContext(), "Not able to share this product...", Toast.LENGTH_LONG).show();
                }
            }
        });



        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);

        getLifecycle().addObserver(youTubePlayerView);


        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                //String videoId = productResponseList.get(pos).getVideoId();
                youTubePlayer.loadVideo(VideoId, 0);

            }
        });
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {

                //     fullScreenYouTube.enterFullScreen();

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                reletedRecyclerView.setVisibility(View.GONE);
                View decorView = getWindow().getDecorView();
// Hide the status bar.
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
                //   ActionBar actionBar = getActionBar();
                //      actionBar.hide();
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                reletedRecyclerView.setVisibility(View.VISIBLE);
                View decorView = getWindow().getDecorView();
// Hide the status bar.
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
                //   ActionBar actionBar = getActionBar();
                //      actionBar.hide();

            }
        });


    }


    private void GetReletedData() {
        ApiService relproductService = RetroClass.getApiService();
        Call<ReletedVideoList> relprodServiceList = relproductService.getReletedVideos(Id,subcat);

        relprodServiceList.enqueue(new Callback<ReletedVideoList>() {
            @Override
            public void onResponse(Call<ReletedVideoList> call, Response<ReletedVideoList> response) {
                progressBar.setVisibility(View.GONE);
                relproductResponseList = new ArrayList<>();
                try {
                    List<RelVideoResponse> pageResponses = null;
                    if (response.body() != null) {
                        pageResponses = response.body().getRelproductResponseList();
                        relproductResponseList.addAll(pageResponses);

                        if (relproductResponseList.size() > 0) {

                            reletedVideoAdapter = new ReletedVideoAdapter(itemClickListener,getApplicationContext(), relproductResponseList);
                            reletedRecyclerView.setAdapter(reletedVideoAdapter);
                            reletedRecyclerView.setLayoutManager(gridLayoutManager);



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
            public void onFailure(Call<ReletedVideoList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                notTextView.setVisibility(View.VISIBLE);
                notTextView.setText("Sorry, No Data Found");
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getTag().equals(1)) {
            Intent intent = new Intent(getApplicationContext(), VideoDetailsActivity.class);
            intent.putExtra("datalist", relproductResponseList);
            intent.putExtra("position", position);
            intent.putExtra("title", relproductResponseList.get(position).getTitle());
            intent.putExtra("id", relproductResponseList.get(position).getId());
            intent.putExtra("subcat", relproductResponseList.get(position).getSubcategory());
            intent.putExtra("VideoID", relproductResponseList.get(position).getVideoId());
            intent.putExtra("viewcount", relproductResponseList.get(position).getViews());
            startActivity(intent);
            Bundle bundle = new Bundle();

        }
    }


    private void AddLikelist(){
        progressBar.setVisibility(View.VISIBLE);
        ApiService addService = RetroClass.getApiService();
        Call<SignupResponse> addResponseCall = addService.Addview(VideoId);
        addResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                progressBar.setVisibility(View.GONE);
                SignupResponse signupResponse = response.body();
                if (signupResponse != null) {
                    if (signupResponse.success == 1){
                        // likeflag = 1;
                        //     BasicFunction.showToast(getApplicationContext(),"Liked");
                    }else {
                        //       likeImageView.setImageResource(R.drawable.thumbupblack);
                        BasicFunction.showToast(getApplicationContext(),"Item can't added ");
                    }
                }else {
                    //     likeImageView.setImageResource(R.drawable.thumbupblack);
                    BasicFunction.showToast(getApplicationContext(),"Item can't added ");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                // likeImageView.setImageResource(R.drawable.thumbupblack);
                BasicFunction.showToast(getApplicationContext(),"Item can't added ");
            }
        });
    }
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            //Log.d("bmp",bmp+ " " );
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), getString(R.string.app_name) + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
