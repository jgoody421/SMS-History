package com.example.arek.smshistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Arek on 2015-06-30.
 */
public class SMSAdapter extends ArrayAdapter<SMS> {

    public SMSAdapter(Context context, List<SMS> resource) {
        super(context, 0, resource);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SMS sms = getItem(position);

        if(convertView == null && sms.getType().equals("received")) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sms_received, parent, false);
            TextView textView3 = (TextView) convertView.findViewById(R.id.textView3);
            TextView textView4 = (TextView) convertView.findViewById(R.id.textView4);
            textView3.setText(sms.getDate());
            textView4.setText(sms.getBody());

        }
        else if(convertView == null && sms.getType().equals("sent")) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sms_sent, parent, false);
            TextView textView5 = (TextView) convertView.findViewById(R.id.textView5);
            TextView textView6 = (TextView) convertView.findViewById(R.id.textView6);
            textView5.setText(sms.getDate());
            textView6.setText(sms.getBody());
        }
        return convertView;
    }
}
