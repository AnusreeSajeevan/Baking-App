package com.example.anu.bakingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * class which manages the details of recently added widget
 */
public class CurrentRecipeUtil {
    private static final String PREF_NAME = "pref_recipe";
    private static final String KEY_RECIPE_ID = "recipe_id";
    private static final String KEY_RECIPE_NAME = "recipe_name";
    private static final String KEY_HAVE_STORAGE_PERMISSION = "have_storage_permission";

    private static SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    private final int PREF_MODE_PRIVATE = 0;
    private final Context context;

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

    public boolean getKeyIsStoragePermissionGranted() {
        return preferences.getBoolean(KEY_HAVE_STORAGE_PERMISSION, false);
    }

    public void setPermissionGranted(boolean havePermission) {
        editor.putBoolean(KEY_HAVE_STORAGE_PERMISSION, havePermission);
        editor.commit();
    }

}
