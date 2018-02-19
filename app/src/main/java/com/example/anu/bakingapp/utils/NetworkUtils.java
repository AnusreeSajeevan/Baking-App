package com.example.anu.bakingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.net.MalformedURLException;

public class NetworkUtils {

    /**
     * method to check network is available or not
     * @param context the context instance
     * @return true if network is available, otherwise return false
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static Uri buildVideoUri(String videoUrl) throws MalformedURLException {
        return Uri.parse(videoUrl).buildUpon().build();
    }
}
