package com.example.arek.smshistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Arek on 2015-06-28.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {

    public ContactAdapter(Context context, List<Contact> resource) {
        super(context, 0, resource);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        textView.setText(contact.getName());

        return convertView;
    }
}
