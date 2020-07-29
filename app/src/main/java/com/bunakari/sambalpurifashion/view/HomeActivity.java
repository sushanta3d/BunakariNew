package com.bunakari.sambalpurifashion.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.adapter.NavDrawerListAdapter;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.CartList;
import com.bunakari.sambalpurifashion.model.CartResponse;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.NavDrawerItem;
import com.bunakari.sambalpurifashion.model.ProfileResponse;
import com.bunakari.sambalpurifashion.model.SignupResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    public String appPackageName = null;

    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    int cartcount;
    public ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    SharedPreferences sharedpreferences;
    String userName,referId,uidString,sessionString,posString,fcmString,regFcmString="",mobileString,msgString;
    TextView userNameTextView,textCartItemCount;

    private ImageView userImageView;
    private ProgressBar progressBar;

    @SuppressWarnings("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        appPackageName = getApplicationContext().getPackageName();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_menu_icon);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        sharedpreferences = getSharedPreferences(GetPrefs.PREFS_NAME, getApplicationContext().MODE_PRIVATE);

        mTitle = mDrawerTitle = getTitle();

     //   mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // load slide menu items

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        posString = getIntent().getStringExtra("pos");
        msgString = getIntent().getStringExtra("msg");

        userName = sharedpreferences.getString(GetPrefs.PREFS_UNAME,"");
        referId = sharedpreferences.getString(GetPrefs.PREFS_RFERRAL_ID,"");
        uidString = sharedpreferences.getString(GetPrefs.PREFS_UID,"");
        mobileString = sharedpreferences.getString(GetPrefs.PREFS_MOBILE,"");
        fcmString = sharedpreferences.getString(GetPrefs.PREFS_NOTIFICATIONS,"");
        regFcmString = sharedpreferences.getString(GetPrefs.PREFS_REGISTER_FCM,"");

        if (regFcmString.length() == 0){
            RegisterFcm();
        }

        ViewGroup header = (ViewGroup) ((LayoutInflater) getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.drawer_header, mDrawerList, false);

        mDrawerList.addHeaderView(header, null, false);

        userNameTextView = header.findViewById(R.id.userNameTextView);
        userImageView = header.findViewById(R.id.userImgView);
        progressBar = header.findViewById(R.id.progressBar);

        userNameTextView.setText(userName);
       // referIdTextView.setText("Ref ID : "+referId);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
                .getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
                .getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
                .getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
                .getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
                .getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
                .getResourceId(5, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
                .getResourceId(6, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons
                .getResourceId(7, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons
                .getResourceId(8, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons
                .getResourceId(9, -1)));

        // Photos

        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems, this.mDrawerList);
        mDrawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                 // nav menu toggle icon
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for
                // accessibility
        ) {

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                // invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("Marriage Arts");
                // calling onPrepareOptionsMenu() to hide action bar icons
                // invalidateOptionsMenu();
            }
        };

        //displayView(0);[
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            if (posString.equals("5")){
                displayView(5);
            }else {
                displayView(0);
            }

        }
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;


        switch (position) {

            case 0:
                fragment = new HomeFragment();
                break;

            case 1:
                fragment = new HomeFragment();
                break;
            case 2:
                fragment = new MyProfileFragment();
                break;
            case 3:
                fragment = new MyOrderFragment();
                break;

            case 4:
                fragment = new WishlistFragment();
                break;
            case 5:
                fragment = new ContactUsFragment();
                break;
            case 6:
                fragment = new TermsConditionFragment();
                break;
            case 7:
                fragment = new PrivacyPolicyFragment();
                break;
            case 8:
                shareus();
                break;
            case 9:
                rateus();
                break;
            case 10:
                logout();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            if (position == 0) {
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                setTitle(navMenuTitles[position]);
            } else {
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position - 1);

                setTitle(navMenuTitles[position - 1]);
            }
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
    //**

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;

    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            doExit();
        }
    }

    @SuppressLint("ApplySharedPref")
    public void logout(){
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setPositiveButton("Yes",
                (paramDialogInterface, paramInt) -> {
                    SharedPreferences settings = getApplicationContext().getSharedPreferences(GetPrefs.PREFS_NAME, getApplicationContext().MODE_PRIVATE);
                    settings.edit().clear().commit();

                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                    startActivity(intent);
                    finish();
                });
        localBuilder.setNegativeButton("No", null);
        localBuilder.setIcon(R.mipmap.ic_launcher);
        localBuilder.setTitle(R.string.app_name);
        localBuilder.setMessage("Are you sure you want to log out ?");
        localBuilder.show();
    }

    public void shareus() {
        String appRefCode = sharedpreferences.getString(GetPrefs.PREFS_APP_RFERRAL_CODE,"");
        String shareBody = "Book Mehndi and Tattoo Artist  | Upto 40% Off On All Mehndi and Tattoo Package. Enter my code " + appRefCode + " to earn ₹200\" \n \"https://play.google.com/store/apps/details?id="
                + appPackageName;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources()
                .getString(R.string.app_name)));
    }

    public void rateus() {
        Intent marketIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appPackageName));
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
                | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(marketIntent);
    }

    private void doExit() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(HomeActivity.this);
        localBuilder.setPositiveButton("Yes",
                (paramDialogInterface, paramInt) -> {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    // finish();

                    HomeActivity.this.finish();
                });
        localBuilder.setNegativeButton("No", null);
        new AlertDialog.Builder(this);
        localBuilder.setIcon(R.mipmap.ic_launcher);
        localBuilder.setTitle(R.string.app_name);
        localBuilder.setMessage("Do you want to exit");
        localBuilder.show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void GetProfileData() {

        ApiService apiService = RetroClass.getApiService();
        Call<ProfileResponse> profileResponseCall = apiService.getProfile(uidString);
        profileResponseCall.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                ProfileResponse profileResponse = response.body();
                if (profileResponse != null) {
                    BasicFunction.showImage2(RetroClass.USER_IMG_PATH + profileResponse.img,getApplicationContext(),userImageView,progressBar);
                }else {
                    //
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                //
            }
        });
    }

    private void GetCartTotal(){
        ApiService cartService = RetroClass.getApiService();
        Call<CartList> cartListCall = cartService.getCart(uidString);
        cartListCall.enqueue(new Callback<CartList>() {
            @Override
            public void onResponse(Call<CartList> call, Response<CartList> response) {
                List<CartResponse> cartResponseList = null;
                if (response.body() != null) {
                    cartResponseList = response.body().getCartResponseList();
                    cartcount = cartResponseList.size();
                    if(cartResponseList.size() > 0){
                        if (textCartItemCount != null) {
                            textCartItemCount.setText(cartcount+"");
                            if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                textCartItemCount.setVisibility(View.VISIBLE);
                            }
                        }
                    }else {
                        if (textCartItemCount != null) {
                            if (cartcount == 0) {
                                if (textCartItemCount.getVisibility() != View.GONE) {
                                    textCartItemCount.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CartList> call, Throwable t) {

            }
        });
    }

    private void RegisterFcm(){

        ApiService apiService = RetroClass.getApiService();
        Call<SignupResponse> fcmResponseCall = apiService.registerFcm(fcmString,mobileString);
        fcmResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if (response.body() != null) {
                    SignupResponse signupResponse = response.body();
                    if (Objects.requireNonNull(signupResponse).success == 1) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(GetPrefs.PREFS_REGISTER_FCM, fcmString);
                 //       BasicFunction.showToast(getApplicationContext(),fcmString) ;
                        editor.commit();
                    } else {
              //  BasicFunction.showToast(getApplicationContext(),signupResponse.success+" 1");
                    }
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
              //  BasicFunction.showToast(getApplicationContext(),t.getMessage()+" 2");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetCartTotal();
        GetProfileData();
        String loginSession = sharedpreferences.getString(GetPrefs.PREFS_SESSION,"");
        if (loginSession.equalsIgnoreCase("2")){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(GetPrefs.PREFS_SESSION,"1");
            editor.commit();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            //called when app goes to background
            String loginSession = sharedpreferences.getString(GetPrefs.PREFS_SESSION,"");
            if (loginSession.equalsIgnoreCase("1")){
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(GetPrefs.PREFS_SESSION,"2");
                editor.commit();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        final MenuItem menuItem = menu.findItem(R.id.cart);

        menuItem.setActionView(R.layout.actionbarlayout);

        View actionView = MenuItemCompat.getActionView(menuItem);

        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                // Intent homeIntent = new Intent(this, MainActivity.class);
                // homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // startActivity(homeIntent);
                finish();
                return true;
            case R.id.play:
                Intent vintent = new Intent(getApplicationContext(),VideosActivity.class);
                startActivity(vintent);
                return true;
            case R.id.cart:
                Intent intent = new Intent(getApplicationContext(),CartActivity.class);
                startActivity(intent);
                return true;
            case R.id.search:
                Intent searchIntent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(searchIntent);
                return true;
            default:
                return (super.onOptionsItemSelected(item));
        }
    }
}
