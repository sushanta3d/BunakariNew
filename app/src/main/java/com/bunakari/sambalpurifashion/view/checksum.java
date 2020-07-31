package com.bunakari.sambalpurifashion.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentCallbacks2;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.bunakari.sambalpurifashion.BuildConfig;
import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.ChecksumResponse;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;


import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class checksum extends AppCompatActivity implements PaytmPaymentTransactionCallback {
    String custid="", orderId="", mid="",amount="",items="",addId="",from;
    String varifyurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
    private ProgressDialog dialog;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);

        custid = sharedPreferences.getString(GetPrefs.PREFS_UID,"");

        Intent intent = getIntent();
        orderId = intent.getExtras().getString("orderid");
        amount = getIntent().getStringExtra("amount");
        mid = BuildConfig.MERCHANT_ID;
        dialog = new ProgressDialog(checksum.this);
        dialog.setMessage("Loading...");
        GenerateChecksum();
    }

    private void GenerateChecksum(){

        dialog.show();

        ApiService checksumService = RetroClass.getApiService();
        Call<ChecksumResponse> checksumResponseCall = checksumService.generateChecksum(mid,orderId,custid,"WAP",amount,
                "DEFAULT",varifyurl,"Retail");
        checksumResponseCall.enqueue(new Callback<ChecksumResponse>() {
            @Override
            public void onResponse(Call<ChecksumResponse> call, Response<ChecksumResponse> response) {
                dialog.dismiss();
                try {
                    ChecksumResponse checksumResponse = response.body();
                    if (checksumResponse.getCHECKSUMHASH() != null) {
                        String checkSum = checksumResponse.getCHECKSUMHASH();
                        initializeTransaction(checkSum);
                    }else {
                        BasicFunction.showDailogBox(checksum.this,"Oops something went wrong.Please try again");
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ChecksumResponse> call, Throwable t) {
                dialog.dismiss();
                BasicFunction.showDailogBox(checksum.this,"Oops something went wrong.Please try again");
            }
        });
    }

    private void initializeTransaction(String checkSumHash){

        PaytmPGService Service = PaytmPGService.getProductionService();
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", mid); //MID provided by paytm
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("CUST_ID", custid);
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", amount);
        paramMap.put("WEBSITE", "DEFAULT");
        paramMap.put("CALLBACK_URL" ,varifyurl);
        paramMap.put("CHECKSUMHASH" ,checkSumHash);
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        PaytmOrder Order = new PaytmOrder(paramMap);
        Log.e("checksum ", "param "+ paramMap.toString());
        Service.initialize(Order,null);
        // start payment service call here
        Service.startPaymentTransaction(checksum.this, true, true,
                checksum.this  );
    }

    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.d("BundleResponse",bundle+" ");
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                // json.put(key, bundle.get(key)); see edit below
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    json.put(key, JSONObject.wrap(bundle.get(key)));
                }
            } catch(JSONException e) {
                //Handle exception here
                e.printStackTrace();
            }
        }

        if (json.optString("STATUS").equalsIgnoreCase("SUCCESS")){
            Intent intent = new Intent();
            intent.putExtra("status",1);
            setResult(2,intent);
            finish();
        }else {
            Intent intent = new Intent();
            intent.putExtra("status",2);
            setResult(2,intent);
            finish();
        }

    }

    @Override
    public void networkNotAvailable() {
        AlertDialog.Builder builder = new AlertDialog.Builder(checksum.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Network Not Available");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }
    @Override
    public void clientAuthenticationFailed(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(checksum.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Authentication Failed");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }
    @Override
    public void someUIErrorOccurred(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(checksum.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(s);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }
    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(checksum.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(s);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }
    @Override
    public void onBackPressedCancelTransaction() {
        finish();
        BasicFunction.showToast(getApplicationContext(),"Cancelled By User");
    }
    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        finish();
        BasicFunction.showToast(getApplicationContext(),"Transaction Cancelled");
    }

    @Override
    protected void onResume() {
        super.onResume();
        String loginSession = sharedPreferences.getString(GetPrefs.PREFS_SESSION,"");
        if (loginSession.equalsIgnoreCase("2")){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(GetPrefs.PREFS_SESSION,"1");
            editor.commit();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            //called when app goes to background
            String loginSession = sharedPreferences.getString(GetPrefs.PREFS_SESSION,"");
            if (loginSession.equalsIgnoreCase("1")){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(GetPrefs.PREFS_SESSION,"2");
                editor.commit();
            }
        }
    }

}
