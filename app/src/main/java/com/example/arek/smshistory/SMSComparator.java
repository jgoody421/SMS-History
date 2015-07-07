package com.example.arek.smshistory;

import java.util.Comparator;

/**
 * Created by Arek on 2015-06-30.
 */
public class SMSComparator implements Comparator<SMS> {

    @Override
    public int compare(SMS lhs, SMS rhs) {
        return lhs.getDate().compareTo(rhs.getDate());
    }
}
