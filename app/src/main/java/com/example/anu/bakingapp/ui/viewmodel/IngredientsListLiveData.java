package com.example.anu.bakingapp.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.anu.bakingapp.data.Ingredient;
import com.example.anu.bakingapp.utils.BakingJsonUtils;

import org.json.JSONException;

import java.util.List;

public class IngredientsListLiveData extends LiveData<List<Ingredient>> {
    private final Context context;
    public IngredientsListLiveData(Context context, String ingredients) {
        this.context = context;
        loadData(ingredients);
    }
    private void loadData(String ingredients) {
        Log.d("checkingredients", ingredients);
        new AsyncTask<String, Void, List<Ingredient>>(){

            @Override
            protected List<Ingredient> doInBackground(String... strings) {
                try {
                    Log.d("checkingredients","1");
                    Log.d("checkingredients",strings[0]);
                    List<Ingredient> list = BakingJsonUtils.parseIngredients(strings[0]);
                    Log.d("checkingredients", String.valueOf(list.size()));
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Ingredient> ingredientList) {
                Log.d("checkPostvalue","onPostExecute");
                setValue(ingredientList);
            }
        }.execute(ingredients);
    }
}
