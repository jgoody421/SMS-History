package com.example.arek.smshistory;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MessagesActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        String number = getIntent().getStringExtra("Number");
        PhoneNumberUtils.formatNumber(number);
        List<SMS> messages = new ArrayList<>();

        messages.addAll(getReceivedSMS(number));
        messages.addAll(getSentSMS(number));
        Collections.sort(messages, new SMSComparator());

        SMSAdapter adapter = new SMSAdapter(this, messages);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<SMS> getReceivedSMS(String number) {
        Uri mSmsinboxQueryUri = Uri.parse("content://sms/inbox");
        Cursor cursor1 = getContentResolver().query(mSmsinboxQueryUri, new String[]{"_id", "thread_id", "address", "person", "date", "body", "type"}, null, null, null);
        startManagingCursor(cursor1);
        String[] columns = new String[] { "address", "person", "date", "body","type" };
        List<SMS> messages = new ArrayList<>();
        if (cursor1.getCount() > 0) {
            while (cursor1.moveToNext()) {
                String address = cursor1.getString(cursor1.getColumnIndex(columns[0]));
                number = number.replaceAll("[^0-9]", "");

                if (PhoneNumberUtils.compare(address, number)) {
                    SMS sms = new SMS();
                    Date date = new Date(cursor1.getLong(cursor1.getColumnIndex(columns[2])));
                    String formattedDate = new SimpleDateFormat("yy/MM/dd HH:mm:ss").format(date);
                    String body = cursor1.getString(cursor1.getColumnIndex(columns[3]));
                    sms.setBody(body);
                    sms.setDate(String.valueOf(formattedDate));
                    sms.setType("received");
                    messages.add(sms);
                }
            }
        }
        return messages;
    }


    public ArrayList<SMS> getSentSMS(String number) {
        Uri sentSMSUri = Uri.parse("content://sms/sent");
        Cursor cursor = getContentResolver().query(sentSMSUri, new String[]{"_id", "thread_id", "address", "person", "date", "body", "type"}, null, null, null);
        startManagingCursor(cursor);
        String[] columns = new String[] { "address", "person", "date", "body","type" };
        ArrayList<SMS> messages = new ArrayList<>();

        while(cursor.moveToNext()) {
            String address = cursor.getString(cursor.getColumnIndex(columns[0]));

            number = number.replaceAll("[^0-9]", "");
            if (PhoneNumberUtils.compare(address, number)) {
                SMS sms = new SMS();
                sms.setBody(cursor.getString(cursor.getColumnIndex(columns[3])));
                Date date = new Date(cursor.getLong(cursor.getColumnIndex(columns[2])));
                String formatedDate = new SimpleDateFormat("yy/MM/dd HH:mm:ss").format(date);
                sms.setDate(formatedDate);
                sms.setType("sent");

                messages.add(sms);
            }
        }
        return messages;
    }
}
