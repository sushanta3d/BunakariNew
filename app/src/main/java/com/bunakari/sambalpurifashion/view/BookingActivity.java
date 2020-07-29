package com.bunakari.sambalpurifashion.view;

import android.content.ComponentCallbacks2;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class BookingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    DatePickerDialog datePickerDialog ;
    TimePickerDialog timePickerDialog ;
    String date,time,bookingam,walletamount,tSize,uId,pId,amount,cId;
    int Year, Month, Day, Hour, Minute;
    Calendar calendar ;
  private   TextView text_hint,text_bAmount,btnBook;
   private    Button  button_address;
    private SharedPreferences sharedPreferences;
    private String uidString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        uidString = sharedPreferences.getString(GetPrefs.PREFS_UID,"");
        button_address = (Button) findViewById(R.id.btnaddaddress);
        btnBook = (TextView) findViewById(R.id.btnBook);
        text_hint = (TextView) findViewById(R.id.text_hint);
        text_bAmount = (TextView) findViewById(R.id.text_bAmount);
        Intent intent = getIntent();
        bookingam = intent.getStringExtra("bookingamt");
        walletamount = intent.getStringExtra("walletamount");

        cId = intent.getStringExtra("cId");
        if(cId.equalsIgnoreCase("2")) {
            tSize = intent.getStringExtra("tSize");
            pId = intent.getStringExtra("pId");
            amount = intent.getStringExtra("amount");
            uId = intent.getStringExtra("uId");
            btnBook.setVisibility(View.VISIBLE);
            text_hint.setVisibility(View.VISIBLE);
            button_address.setVisibility(View.GONE);
            text_bAmount.setVisibility(View.VISIBLE);
            text_bAmount.setText("Booking Amount : " + "\u20B9 " + bookingam);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Booking Information");

        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        Hour = calendar.get(Calendar.HOUR_OF_DAY);
        Minute = calendar.get(Calendar.MINUTE);

        //getWeekendDays();

        final Button button_datepicker = (Button) findViewById(R.id.button_datepicker);
        button_datepicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datePickerDialog = DatePickerDialog.newInstance(BookingActivity.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setTitle("Date Picker");

                // Setting Min Date to today date
                Calendar min_date_c = Calendar.getInstance();
                datePickerDialog.setMinDate(min_date_c);

                // Setting Max Date to next 2 years
                Calendar max_date_c = Calendar.getInstance();
                max_date_c.set(Calendar.YEAR, Year+2);
                datePickerDialog.setMaxDate(max_date_c);

                //Disable all SUNDAYS and SATURDAYS between Min and Max Dates
      /*          for (Calendar loopdate = min_date_c; min_date_c.before(max_date_c); min_date_c.add(Calendar.DATE, 1), loopdate = min_date_c) {
                    int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
                    if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                        Calendar[] disabledDays =  new Calendar[1];
                        disabledDays[0] = loopdate;
                        datePickerDialog.setDisabledDays(disabledDays);
                    }
                }*/


                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialogInterface) {

                        Toast.makeText(BookingActivity.this, "Datepicker Canceled", Toast.LENGTH_SHORT).show();
                    }
                });

                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
            }
        });


        final Button button_timepicker = (Button) findViewById(R.id.button_timepicker);
        button_timepicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timePickerDialog = TimePickerDialog.newInstance(BookingActivity.this, Hour, Minute,false );
                timePickerDialog.setThemeDark(false);
                //timePickerDialog.showYearPickerFirst(false);
                timePickerDialog.setTitle("Time Picker");

                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialogInterface) {

                        Toast.makeText(BookingActivity.this, "Timepicker Canceled", Toast.LENGTH_SHORT).show();
                    }
                });

                timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
            }
        });



        button_address.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (date != null)     //size as per your requirement
                {
                    Intent intent = new Intent(getApplicationContext(), DelieveryAddressActivity.class);
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    intent.putExtra("bookingamt", bookingam);
                    intent.putExtra("walletamount", walletamount);
                    startActivity(intent);

                }
                else{


                    Toast.makeText(BookingActivity.this, "Select Booking Date & Time", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (date != null)     //size as per your requirement
                {
                    Intent intent = new Intent(getApplicationContext(), PaymentMethodActivity.class);
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    intent.putExtra("bookingamt", bookingam);
                    startActivity(intent);

                }
                else{


                    Toast.makeText(BookingActivity.this, "Select Booking Date & Time", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int Year, int Month, int Day) {

        date = "Date: "+Day+"/"+(Month+1)+"/"+Year;

        Toast.makeText(BookingActivity.this, date, Toast.LENGTH_LONG).show();

        TextView text_datepicker = (TextView)findViewById(R.id.text_datepicker);
        text_datepicker.setText(date);

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

      time = "Time: "+hourOfDay+"h"+minute+"m"+second;

        Toast.makeText(BookingActivity.this, time, Toast.LENGTH_LONG).show();


        TextView text_timepicker = (TextView)findViewById(R.id.text_timepicker);
        text_timepicker.setText(time);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BasicFunction.isNetworkAvailable(getApplicationContext())) {

        }else {
            BasicFunction.showSnackbar(BookingActivity.this,"No internet connection,Please try again..!!");
        }
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