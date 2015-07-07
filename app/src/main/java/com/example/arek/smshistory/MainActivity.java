package com.example.arek.smshistory;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ListActivity {

    private final String[] URIS = { "content://sms/inbox", "content://sms/sent" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        List<Contact> list = new ArrayList<>();
        for (String s : URIS) {
            Uri uri = Uri.parse(s);
            Cursor c = getSMS(uri);
            startManagingCursor(c);
            List<Contact> contacts = getContacts(c);
            for (Contact contact : contacts) {
                if (!isOnList(contact.getNumber(), list)) {
                    list.add(contact);
                }
            }
        }
        ContactAdapter adapter = new ContactAdapter(this, list);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick (ListView listView, View view, int pos, long id) {
        super.onListItemClick(listView, view, pos, id);

        Contact contact = (Contact) getListAdapter().getItem(pos);
        Context context = getApplicationContext();

        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra("Number", contact.getNumber());
        startActivity(intent);
    }

    public Cursor getSMS(Uri uri) {
        String[] projection = new String[] {Telephony.Sms._ID, Telephony.Sms.ADDRESS};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = Telephony.Sms.DEFAULT_SORT_ORDER;
        return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }

    private List<Contact> getContacts(Cursor numbers) {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        List<String> uniqueNumbers = getUniqueNumbers(numbers);
        List<Contact> contacts = new ArrayList<>();

        while (phones.moveToNext()) {
            String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            if(name == null) {
                name = number;
                contacts.add(new Contact(name, number));
            }
            for(String uniqueNumber : uniqueNumbers) {
                if(PhoneNumberUtils.compare(uniqueNumber, number)) {
                    contacts.add(new Contact(name, number));
                }
            }
        }
        return contacts;
    }

    public List<String> getUniqueNumbers(Cursor c) {
        List<String> uniqueNumbers = new ArrayList<>();

        c.moveToFirst();
        String number = (c.getString(c.getColumnIndex("address")));
        uniqueNumbers.add(number);

        while(c.moveToNext()) {
            number = (c.getString(c.getColumnIndex("address")));
            if (!uniqueNumbers.contains(number)) {
                uniqueNumbers.add(number);
            }
        }
        return uniqueNumbers;
    }

    private boolean isOnList(String number, List<Contact> list) {
        for(Contact contact : list) {
            if(contact.getNumber().equals(number))
                return true;
        }
        return false;
    }
}
