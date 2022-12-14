package id.ac.umn.yapura;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookRuangan extends AppCompatActivity {

    private DatePickerDialog datePickerDialog, datePickerDialog2;
    private Button dateButton, dateButton2, btnBook;
    private TextView time_value, time_value2, namaRuang;
    private EditText capacity, necessity;
    String newDate, newDate2, newTime, newTime2;
    int userId, ruanganID;

    private static String URL_BOOK_RUANGAN = "https://yapuraapi.000webhostapp.com/yapura_api/ruangan/pinjam_ruangan.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sessions = getSharedPreferences("user_data",  Context.MODE_PRIVATE);
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
        capacity = findViewById(R.id.capacity);
        necessity = findViewById(R.id.necessity);
        namaRuang = findViewById(R.id.namaRuang);
        userId = Integer.parseInt(sessions.getString("userId", "0"));

        Intent intent = getIntent();
        String namaRuangan = intent.getStringExtra("namaRuangan");
        ruanganID = Integer.parseInt(intent.getStringExtra("ruangId"));

        namaRuang.setText(namaRuangan);


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
                        newTime = newHour +":"+newMinute;
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
                        newTime2 = newHour +":"+newMinute;
                    }
                });
            }
        });
        btnBook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String time1 = time_value.getText().toString();
                String time2 = time_value2.getText().toString();
                String capacityKey = capacity.getText().toString();
                String necessityKey = necessity.getText().toString();
                if(time1.isEmpty()){
                    time_value.setError("Start Time tidak boleh kosong");
                }else if (time2.isEmpty()){
                    time_value2.setError("End Time tidak boleh kosong");
                } else if(capacityKey.isEmpty()){
                    capacity.setError("Capacity tidak boleh kosong");
                } else if(necessityKey.isEmpty()){
                    necessity.setError("Necessity tidak boleh kosong");
                }else {
                    AlertDialog.Builder dialog = isBook(userId, ruanganID, namaRuangan,
                            newDate, newTime, newDate2,
                            newTime2, Integer.parseInt(capacityKey), necessityKey);
                    dialog.show();
                }


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
                newDate = makeDateFormat(day, month, year);
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
        newDate2 = makeDateFormat2(day2, month2, year2);
        datePickerDialog2 = new DatePickerDialog(this, style2, dateSetListener2, year2, month2, day2);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year ;
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

    public AlertDialog.Builder isBook(int userId, int ruanganID, String namaRuangan,
                                      String startDate, String startTime, String endDate, String endTime, int capacity, String necessity){
        AlertDialog.Builder bookDialog = new AlertDialog.Builder(this);
        bookDialog.setTitle("Booking "+namaRuangan+" confirmation");
        bookDialog.setMessage("Are the data is all correct?");

        bookDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                insertBooking(userId, ruanganID, namaRuangan, startDate , startTime, endDate, endTime, capacity, necessity);
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

    private void insertBooking(int userId, int ruanganID, String namaRuangan,
                               String startDate, String startTime, String endDate, String endTime, int capacity, String necessity){
        StringRequest request = new StringRequest(Request.Method.POST, URL_BOOK_RUANGAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject resp = obj.getJSONObject("server_response");

                            JSONArray keys = resp.names();

                            for (int i = 0; i < keys.length(); i++) {
                                String status = resp.getString("status").trim();

                                if(status.equals("OK")){
                                    Intent intent = new Intent(BookRuangan.this, RuanganActivity.class);
                                    startActivity(intent);

                                    Toast.makeText(BookRuangan.this, "Peminjaman "+namaRuangan+" untuk "+startDate+" pada pukul "+startTime+" telah berhasil!", Toast.LENGTH_SHORT);

                                }else if(status.equals("ROOM_BORROWED")){
                                    Toast.makeText(BookRuangan.this, "Peminjaman "+namaRuangan+" untuk "+startDate+" pada pukul "+startTime+" tidak berhasil! silahkan pilih jam dan tanggal lain", Toast.LENGTH_SHORT);
                                }else if(status.equals("EXCEED_MAX_CAPACITY")){
                                    Toast.makeText(BookRuangan.this, "Kapasitas melebihi kapasitas maksimal!", Toast.LENGTH_SHORT);
                                }else if(status.equals("FAILED") || status.equals("DB FAILED")){
                                    Toast.makeText(BookRuangan.this, "Terdapat kesalahan pada server", Toast.LENGTH_LONG).show();
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookRuangan.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                params.put("ruangId", String.valueOf(ruanganID));
                params.put("startDate", startDate);
                params.put("startTime", startTime);
                params.put("endDate", endDate);
                params.put("endTime", endTime);
                params.put("capacity", String.valueOf(capacity));
                params.put("necessity", necessity);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void toMenu(View view){
        startActivity(new Intent(BookRuangan.this, RuanganActivity.class));
    }


}

