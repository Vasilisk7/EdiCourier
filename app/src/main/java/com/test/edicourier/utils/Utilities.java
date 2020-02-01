package com.test.edicourier.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utilities {
    private static final Utilities ourInstance = new Utilities();

    public static Utilities getInstance() {
        return ourInstance;
    }

    private Utilities() {
    }

    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public String dateInKanadaFormat(int year, int month, int day) {
        String date = Integer.toString(year);
        if (Integer.toString(month).length() == 1) {
            date += "-" + "0" + month;
        } else {
            date += "-" + month ;
        }
        if (Integer.toString(day).length() == 1) {
            date += "-" + "0" + day;
        } else {
            date += "-" + day;
        }
        return date;
    }
}
