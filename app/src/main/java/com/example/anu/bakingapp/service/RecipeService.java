package com.example.anu.bakingapp.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.Ingredient;
import com.example.anu.bakingapp.data.IngredientContentProvider;
import com.example.anu.bakingapp.data.RecipeWidgetProvider;
import com.example.anu.bakingapp.utils.CurrentRecipeUtil;

import java.util.ArrayList;
import java.util.List;
import static com.example.anu.bakingapp.data.IngredientContentProvider.COLUMN_INGREDIENT;
import static com.example.anu.bakingapp.data.IngredientContentProvider.COLUMN_MEASURE;
import static com.example.anu.bakingapp.data.IngredientContentProvider.COLUMN_QUANTITY;
import static com.example.anu.bakingapp.data.IngredientContentProvider.CONTENT_URI;

/**
 * An {@link IntentService} sub class is used to
 * handle an asynchronous task in a service on a seperate handler thread
 */
public class RecipeService extends IntentService {

    /**
     * To keep things organized, it’s best to define the actions that the IntentService can handle
     */
    private static final String ACTION_UPDATE_RECIPE_WIDGETS = "com.example.anu.bakingapp.action.update_recipe_widgets";

    public RecipeService() {
        super("RecipeService");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null){
            if (ACTION_UPDATE_RECIPE_WIDGETS.equals(intent.getAction())) {
                handleActionUpdateRecipeWidgets();
            }
        }
    }

    /**
     * Handle action UpdateRecipeWidgets in the provided background thread
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void handleActionUpdateRecipeWidgets() {
        CurrentRecipeUtil currentRecipeUtil = new CurrentRecipeUtil(this);
        int recipe_id = currentRecipeUtil.getKeyRecipeId();
        List<Ingredient> ingredientList = new ArrayList<>();
        if (recipe_id != -1){
            Uri RECIPE_URI = CONTENT_URI.buildUpon().appendPath(String.valueOf(recipe_id)).build();
            Cursor cursor = getContentResolver().query(
                    RECIPE_URI,
                    null,
                    null,
                    null,
                    null
            );
            // Extract the recipe details
            int imgRes = R.drawable.ic_restaurant_icon;
            // Default image in case no recipes added
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (cursor.moveToNext()){
                    int recipeIdIndex = cursor.getColumnIndex(IngredientContentProvider.COLUMN_RECIPE_ID);
                    int quantityIndex = cursor.getColumnIndex(COLUMN_QUANTITY);
                    int measureIndex = cursor.getColumnIndex(COLUMN_MEASURE);
                    int ingredientIndex = cursor.getColumnIndex(COLUMN_INGREDIENT);
                    int quantity = cursor.getInt(quantityIndex);
                    String measure = cursor.getString(measureIndex);
                    String ingredient = cursor.getString(ingredientIndex);
                    ingredientList.add(new Ingredient(recipe_id, quantity, measure, ingredient));
                }
            }
            if (cursor != null)
                cursor.close();
        }
        else {
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view_widget);
        //Now update all widgets
        RecipeWidgetProvider.updateRecipeWidgets(this, ingredientList, appWidgetManager, appWidgetIds);
    }

    /**
     * Starts this service to perform UpdaterecipeWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateRecipeWidgets(Context context) {
        Intent intent = new Intent(context, RecipeService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGETS);
        context.startService(intent);
    }
}
