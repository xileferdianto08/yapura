package id.ac.umn.yapura;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
    private Button dateButton, dateButton2, btnBook;
    private TextView time_value, time_value2;
    String newDate, newDate2, newTime, newTime2;

    private static String URL_BOOK_RUANGAN = "https://yapuraapi.000webhostapp.com/yapura_api/users/check_login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_ruangan);
        initDatePicker();
        initDatePicker2();
        dateButton = findViewById(R.id.btnStartDate);
        dateButton.setText(getTodaysDate());
        dateButton2 = findViewById(R.id.btnEndDate);
        dateButton2.setText(getTodaysDate2());
        time_value = findViewById(R.id.startTime2);
        time_value2 = findViewById(R.id.EndTime2);
        btnBook = findViewById(R.id.btnBook);

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

        time_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialTimePicker.show(getSupportFragmentManager(),"Time");
                materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newHour = "";
                        String newMinute = "";
                        int Hour = materialTimePicker.getHour();
                        newHour = String.valueOf(Hour);
                        if(Hour < 10){
                            newHour = "0"+Hour;
                        }

                        int Minute = materialTimePicker.getMinute();
                        newMinute = String.valueOf(Minute);
                        if(Minute < 10){
                            newMinute = "0"+Minute;
                        }


                        time_value.setText(newHour +":"+newMinute);
                    }
                });
            }
        });

        time_value2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                materialTimePicker2.show(getSupportFragmentManager(),"Time");
                materialTimePicker2.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newHour = "";
                        String newMinute = "";
                        int Hour2 = materialTimePicker2.getHour();
                        newHour = String.valueOf(Hour2);
                        if(Hour2 < 10){
                            newHour = "0"+Hour2;
                        }

                        int Minute2 = materialTimePicker2.getMinute();
                        newMinute = String.valueOf(Minute2);
                        if(Minute2 < 10){
                            newMinute = "0"+Minute2;
                        }

                        time_value2.setText(newHour +":"+newMinute);
                    }
                });
            }
        });
        btnBook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String startDatekey = newDate;
                Log.d("date", startDatekey);
                AlertDialog.Builder dialog = isBook();
                dialog.show();
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
                newDate = makeDateFormat(day, month, year);
                String date = makeDateString(day, month, year);

                dateButton.setText(date);

            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        newDate = makeDateFormat(day, month, year);
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private void initDatePicker2() {
        DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker2, int year2, int month2, int day2) {
                month2 = month2 + 1;
                String date = makeDateString(day2, month2, year2);
                dateButton2.setText(date);
                newDate2 = makeDateFormat2(day2, month2, year2);
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

    private String makeDateFormat(int day, int month, int year) {
        return year+"-"+month+"-"+day;
    }

    private String makeDateFormat2(int day2, int month2, int year2) {
        return year2+"-"+month2+"-"+day2;
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
    }

    public void openDatePicker2(View view){
        datePickerDialog2.show();
    }

    public AlertDialog.Builder isBook(){
        AlertDialog.Builder bookDialog = new AlertDialog.Builder(this);
        bookDialog.setTitle("Booking Confirmation");
        bookDialog.setMessage("Are the data is all correct?");

        bookDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        bookDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        bookDialog.create();

        return bookDialog;
    }


}

