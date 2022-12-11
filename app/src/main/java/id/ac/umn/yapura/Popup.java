package id.ac.umn.yapura;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class Popup extends AppCompatActivity {

    private DatePickerDialog datePickerDialog, datePickerDialog2;
    private Button dateButton, dateButton2, timeButton, timeButton2;
    private TextView time_value, time_value2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        initDatePicker();
        initDatePicker2();
        dateButton = findViewById(R.id.btnStartDate);
        dateButton.setText(getTodaysDate());
        dateButton2 = findViewById(R.id.btnEndDate);
        dateButton2.setText(getTodaysDate2());
        timeButton = findViewById(R.id.btnStartTime);
        time_value = findViewById(R.id.startTime2);
        timeButton2 = findViewById(R.id.btnEndTime);
        time_value2 = findViewById(R.id.EndTime2);

        Calendar calendar = Calendar.getInstance();
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).
                setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                        .build();

        Calendar calendar2 = Calendar.getInstance();
        MaterialTimePicker materialTimePicker2 = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).
                setHour(calendar2.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar2.get(Calendar.MINUTE))
                .build();

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialTimePicker.show(getSupportFragmentManager(),"Time");
                materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int Hour = materialTimePicker.getHour();
                        int Minute = materialTimePicker.getMinute();
                        time_value.setText(Hour+ ":"+Minute);
                    }
                });
            }
        });

        timeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialTimePicker2.show(getSupportFragmentManager(),"Time");
                materialTimePicker2.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int Hour2 = materialTimePicker2.getHour();
                        int Minute2 = materialTimePicker2.getMinute();
                        time_value2.setText(Hour2+ ":"+Minute2);
                    }
                });
            }
        });

    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String getTodaysDate2() {
        Calendar cal2 = Calendar.getInstance();
        int year2 = cal2.get(Calendar.YEAR);
        int month2 = cal2.get(Calendar.MONTH);
        month2 = month2 + 1;
        int day2 = cal2.get(Calendar.DAY_OF_MONTH);
        return makeDateString2(day2, month2, year2);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private void initDatePicker2() {
        DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker2, int year2, int month2, int day2) {
                month2 = month2 + 1;
                String date = makeDateString(day2, month2, year2);
                dateButton2.setText(date);
            }
        };

        Calendar cal2 = Calendar.getInstance();
        int year2 = cal2.get(Calendar.YEAR);
        int month2 = cal2.get(Calendar.MONTH);
        int day2 = cal2.get(Calendar.DAY_OF_MONTH);

        int style2 = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog2 = new DatePickerDialog(this, style2, dateSetListener2, year2, month2, day2);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String makeDateString2(int day2, int month2, int year2) {
        return getMonthFormat(month2) + " " + day2 + " " + year2;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AGS";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";
        return "JAN";
    }

    public void openDatePicker(View view){
        datePickerDialog.show();
        datePickerDialog2.show();
    }

}

