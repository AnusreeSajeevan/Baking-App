package com.example.anu.bakingapp.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.utils.CurrentRecipeUtil;
import static com.example.anu.bakingapp.data.IngredientContentProvider.COLUMN_INGREDIENT;
import static com.example.anu.bakingapp.data.IngredientContentProvider.COLUMN_MEASURE;
import static com.example.anu.bakingapp.data.IngredientContentProvider.COLUMN_QUANTITY;
import static com.example.anu.bakingapp.data.IngredientContentProvider.COLUMN_RECIPE_ID;
import static com.example.anu.bakingapp.data.IngredientContentProvider.CONTENT_URI;

/**
 * Created by Design on 31-01-2018.
 */

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private static final String TAG = ListRemoteViewsFactory.class.getSimpleName();

    private Context context;

    public ListRemoteViewsFactory(Context context) {
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
        CurrentRecipeUtil currentRecipeUtil = new CurrentRecipeUtil(context);
        String currentRecipeId = String.valueOf(currentRecipeUtil.getKeyRecipeId());
        // Get all ingredients corresponding to recipeId
        Uri INGREDIENT_URI = CONTENT_URI.buildUpon().appendPath(currentRecipeId).build();
        if (cursor != null) cursor.close();
        cursor = context.getContentResolver().query(
                INGREDIENT_URI,
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
     * @param position The current position of the item in the ListView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (cursor == null || cursor.getCount() == 0) return null;
        cursor.moveToPosition(position);
        int recipeIdIndex = cursor.getColumnIndex(COLUMN_RECIPE_ID);
        int ingredientNameIndex = cursor.getColumnIndex(COLUMN_INGREDIENT);
        int quantityIndex = cursor.getColumnIndex(COLUMN_QUANTITY);
        int measureNameIndex = cursor.getColumnIndex(COLUMN_MEASURE);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);

        views.setTextViewText(R.id.widget_ingredient_name, cursor.getString(ingredientNameIndex));
        views.setTextViewText(R.id.widget_quantity_measure, cursor.getString(quantityIndex) + " " + cursor.getString(measureNameIndex));
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