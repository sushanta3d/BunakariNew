package com.bunakari.sambalpurifashion.view;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.SignupResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAllReviewActivity extends AppCompatActivity {

    TextView titleTextView,extraTextView;
    Spinner reasonSpinner;
    EditText descEditText;
    Button submitButton;
    String uid,from,spinString = "",id1,id2,id3,rateString = "",ratingMsg ="";
    String[] allArray = null;
    Dialog dialog;
    Call<SignupResponse> detailsListCall;
    SharedPreferences sharedPreferences;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_all_review);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        initUi();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingBar.setRating(v);
                rateString = String.valueOf(v);
            }
        });

        submitButton.setOnClickListener(view -> {
            if (from.equalsIgnoreCase("0") || from.equalsIgnoreCase("1")){
                if (spinString.length() != 0) {
                    if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
                        SubmitFeedback();
                        dialog.show();
                    } else {
                        BasicFunction.showSnackbar(EditAllReviewActivity.this, "No internet connection,Please try again..!!");
                    }
                }else {
                    BasicFunction.showToast(getApplicationContext(),"Please Select Any Reason");
                }
            }else {
                if (rateString.length() != 0) {
                    if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
                        SubmitFeedback();
                        dialog.show();
                    } else {
                        BasicFunction.showSnackbar(EditAllReviewActivity.this, "No internet connection,Please try again..!!");
                    }
                }else {
                    BasicFunction.showToast(getApplicationContext(),"Please Select Any Rating");
                }
            }
        });

    }

    private void initUi() {
        titleTextView = findViewById(R.id.titleTextView);
        reasonSpinner = findViewById(R.id.reasonSpinner);
        descEditText = findViewById(R.id.descEditText);
        submitButton = findViewById(R.id.submitButton);
        ratingBar = findViewById(R.id.rateBar);
        extraTextView = findViewById(R.id.extraTextView);
        dialog = new Dialog(EditAllReviewActivity.this);
        dialog.setContentView(R.layout.progress_bar);

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        uid = sharedPreferences.getString(GetPrefs.PREFS_UID,"");

        from = getIntent().getStringExtra("from");

        if (from.equalsIgnoreCase("0")){
            id1 = getIntent().getStringExtra("crid1");
            id2 = getIntent().getStringExtra("crid2");
            id3 = getIntent().getStringExtra("crid3");
            titleTextView.setText("Cancel Order Item");
            allArray =  getResources().getStringArray(R.array.cancelArray);
            getSupportActionBar().setTitle("Cancel Product");
        }else if (from.equalsIgnoreCase("1")){
            id1 = getIntent().getStringExtra("crid1");
            id2 = getIntent().getStringExtra("crid2");
            id3 = getIntent().getStringExtra("crid3");
            titleTextView.setText("Return Order Item");
            allArray =  getResources().getStringArray(R.array.returnArray);
            getSupportActionBar().setTitle("Return Product");
        }else if (from.equalsIgnoreCase("2")){
            id1 = getIntent().getStringExtra("crid1");
            id2 = getIntent().getStringExtra("crid2");
            id3 = getIntent().getStringExtra("crid3");
            ratingMsg = getIntent().getStringExtra("msg");
            titleTextView.setText("Write Review");
            reasonSpinner.setVisibility(View.GONE);
            ratingBar.setVisibility(View.VISIBLE);
            extraTextView.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Review Product");
            if (id2 != null) {
                ratingBar.setRating(Float.parseFloat(id2));
            }
            if (ratingMsg != null){
                descEditText.setText(ratingMsg);
            }
        }

        if (from.equalsIgnoreCase("0") || from.equalsIgnoreCase("1")) {
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this, R.layout.spinner_item, allArray) {
                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };

            arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            reasonSpinner.setAdapter(arrayAdapter);

            reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItemText = (String) parent.getItemAtPosition(position);
                    // If user change the default selection
                    // First item is disable and it is used for hint
                    if (position > 0) {
                        // Notify the selected item text
                        spinString = selectedItemText;
                    } else {
                        spinString = "";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void SubmitFeedback(){
        Log.e("FeedbackData",id3+" "+id2+" "+id1+" "+uid+" "+spinString);
        ApiService orderService = RetroClass.getApiService();
        if (from.equalsIgnoreCase("0")){
            detailsListCall = orderService.cancelOrder(id3,id1,uid,spinString,descEditText.getText().toString(),id2);
        }else if (from.equalsIgnoreCase("1")){
            detailsListCall = orderService.returnOrder(id3,id1,uid,spinString,descEditText.getText().toString(),id2);
        }else if (from.equalsIgnoreCase("2")){
            detailsListCall = orderService.applyRating(uid,id3,id1,rateString,descEditText.getText().toString());
        }
        detailsListCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                dialog.dismiss();
                SignupResponse signupResponse = response.body();
                if (signupResponse != null){
                    if (signupResponse.success == 1){
                        if (from.equalsIgnoreCase("0")){
                            BasicFunction.showToast(getApplicationContext(),"Cancel request submitted successfully...");
                        }else if (from.equalsIgnoreCase("1")){
                            BasicFunction.showToast(getApplicationContext(),"Return request submitted successfully...");
                        }else if (from.equalsIgnoreCase("2")){
                            BasicFunction.showToast(getApplicationContext(),"Thanks For Rating...");
                        }
                        finish();
                    }else {
                        BasicFunction.showToast(getApplicationContext(),"Something went wrong,Please try again...");
                    }
                }else {
                    BasicFunction.showToast(getApplicationContext(),"Something went wrong,Please try again...");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                dialog.dismiss();
                BasicFunction.showToast(getApplicationContext(),"Something went wrong,Please try again...");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                // Intent homeIntent = new Intent(this, MainActivity.class);
                // homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // startActivity(homeIntent);
                finish();
                return true;
            default:
                return (super.onOptionsItemSelected(item));
        }
    }
}
