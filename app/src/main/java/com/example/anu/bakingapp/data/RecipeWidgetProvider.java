package com.example.anu.bakingapp.data;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.Ingredient;
import com.example.anu.bakingapp.ui.activity.RecipeIngredientsWidgetActivity;
import com.example.anu.bakingapp.ui.activity.RecipeActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String EXTRA_RECIPE_ID = "com.example.anu.bakingapp.extra.RECIPE_ID";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        /**
         * get widget width to determine single recipe or all the recipes
         */
        Bundle bundle = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = bundle.getInt(appWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);

        RemoteViews views = null;
        int recipeId = -1;
        if (width < 300)
            views = getSingleRecipeRemoteView(context, recipeId);
      /*  else
            views = getAllRecipesRemoteView(context);*/

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

 /*   *//**
     * method to get remote view for all the recipes
     * @param context called context
     * @return remote view
     *//*
    private static RemoteViews getAllRecipesRemoteView(Context context) {
        
    }
*/
    /**
     * method to get remote view for single recipe
     * @param context called context
     * @param recipeId recipe id
     * @return remote view
     */
    private static RemoteViews getSingleRecipeRemoteView(Context context, int recipeId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        /**
         * create pending intent to load {@link RecipeIngredientsWidgetActivity} for the recipeId
         * and load {@link RecipeActivity} for invalid recipe
         */
        Intent intent;
        if (recipeId == Ingredient.INVALID_RECIPE_ID)
            intent = new Intent(context, RecipeActivity.class);
        else {
            intent = new Intent(context, RecipeIngredientsWidgetActivity.class);
            intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_recipe_image, pendingIntent);
        views.setTextViewText(R.id.widget_plant_name, String.valueOf(recipeId));

        return views;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

