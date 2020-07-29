package com.bunakari.sambalpurifashion.view;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.SingleResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivacyPolicyFragment extends Fragment {

    private WebView privacyWebView;
    private TextView notfoundTextView;
    private ProgressBar progressBar;

    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_privacy_policy, container, false);

        initUi(v);

        if (BasicFunction.isNetworkAvailable(getActivity())) {
            GetPrivacyData();
        }else {
            BasicFunction.showSnackbar(getActivity(),"No internet connection,Please try again..!!");
        }

        return v;
    }

    private void initUi(View v) {
        privacyWebView = v.findViewById(R.id.privacyWebView);
        notfoundTextView = v.findViewById(R.id.notfoundTextView);
        progressBar = v.findViewById(R.id.progressBar);
    }

    private void GetPrivacyData(){
        ApiService privacyService = RetroClass.getApiService();
        Call<SingleResponse> privacyStringCall = privacyService.getPrivacy();
        privacyStringCall.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                progressBar.setVisibility(View.GONE);
                SingleResponse singleResponse = response.body();
                Log.d("privacyResponse",singleResponse.content+" ");
                if (singleResponse != null) {
                    privacyWebView.loadData(singleResponse.content,"text/html",null);
                }else {
                    notfoundTextView.setText("Sorry No data found");
                    notfoundTextView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                notfoundTextView.setText("Sorry No data found2\n"+t.getMessage());
                notfoundTextView.setVisibility(View.VISIBLE);
            }
        });
    }
}
