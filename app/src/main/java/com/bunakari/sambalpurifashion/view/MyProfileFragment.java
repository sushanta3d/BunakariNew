package com.bunakari.sambalpurifashion.view;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bunakari.sambalpurifashion.BuildConfig;
import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.ProfileResponse;
import com.bunakari.sambalpurifashion.model.SignupResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment implements View.OnClickListener {

    private TextView nameTextView,refIdTextView,mobTextView,walletValueTextView,addMoneyTextView,addPhotoTextView;
    static TextView dobTextView;
    private EditText emailEditText,addressEditText,stateEditText,pincodeEditText;
    private Button submitButton;
    private ImageView editImageView,userImageView;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private String userid,amount;
    private ConstraintLayout layout;
    private static final long LIMIT = 10000000000L;
    private static long last = 0;
    private int SELECT_FILE = 5;
    private int REQUEST_CAMERA = 7;
    String userChoosenTask;
    Bitmap bitmap;
    String filePath = "", fileName = "",encodedString = "",imgPath;
    int PERMISSION_ALL = 1;
    Uri fileUri;
    public String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int MEDIA_TYPE_IMAGE = 1;

    private String mediaPath;

    private String mImageFileLocation = "";
    public static final String IMAGE_DIRECTORY_NAME = "MAAPP";
    private String postPath;


    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        initUi(v);

        return v;
    }

    private void initUi(View v) {

        layout = v.findViewById(R.id.mainLayout);
        layout.setVisibility(View.GONE);

        nameTextView = v.findViewById(R.id.uNameTextView);
        refIdTextView = v.findViewById(R.id.refIdTextView);
        mobTextView = v.findViewById(R.id.mobTextView);
        dobTextView = v.findViewById(R.id.dobValueTextView);
        walletValueTextView = v.findViewById(R.id.walletValueTextView);
        addMoneyTextView = v.findViewById(R.id.addMoneyTextView);
        addPhotoTextView = v.findViewById(R.id.addPhotoTextView);

        emailEditText = v.findViewById(R.id.emailEditText);
        addressEditText = v.findViewById(R.id.addressEditText);
        stateEditText = v.findViewById(R.id.stateEditText);
        pincodeEditText = v.findViewById(R.id.pincodeEditText);

        submitButton = v.findViewById(R.id.submitButton);

        progressBar = v.findViewById(R.id.progressBar);

        userImageView = v.findViewById(R.id.userImgView);
        editImageView = v.findViewById(R.id.editImgView);

        sharedPreferences = getActivity().getSharedPreferences(GetPrefs.PREFS_NAME,getActivity().MODE_PRIVATE);
        userid = sharedPreferences.getString(GetPrefs.PREFS_UID,"");

        editImageView.setOnClickListener(this);
        userImageView.setOnClickListener(this);
        dobTextView.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        addMoneyTextView.setOnClickListener(this);

        final StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    private void GetProfileData() {

        ApiService apiService = RetroClass.getApiService();
        Call<ProfileResponse> profileResponseCall = apiService.getProfile(userid);
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
        nameTextView.setText(profileResponse.name);
        refIdTextView.setText("Ref Code : "+profileResponse.appreferralcode);
        mobTextView.setText(profileResponse.mob);
        dobTextView.setText(profileResponse.dob);
        walletValueTextView.setText(profileResponse.mybalance);
        emailEditText.setText(profileResponse.email);
        addressEditText.setText(profileResponse.address);
        stateEditText.setText(profileResponse.state);
        pincodeEditText.setText(profileResponse.pincode);
        emailEditText.setEnabled(false);
        addressEditText.setEnabled(false);
        stateEditText.setEnabled(false);
        pincodeEditText.setEnabled(false);
        dobTextView.setEnabled(false);
        Picasso.with(getContext()).load(RetroClass.USER_IMG_PATH + profileResponse.img).placeholder(R.drawable.person).into(userImageView);
        userImageView.setEnabled(false);
    }

    private void selectImage() {

        if(!hasPermissions(getActivity(), PERMISSIONS)){
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, item) -> {
            //    boolean result= Utility.checkPermission(RegisterActivity.this);

            if (items[item].equals("Take Photo")) {
                userChoosenTask ="Take Photo";
                //   if(result)
                cameraIntent();

            } else if (items[item].equals("Choose from Library")) {
                userChoosenTask ="Choose from Library";
                //    if(result)
                galleryIntent();

            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        captureImage();
    }

    private void galleryIntent()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, SELECT_FILE);
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

    public boolean isValidData(){
        int count = 0;

        if (emailEditText.getText().length() != 0){
            if (isValidEmail(emailEditText.getText().toString())) {
                count++;
            }else {
                emailEditText.setError("Please enter a valid email");
            }
        }else {
            emailEditText.setError("Required Field");
        }

        if (addressEditText.getText().length() != 0){
            count++;
        }else {
            addressEditText.setError("Required Field");
        }

        if (stateEditText.getText().length() != 0){
            count++;
        }else {
            stateEditText.setError("Required Field");
        }

        if (pincodeEditText.getText().length() != 0){
            if (pincodeEditText.getText().length() == 6) {
                count++;
            }else {
                pincodeEditText.setError("Please enter a valid pincode");
            }
        }else {
            pincodeEditText.setError("Required Field");
        }


        if (count == 4){
            return true;
        }else {
            return false;
        }

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker


            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dialog;
        }

        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            // Do something with the date chosen by the user
            //datePicker.setMaxDate(System.currentTimeMillis());

            month = month + 1;

            dobTextView.setText("" + day + "-" + "" + month + "-" + "" + year);
        }
    }

    private void captureImage() {
        if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
            Intent callCameraApplicationIntent = new Intent();
            callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

            // We give some instruction to the intent to save the image
            File photoFile = null;

            try {
                // If the createImageFile will be successful, the photo file will have the address of the file
                photoFile = createImageFile();
                // Here we call the function that will try to catch the exception made by the throw function
            } catch (IOException e) {
                Logger.getAnonymousLogger().info("Exception error in generating the file");
                e.printStackTrace();
            }
            // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
            Uri outputUri = FileProvider.getUriForFile(
                    Objects.requireNonNull(getActivity()),
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile);
            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

            // The following is a new line with a trying attempt
            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Logger.getAnonymousLogger().info("Calling the camera App by intent");

            // The following strings calls the camera app and wait for his file in return.
            startActivityForResult(callCameraApplicationIntent, REQUEST_CAMERA);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    File createImageFile() throws IOException {
        Logger.getAnonymousLogger().info("Generating the image - method started");

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp;
        // Here we specify the environment location and the exact path where we want to save the so-created file
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/FIM");
        Logger.getAnonymousLogger().info("Storage directory set");

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir();

        // Here we create the file using a prefix, a suffix and a directory
        File image = new File(storageDirectory, imageFileName + ".jpg");
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation
        Logger.getAnonymousLogger().info("File name and path set");

        mImageFileLocation = image.getAbsolutePath();
        // fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application
        return image;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("error", "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + ".jpg");
        }  else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.editImgView){
            enableView();
        }else if (view.getId() == R.id.submitButton){
            if (isValidData()){
                if (BasicFunction.isNetworkAvailable(Objects.requireNonNull(getActivity()))){
                    SubmitProfileData();
                    progressBar.setVisibility(View.VISIBLE);
                }else {
                    BasicFunction.showSnackbar(getActivity(),"No Internet Connection, Please try again..!!");
                }
            }
        }else if (view.getId() == R.id.dobValueTextView){
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getFragmentManager(), "datePicker");
        }else if (view.getId() == R.id.addMoneyTextView){
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.addmoney_layout);

            final TextInputEditText amountEditText = dialog.findViewById(R.id.amountEditText);
            Button proceedButton = dialog.findViewById(R.id.submitButton);

            proceedButton.setOnClickListener(view1 -> {
                if (amountEditText.getText().toString().equalsIgnoreCase("")){
                    amountEditText.setError("Required Field..");
                }else {
                    amount = amountEditText.getText().toString();
                    Intent intent = new Intent(getActivity(),checksum.class);
                    intent.putExtra("orderid", String.valueOf(getID()));
                    intent.putExtra("amount",amountEditText.getText().toString());
                    startActivityForResult(intent,2);
                    amountEditText.setText("");
                    dialog.hide();
                }
            });
            dialog.show();
        }else if (view.getId() == R.id.userImgView){
            selectImage();
        }
    }

    public static long getID() {
        // 10 digits.
        long id = System.currentTimeMillis() % LIMIT;
        if ( id <= last ) {
            id = (last + 1) % LIMIT;
        }
        return last = id;
    }

    private void SubmitProfileData() {

        ApiService apiService = RetroClass.getApiService();
        Call<ProfileResponse> profileResponseCall = apiService.submitProfile(userid,nameTextView.getText().toString(),emailEditText.getText().toString(),
                                mobTextView.getText().toString(),dobTextView.getText().toString(),addressEditText.getText().toString(),
                                stateEditText.getText().toString(), pincodeEditText.getText().toString());
        profileResponseCall.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                progressBar.setVisibility(View.GONE);
                ProfileResponse profileResponse = response.body();
                Log.d("ProfileResponse",profileResponse.success+" ");
                if (profileResponse != null) {
                    if (profileResponse.success == 1) {
                        BasicFunction.showDailogBox(getActivity(),"Profile Updated Successfully");
                       disableView();
                    }else {
                        BasicFunction.showDailogBox(getActivity(),"Oops Something went wrong, Please try again..!!");
                    }
                }else {
                    BasicFunction.showDailogBox(getActivity(),"Oops Something went wrong, Please try again..!!");
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.d("ErrorResponse",t.getMessage()+" ");
                progressBar.setVisibility(View.GONE);
                BasicFunction.showDailogBox(getActivity(),"Oops Something went wrong, Please try again..!!");
            }
        });
    }

    private void UploadImage() {
        Map<String, RequestBody> map = new HashMap<>();
        File file = new File(postPath);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        map.put("cateimg\"; filename=\"" + file.getName() + "\"", requestBody);

        ApiService apiService = RetroClass.getApiService();
        Call<SignupResponse> profileResponseCall = apiService.uploadImg(userid,map);
        profileResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                progressBar.setVisibility(View.GONE);
                SignupResponse profileResponse = response.body();
                Log.d("ProfileResponse", Objects.requireNonNull(profileResponse).success+" ");
                if (profileResponse != null) {
                    if (profileResponse.success == 1) {
                        BasicFunction.showToast(getActivity(),"Image Uploaded Successfully");
                    }else {
                        BasicFunction.showToast(getActivity(),"Oops Something went wrong, Please try again..!!");
                    }
                }else {
                    BasicFunction.showToast(getActivity(),"Oops Something went wrong, Please try again..!!");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Log.d("ErrorResponse",t.getMessage()+" ");
                progressBar.setVisibility(View.GONE);
                BasicFunction.showToast(getActivity(),"Oops Something went wrong, Please try again..!!");
            }
        });
    }

    private void RechargeWallet(){
        ApiService walletService = RetroClass.getApiService();
        Call<SignupResponse> walletResponseCall = walletService.addMoney(userid,amount);
        walletResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                progressBar.setVisibility(View.GONE);
                SignupResponse signupResponse = response.body();
                Log.d("Walletresponse",signupResponse.success + " "+signupResponse);
                if (signupResponse != null) {
                    if (signupResponse.success == 1){

                        Intent intent = new Intent(getActivity(),StatusActivity.class);
                        intent.putExtra("from","1");
                        intent.putExtra("amount",amount);
                        startActivity(intent);

                    }else {
                        BasicFunction.showDailogBox(getActivity(),"Oops something went wrong.Please try again!!");
                    }
                }else {
                    BasicFunction.showDailogBox(getActivity(),"Oops something went wrong.Please try again!!");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                BasicFunction.showDailogBox(getActivity(),"Oops something went wrong.Please try again!!");
            }
        });
    }

    private void enableView(){
        emailEditText.setEnabled(true);
        addressEditText.setEnabled(true);
        stateEditText.setEnabled(true);
        pincodeEditText.setEnabled(true);
        dobTextView.setEnabled(true);
        submitButton.setVisibility(View.VISIBLE);
        addPhotoTextView.setVisibility(View.VISIBLE);
        editImageView.setVisibility(View.GONE);
        userImageView.setEnabled(true);
    }

    private void disableView(){
        emailEditText.setEnabled(false);
        addressEditText.setEnabled(false);
        stateEditText.setEnabled(false);
        pincodeEditText.setEnabled(false);
        dobTextView.setEnabled(false);
        submitButton.setVisibility(View.GONE);
        addPhotoTextView.setVisibility(View.GONE);
        editImageView.setVisibility(View.VISIBLE);
        userImageView.setEnabled(false);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2) {
            if (resultCode == 2) {
                int position = data.getIntExtra("status",0);
                if (position == 1) {
                    if (BasicFunction.isNetworkAvailable(getActivity())) {
                        RechargeWallet();
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        BasicFunction.showSnackbar(getActivity(), "No internet connection,Please try again..!!");
                    }
                }
            }
        }else if (requestCode == SELECT_FILE) {
            if (data != null) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                // Set the Image in ImageView for Previewing the Media
                //userImageView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));

                cursor.close();

                String img_str = BitMapToString(BitmapFactory.decodeFile(mediaPath));
                Picasso.with(getActivity()).load(img_str).into(userImageView);
                postPath = mediaPath;

                UploadImage();
            }
        }else if (requestCode == REQUEST_CAMERA){
            if (Build.VERSION.SDK_INT > 21) {

                Picasso.with(getActivity()).load(mImageFileLocation).into(userImageView);
                postPath = mImageFileLocation;

            }else{
                Picasso.with(getActivity()).load(fileUri).into(userImageView);
                postPath = fileUri.getPath();
            }

            UploadImage();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
