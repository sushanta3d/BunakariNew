package com.bunakari.sambalpurifashion.view;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.ContactUsResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {

    private TextView personTextView,addressTextView,mobileTextView,emailTextView,websiteTextView;
    private ImageView mapImageView;
    private RelativeLayout personLayout,addressLayout,mobileLayout,emailLayout,websiteLayout;
    private ProgressBar progressBar;

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contact_us, container, false);

        initUi(v);

        if (BasicFunction.isNetworkAvailable(getActivity())) {
            GetContactData();
        }else {
            BasicFunction.showSnackbar(getActivity(),"No internet connection,Please try again..!!");
        }

        return v;
    }

    private void initUi(View v) {
        personTextView = v.findViewById(R.id.personValueTextView);
        addressTextView = v.findViewById(R.id.addressValueTextView);
        mobileTextView = v.findViewById(R.id.mobileValueTextView);
        emailTextView = v.findViewById(R.id.emailValueTextView);
        websiteTextView = v.findViewById(R.id.webValueTextView);

        mapImageView = v.findViewById(R.id.mapImgView);

        personLayout = v.findViewById(R.id.nameLayout);
        addressLayout = v.findViewById(R.id.addressLayout);
        mobileLayout = v.findViewById(R.id.mobileLayout);
        emailLayout = v.findViewById(R.id.emailLayout);
        websiteLayout = v.findViewById(R.id.webLayout);
        
        progressBar = v.findViewById(R.id.progressBar);

    }

    private void GetContactData(){
        ApiService contactService = RetroClass.getApiService();
        Call<ContactUsResponse> contactResponseCall = contactService.getContactInfo();
        contactResponseCall.enqueue(new Callback<ContactUsResponse>() {
            @Override
            public void onResponse(Call<ContactUsResponse> call, Response<ContactUsResponse> response) {
                progressBar.setVisibility(View.GONE);
                final ContactUsResponse contactResponse = response.body();
                if (contactResponse != null){
                    if (contactResponse.address.length() == 0) {
                        addressLayout.setVisibility(View.GONE);
                        addressTextView.setClickable(false);
                        mapImageView.setVisibility(View.GONE);
                    } else {
                        addressLayout.setVisibility(View.VISIBLE);
                        addressTextView.setText(Html.fromHtml(contactResponse.address));
                        addressTextView.setClickable(true);
                    }

                    if (contactResponse.cname.length() == 0) {
                        personLayout.setVisibility(View.GONE);
                    } else {
                        personLayout.setVisibility(View.VISIBLE);
                        personTextView.setText(contactResponse.cname);

                    }

                    if (contactResponse.email.length() == 0 && contactResponse.email1.length() == 0) {
                        emailLayout.setVisibility(View.GONE);
                    } else {
                        emailLayout.setVisibility(View.VISIBLE);
                        if (contactResponse.email.length() != 0 && contactResponse.email1.length() != 0) {
                            emailTextView.setText(contactResponse.email + " / " + contactResponse.email1);
                        } else {
                            if (contactResponse.email.length() != 0) {
                                emailTextView.setText(contactResponse.email);
                            }
                            if (contactResponse.email1.length() != 0) {
                                emailTextView.setText(contactResponse.email1);
                            }
                        }
                    }

                    if (contactResponse.mob.length() == 0 && contactResponse.mob1.length() == 0) {
                        mobileLayout.setVisibility(View.GONE);
                    } else {
                        mobileLayout.setVisibility(View.VISIBLE);
                        if (contactResponse.mob.length() != 0 && contactResponse.mob1.length() != 0) {
                            mobileTextView.setText(contactResponse.mob + " / " + contactResponse.mob1);
                        } else {
                            if (contactResponse.mob.length() != 0) {
                                mobileTextView.setText(contactResponse.mob);
                            }
                            if (contactResponse.mob1.length() != 0) {
                                mobileTextView.setText(contactResponse.mob1);
                            }
                        }
                    }

                    if (contactResponse.website.length() == 0) {
                        websiteLayout.setVisibility(View.GONE);
                    } else {
                        websiteTextView.setText(contactResponse.website);
                    }

                    if (contactResponse.lat.length() == 0 || contactResponse.lang.length() == 0) {
                        addressTextView.setClickable(false);
                    } else {
                        addressTextView.setClickable(true);
                        Toast.makeText(getActivity(),
                                "Click On Address for directions",
                                Toast.LENGTH_SHORT).show();
                    }

                    addressTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double latitude = Double.parseDouble(contactResponse.lat);
                            double longitude = Double.parseDouble(contactResponse.lang);

                            String label = getResources().getString(R.string.app_name);
                            String uri = String.format(Locale.ENGLISH,
                                    "http://maps.google.com/maps?daddr=%f,%f (%s)",
                                    latitude, longitude, label);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            intent.setClassName("com.google.android.apps.maps",
                                    "com.google.android.maps.MapsActivity");
                            startActivity(intent);
                        }
                    });

                    mapImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double latitude = Double.parseDouble(contactResponse.lat);
                            double longitude = Double.parseDouble(contactResponse.lang);

                            String label = getResources().getString(R.string.app_name);
                            String uri = String.format(Locale.ENGLISH,
                                    "http://maps.google.com/maps?daddr=%f,%f (%s)",
                                    latitude, longitude, label);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            intent.setClassName("com.google.android.apps.maps",
                                    "com.google.android.maps.MapsActivity");
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ContactUsResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
