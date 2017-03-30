package com.thunder.simplytweet.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by anlinsquall on 25/3/17.
 */

public class Utils {
    public static String getRelativeTimeAgo(String rawJsonDate){
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(twitterFormat);

        simpleDateFormat.setLenient(true);

        String relaviteDate = "";

        long dateMillis = 0;
        try {
            dateMillis = simpleDateFormat.parse(rawJsonDate).getTime();
            relaviteDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relaviteDate;
    }

    public static String getDateTime(String rawJsonDate){
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        String displayFormat = "HH:mm a - dd MMM yyyy";
        String dateString="";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(twitterFormat);

        try {
            Date date = simpleDateFormat.parse(rawJsonDate);
            simpleDateFormat = new SimpleDateFormat(displayFormat);
            dateString = simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }
}
