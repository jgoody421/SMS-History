package com.example.arek.smshistory;

/**
 * Created by Arek on 2015-06-30.
 */
public class SMS {
    private String body;
    private String date;
    private String type;

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }
}
