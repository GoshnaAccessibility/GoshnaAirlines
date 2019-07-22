package com.example.wquist.goshnaairlines;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import retrofit.RestAdapter;

public class GoshnaAirlines extends Application {
    public static final String API_ADDRESS = "https://scc-dh-testbed.lancs.ac.uk";//"http://10.0.2.2:5000";
    public static final String API_URL = "/goshna/api";

    private static Context mContext;

    private static ApiInterface mApi;
    private static SharedPreferences mPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        RestAdapter ara = new RestAdapter.Builder()
                .setEndpoint(API_ADDRESS + API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        mApi = ara.create(ApiInterface.class);

        mPreferences = mContext.getSharedPreferences(mContext.getString(R.string.preferences), Context.MODE_PRIVATE);
    }

    public static ApiInterface getApi() { return mApi; }

    public static SharedPreferences getPreferences() { return mPreferences; }
}
