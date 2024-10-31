package com.example.contentprovider;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
public class ShowAllContactActivity extends Activity {
    private ListView listViewSMS;
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_contact);

        listViewSMS = findViewById(R.id.listViewSMS);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showSMS();
    }
    public void showSMS() {
        ArrayList<String> smsList = new ArrayList<>();
        Uri uri = Telephony.Sms.Inbox.CONTENT_URI; // Truy cập vào SMS Inbox
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.Inbox.BODY)); // Lấy nội dung SMS
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.Inbox.ADDRESS)); // Lấy địa chỉ gửi SMS
                smsList.add("From: " + address + "\n" + "Message: " + body); // Thêm vào danh sách SMS
            } while (cursor.moveToNext());

            cursor.close();
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, smsList);
        listViewSMS.setAdapter(adapter);
    }
}
