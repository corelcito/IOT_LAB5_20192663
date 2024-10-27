package com.seguimiento_calorias;

import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class NotisActivity extends AppCompatActivity {
    ImageView btnBack;
    TextInputEditText edtMoti;
    TextInputEditText edtTime;
    MaterialButton btnSave;
    TextInputLayout tilMoti;
    TextInputLayout tilTime;
    MaterialButton btnDate;
    MaterialButton btnCancel;
    long hour;
    long min;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notis);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        NotificationScheduler.createNotificationChannel(this);
        btnBack = findViewById(R.id.btn_back);
        edtMoti = findViewById(R.id.edt_motivation);
        edtTime = findViewById(R.id.edt_min);
        btnSave = findViewById(R.id.btn_save);
        tilMoti = findViewById(R.id.til_motivation);
        tilTime = findViewById(R.id.til_min);
        btnDate = findViewById(R.id.btn_date);
        btnCancel = findViewById(R.id.btn_cancel);

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        hour = currentHour;
        min = currentMinute;
        btnDate.setText(currentHour+":"+currentMinute);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new OnTimeSelectedListener() {
                    @Override
                    public void onTimeSelected(int hourOfDay, int minute) {
                        hour = hourOfDay;
                        min = minute;
                        btnDate.setText(hourOfDay+":"+minute);

                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationScheduler.cancelAllNotifications(NotisActivity.this);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionNotifications();
                saveNoti();
            }
        });

        edtTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilTime.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtMoti.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilMoti.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkPermissionNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            } else {
                saveNoti();
            }
        }
    }

    public void showTimePickerDialog(final OnTimeSelectedListener listener) {

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(NotisActivity.this,
                (TimePicker view, int hourOfDay, int minute) -> {

                    listener.onTimeSelected(hourOfDay, minute);
                },
                currentHour,
                currentMinute,
                true);


        timePickerDialog.show();
    }

    private void saveNoti() {
        String time = edtTime.getText().toString();
        String moti = edtMoti.getText().toString();

        if (time.isEmpty()) {
            tilTime.setError("El campo no puede estar vacío");
            return;
        }

        if (moti.isEmpty()) {
            tilMoti.setError("El campo no puede estar vacío");
            return;
        }
        long startTime = calculateStartTime((int) hour,(int) min);
        NotificationScheduler.scheduleNotification(this, moti, 101, startTime,Integer.valueOf(time) * 60 * 1000);
    }

    public static long calculateStartTime(int hour, int min) {
        Calendar now = Calendar.getInstance();
        Calendar notificationTime = Calendar.getInstance();

        notificationTime.set(Calendar.HOUR_OF_DAY, hour);
        notificationTime.set(Calendar.MINUTE, min);
        notificationTime.set(Calendar.SECOND, 0);
        notificationTime.set(Calendar.MILLISECOND, 0);

        /*if (notificationTime.before(now)) {
            notificationTime.add(Calendar.DAY_OF_YEAR, 1);
        }*/

        return notificationTime.getTimeInMillis();
    }

    public interface OnTimeSelectedListener {
        void onTimeSelected(int hourOfDay, int minute);
    }
}