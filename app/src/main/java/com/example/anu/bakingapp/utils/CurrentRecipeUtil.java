package com.example.anu.bakingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.toolbox.StringRequest;

/**
 * class which manages the details of recently added widget
 */
public class CurrentRecipeUtil {
    public static final String PREF_NAME = "pref_recipe";
    public static final String KEY_RECIPE_ID = "recipe_id";
    public static final String KEY_RECIPE_NAME = "recipe_name";

    private static SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int PREF_MODE_PRIVATE = 0;
    private Context context;

    public CurrentRecipeUtil(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PREF_MODE_PRIVATE);
        editor = preferences.edit();
    }

    public int getKeyRecipeId() {
        return preferences.getInt(KEY_RECIPE_ID, -1);
    }

    public String getKeyRecipeName() {
        return preferences.getString(KEY_RECIPE_NAME, "");
    }

    public void setLatestRecipeDetails(int recipeId, String recipeName) {
        editor.putInt(KEY_RECIPE_ID, recipeId);
        editor.putString(KEY_RECIPE_NAME, recipeName);
        editor.commit();
    }
}
