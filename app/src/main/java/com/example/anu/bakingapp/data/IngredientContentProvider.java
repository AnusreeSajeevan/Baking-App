package com.example.anu.bakingapp.data;

import android.arch.persistence.room.Ignore;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.anu.bakingapp.data.database.BakingDatabase;
import com.example.anu.bakingapp.data.database.IngredientsDao;

/**
 * Created by Design on 08-02-2018.
 */

public class IngredientContentProvider extends ContentProvider {


    public static final int CODE_INGREDIENT_WITH_RECIPE_ID = 100;

    public static UriMatcher sUriMatcher = buildUriMatcher();

    public static final String PATH_INGREDIENT = "ingredient";



    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_INGREDIENT + "/#", CODE_INGREDIENT_WITH_RECIPE_ID);

        return uriMatcher;
    }

    public static final String COLUMN_RECIPE_ID = "recipe_id";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_MEASURE = "measure";
    public static final String COLUMN_INGREDIENT = "ingredient";

    public static final int INVALID_RECIPE_ID = -1;

    // The authority, which is how your code knows which Content Provider to access
    public static final String CONTENT_AUTHORITY = "com.example.anu.bakingapp";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * the content uri used to access ingredient table from th content provider
     */
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENT).build();

    private static final String EXTRA_RECIPE_ID = "com.example.anu.bakingapp.extra.RECIPE_ID";

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1,
                        @Nullable String s1) {
        Log.d("CheckWwidgetupdate","query");
        int uriMatch = sUriMatcher.match(uri);
        if (uriMatch == CODE_INGREDIENT_WITH_RECIPE_ID) {
            Log.d("CheckWwidgetupdate","if");
            final Context context = getContext();
            Log.d("CheckWwidgetupdate","1");
//            if (context == null) {
//                Log.d("CheckWwidgetupdate","if 2");
//                return null;
//            }
            IngredientsDao ingredientsDao = BakingDatabase.getNewInstance(context).getIngredientsDao();
            Log.d("CheckWwidgetupdate","2");
            final Cursor cursor;
            String[] recipeId = new String[]{uri.getLastPathSegment()};
            Log.d("CheckWwidgetupdate","uri.getLastPathSegment() : "+uri.getLastPathSegment());
            Log.d("CheckWwidgetupdate","3");
            Log.d("CheckWwidgetupdate","recipeId : " + recipeId);
            Log.d("CheckWwidgetupdate","recipe id : " + Integer.parseInt(recipeId[0]));
            cursor = ingredientsDao.getIngredientWithId(Integer.parseInt(recipeId[0]));
            Log.d("CheckWwidgetupdate","count : " + cursor.getCount());
            cursor.setNotificationUri(context.getContentResolver(), uri);
            Log.d("CheckWwidgetupdate","4");
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
