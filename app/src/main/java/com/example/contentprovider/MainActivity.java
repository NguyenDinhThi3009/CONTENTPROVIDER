package com.example.contentprovider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends Activity implements OnClickListener {

    Button btnShowSMS; // Nút để truy cập SMS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowSMS = findViewById(R.id.btnShowSMS);
        btnShowSMS.setOnClickListener(this);

        // Kiểm tra và yêu cầu quyền truy cập SMS
        if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, 1);
        }
    }
    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.activity_main, menu);
        return true;
    }
    @Override
    public void onClick(View v) {
        if (v == btnShowSMS) {
            accessSMS(); // Gọi hàm để đọc SMS
        }
    }
    public void accessSMS() {
        String[] projection = new String[]{
                Telephony.Sms.ADDRESS,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE
        };
        Cursor c = getContentResolver().query(
                Telephony.Sms.CONTENT_URI,
                projection,
                null, null,
                Telephony.Sms.DATE + " DESC"
        );
        if (c != null && c.moveToFirst()) {
            ArrayList<String> smsList = new ArrayList<>();
            do {
                @SuppressLint("Range") String address = c.getString(c.getColumnIndex(Telephony.Sms.ADDRESS));
                @SuppressLint("Range") String body = c.getString(c.getColumnIndex(Telephony.Sms.BODY));
                @SuppressLint("Range") String date = c.getString(c.getColumnIndex(Telephony.Sms.DATE));

                smsList.add("From: " + address + "\n" + "Message: " + body + "\n" + "Date: " + date + "\n");
            } while (c.moveToNext());
            c.close();

            Intent intent = new Intent(this, ShowAllContactActivity.class);
            intent.putStringArrayListExtra("smsList", smsList);
            startActivity(intent);
        } else {
            Toast.makeText(this, "No SMS found", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
