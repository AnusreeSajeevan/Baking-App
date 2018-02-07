package com.example.anu.bakingapp.ui.recipe;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.anu.bakingapp.data.database.Ingredient;
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
        Log.d("checkSteps", steps);
        new AsyncTask<String, Void, List<Step>>(){

            @Override
            protected List<Step> doInBackground(String... strings) {
                try {
                    Log.d("checkSteps","1");
                    Log.d("checkSteps",strings[0]);
                    List<Step> list = BakingJsonUtils.parseSteps(strings[0]);
                    Log.d("checkSteps", String.valueOf(list.size()));
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Step> stepList) {
                Log.d("checkSteps","onPostExecute");
                setValue(stepList);
            }
        }.execute(steps);
    }
}
