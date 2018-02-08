package com.example.anu.bakingapp.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.anu.bakingapp.data.Step;
import com.example.anu.bakingapp.utils.BakingJsonUtils;

import org.json.JSONException;

import java.util.List;

public class StepsListLiveData extends LiveData<List<Step>> {
    private final Context context;
    public StepsListLiveData(Context context, String steps) {
        this.context = context;
        loadSteps(steps);
    }
    private void loadSteps(String steps) {
        new AsyncTask<String, Void, List<Step>>(){

            @Override
            protected List<Step> doInBackground(String... strings) {
                try {
                    List<Step> list = BakingJsonUtils.parseSteps(strings[0]);
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Step> stepList) {
                setValue(stepList);
            }
        }.execute(steps);
    }
}
