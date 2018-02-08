package com.example.anu.bakingapp.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.Ingredient;
import com.example.anu.bakingapp.ui.activity.RecipeIngredientsWidgetActivity;

import static com.example.anu.bakingapp.data.Ingredient.BASE_CONTENT_URI;
import static com.example.anu.bakingapp.data.Ingredient.PATH_RECIPES;

public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private Context context;

    public GridRemoteViewsFactory(Context context) {
        this.context = context;
    }

    private Cursor cursor;

    @Override
    public void onCreate() {

    }

    /**
     * called on start and when notifiAppWidgetViewDataChanged called
     */
    @Override
    public void onDataSetChanged() {

        // Get all recipes details
        Uri RECIPE_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();
        if (cursor != null) cursor.close();
        cursor = context.getContentResolver().query(
                RECIPE_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onDestroy() {
        if (cursor != null) cursor.close();
    }

    @Override
    public int getCount() {
        if (null == cursor)
            return 0;
        else
            return cursor.getCount();
    }


    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (cursor == null || cursor.getCount() == 0) return null;
        cursor.moveToPosition(position);
        int idIndex = cursor.getColumnIndex(Ingredient.COLUMN_RECIPE_ID);
        int quantityIndex = cursor.getColumnIndex(Ingredient.COLUMN_QUANTITY);
        int measureIndex = cursor.getColumnIndex(Ingredient.COLUMN_MEASURE);
        int ingredientIndex = cursor.getColumnIndex(Ingredient.COLUMN_INGREDIENT);

        int recipeId = cursor.getInt(idIndex);
        int quantity = cursor.getInt(quantityIndex);
        String measure = cursor.getString(measureIndex);
        String ingredient = cursor.getString(ingredientIndex);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

       /* // Update the plant image
        int imgRes = PlantUtils.getPlantImageRes(context, timeNow - createdAt, timeNow - wateredAt, plantType);
        views.setImageViewResource(R.id.widget, imgRes);*/
        views.setTextViewText(R.id.widget_plant_name, String.valueOf(recipeId));


        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
        Bundle extras = new Bundle();
        extras.putLong(RecipeIngredientsWidgetActivity.EXTRA_RECIPE_ID, recipeId);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_recipe_image, fillInIntent);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; //treat all the items in the gridview as same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}