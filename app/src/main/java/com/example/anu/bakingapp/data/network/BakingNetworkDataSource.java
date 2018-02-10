package com.example.anu.bakingapp.data.network;


import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.anu.bakingapp.AppExecutors;
import com.example.anu.bakingapp.data.Recipe;
import com.example.anu.bakingapp.data.Thumbnail;
import com.example.anu.bakingapp.data.database.RecipeDao;
import com.example.anu.bakingapp.utils.BakingJsonUtils;
import com.example.anu.bakingapp.volley.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * provides an api for doing all the networking operations
 */
public class BakingNetworkDataSource {
    private static final String TAG = BakingNetworkDataSource.class.getSimpleName();
    private Context mContext;
    private final AppExecutors mAppExecutors;

    public MutableLiveData<Recipe[]> getmDownloadedRecipes() {
        return mDownloadedRecipes;
    }

    private final MutableLiveData<Recipe[]> mDownloadedRecipes;
    Recipe[] recipes = new Recipe[0];

    //the url where the recipe listing json is located
    private static final String URL_RECIPE_LISTING = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    /**
     * for singleton instantiation
     */
    private static final Object LOCK = new Object();
    private static BakingNetworkDataSource newInstance;

    private BakingNetworkDataSource(Context mContext, AppExecutors mAppExecutors) {
        this.mContext = mContext;
        this.mAppExecutors = mAppExecutors;
        mDownloadedRecipes = new MutableLiveData<>();
    }

    /**
     * get the singleton for this class
     */
    public static BakingNetworkDataSource getNewInstance(Context context, AppExecutors appExecutors){
        if (null == newInstance){
            synchronized (LOCK){
                newInstance = new BakingNetworkDataSource(context, appExecutors);
            }
        }
        return newInstance;
    }



    /**
     * method to fetch recipes
     */
    public void fetchRecipes() {
            mAppExecutors.networkIO().execute(() -> {
                String tag = "FetchRecipes";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, BakingNetworkDataSource.URL_RECIPE_LISTING,
                        response -> {
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                Recipe[] recipe = new Recipe[0];
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                    recipe = BakingJsonUtils.getRecipeListFromJson(jsonArray);
                                    recipes = recipe;
                                    mDownloadedRecipes.postValue(recipe);
//                                    getThumbnailUrls(recipes);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }, error -> {});
                AppController.getInstance().addToRequestQueue(stringRequest, tag);
            });
    }

  /*  private void getThumbnailUrls(Recipe[] recipes) throws Throwable {
        Log.d("getBitmapp","getBitmap");
        Log.d("getBitmapp","length : " + recipes.length);
        ArrayList<Thumbnail> thumbnails = new ArrayList<>();
        for (int i=0;i<recipes.length;i++){
            Thumbnail thumbnail = new Thumbnail();
            thumbnail.setPath(recipes[i].getThumbnailPath());
            thumbnail.setPosition(i);
            thumbnails.add(thumbnail);
        }
        Log.d("getBitmapp","1 : : ");
        Log.d("getBitmapp","thumbnails.size() : " + thumbnails.size());
        if (thumbnails.size() > 0*//* && !mView.isRunningTest()*//*) {
            new GetVideoThumbnailTask().execute(thumbnails.toArray(new Thumbnail[thumbnails.size()]));
        }
    }*/
}
