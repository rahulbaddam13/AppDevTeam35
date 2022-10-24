package edu.northeastern.numad22fa_mrp;


import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class AtYourService extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TextView displayDate;
    String datetxt;
    long dayBetween;
    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_your_service);

        displayDate = (TextView) findViewById(R.id.selectDate);
        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AtYourService.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year,
                        month, day);
                long now = System.currentTimeMillis() - 1000;
                dialog.getDatePicker().setMinDate(now);
                dialog.getDatePicker().setMaxDate(now + (1000 * 60 * 60 * 24 * 5));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                String selected = year + "-" + month + "-" + day + "T23:59:59";

                try {
                    LocalDateTime date2 = LocalDateTime.parse(selected, dtf);
                    LocalDateTime date1 = LocalDateTime.of(LocalDate.now(),
                            LocalTime.of(23,59,59));
                    dayBetween = Duration.between(date1, date2).toDays();
                    Log.d(TAG, "onDateSet: " + dayBetween);

                } catch(ParseException e) {
                    e.printStackTrace();
                }




                datetxt = getMonthFormat(month) + " " + day + ", " + year;
                displayDate.setText(datetxt);

            }
        };

    }

    public void openWeather(View view) {
        int theId = view.getId();
        if (theId == R.id.weather) {
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("dateString", datetxt);
            intent.putExtra("daysFuture", dayBetween);
            startActivity(intent);
        }
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JuN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        // Should not reach here
        return "JAN";

    }

}