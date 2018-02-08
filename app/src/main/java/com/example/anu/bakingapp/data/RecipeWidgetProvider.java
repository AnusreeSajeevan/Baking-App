package com.example.anu.bakingapp.data;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.service.ListWidgetService;
import com.example.anu.bakingapp.service.RecipeService;
import com.example.anu.bakingapp.utils.CurrentRecipeUtil;

import java.util.List;


/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String TAG = RecipeWidgetProvider.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, List<Ingredient> ingredientList, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = getListIngredientsRemoteView(context);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews getListIngredientsRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);

        /**
         * set {@link ListWidgetService} to act as the adapter for the listview
         */
        Intent intentAdapter = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.list_view_widget, intentAdapter);

        CurrentRecipeUtil currentRecipeUtil = new CurrentRecipeUtil(context);

        views.setTextViewText(R.id.recipe_name_widget, currentRecipeUtil.getKeyRecipeName());

        //Handle empty views
        views.setEmptyView(R.id.list_view_widget, R.id.layout_empty);

        return views;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Start the intent service update widget action, the service takes care of updating the widgets UI
        RecipeService.startActionUpdateRecipeWidgets(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void updateRecipeWidgets(Context context, List<Ingredient> ingredientList, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, ingredientList, appWidgetManager, appWidgetId);
        }
    }



    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        RecipeService.startActionUpdateRecipeWidgets(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
}

