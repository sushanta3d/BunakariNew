package com.bunakari.sambalpurifashion.view;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.ProfileResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyDashboardFragment extends Fragment implements View.OnClickListener {

    private TextView userNameTextView,referalIdTextView,totalMemberTextView,totalAmountTextView,supportTextView,howWorkTextView,notfoundTextView;
    private RelativeLayout networkLayout,balanceLayout,transactionLayout,inviteLayout;
    private ProgressBar progressBar;
    private ScrollView layout;
    private SharedPreferences sharedPreferences;
    private String uidString,nameString,refIdString,appPackageName;

    public MyDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_my_dashboard, container, false);

        initUi(v);

        return v;
    }

    private void initUi(View v) {

        userNameTextView = v.findViewById(R.id.userNameTextView);
        referalIdTextView = v.findViewById(R.id.referIdTextView);
        totalMemberTextView = v.findViewById(R.id.totalMemberTextView);
        totalAmountTextView = v.findViewById(R.id.totalAmoountTextView);
        supportTextView = v.findViewById(R.id.supportTextView);
        howWorkTextView = v.findViewById(R.id.howWorkTextView);
        notfoundTextView = v.findViewById(R.id.notfoundTextView);

        networkLayout = v.findViewById(R.id.networkLayout);
        balanceLayout = v.findViewById(R.id.balanceLayout);
        transactionLayout = v.findViewById(R.id.transactionLayout);
        inviteLayout = v.findViewById(R.id.inviteLayout);

        progressBar = v.findViewById(R.id.progressBar);
        layout = v.findViewById(R.id.scrollLayout);

        supportTextView.setOnClickListener(this);
        howWorkTextView.setOnClickListener(this);
        networkLayout.setOnClickListener(this);
        balanceLayout.setOnClickListener(this);
        transactionLayout.setOnClickListener(this);
        inviteLayout.setOnClickListener(this);

        appPackageName = getActivity().getPackageName();

        sharedPreferences = getActivity().getSharedPreferences(GetPrefs.PREFS_NAME,getActivity().MODE_PRIVATE);
        uidString = sharedPreferences.getString(GetPrefs.PREFS_UID,"");
        nameString = sharedPreferences.getString(GetPrefs.PREFS_UNAME,"");
        refIdString = sharedPreferences.getString(GetPrefs.PREFS_RFERRAL_ID,"");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GetPrefs.PREFS_SESSION,"1");
        editor.commit();

    }

    private void GetProfileData() {

        ApiService apiService = RetroClass.getApiService();
        Call<ProfileResponse> profileResponseCall = apiService.getProfile(uidString);
        profileResponseCall.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                progressBar.setVisibility(View.GONE);
                ProfileResponse profileResponse = response.body();
                if (profileResponse != null) {
                    layout.setVisibility(View.VISIBLE);
                    setViewData(profileResponse);
                }else {
                    BasicFunction.showDailogBox(getActivity(),"Oops Something went wrong, Please try again..!!");
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                BasicFunction.showDailogBox(getActivity(),"Oops Something went wrong, Please try again..!!");
            }
        });
    }

    private void setViewData(ProfileResponse profileResponse) {
        userNameTextView.setText(nameString);
        referalIdTextView.setText("Ref Id : "+refIdString);
        totalMemberTextView.setText("Total Members : "+profileResponse.mynetwork);
        totalAmountTextView.setText("Earning Amount : \u20B9 "+profileResponse.earnamount);
        howWorkTextView.setText("Cashback Amount : \u20B9 "+profileResponse.returnamount);
    }

    public void shareus() {
        String appRefCode = sharedPreferences.getString(GetPrefs.PREFS_APP_RFERRAL_CODE,"");
        String shareBody = "Join me on Free India Market, Enter my code " + appRefCode + " to earn ₹100 back.\nhttps://play.google.com/store/apps/details?id="
                + appPackageName;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources()
                .getString(R.string.app_name)));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (BasicFunction.isNetworkAvailable(getActivity())) {
            GetProfileData();
        }else {
            BasicFunction.showSnackbar(getActivity(),"No internet connection,Please try again..!!");
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.supportTextView){

        }else if (view.getId() == R.id.howWorkTextView){

        }else if (view.getId() == R.id.networkLayout){
            Intent intent = new Intent(getActivity(),MyNetworkActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.balanceLayout){
            Intent intent = new Intent(getActivity(),MyBalanceActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.transactionLayout){
            Intent intent = new Intent(getActivity(),MyTransactionActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.inviteLayout){
            shareus();
        }
    }
}
