package com.example.anu.bakingapp.data.network;


import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.anu.bakingapp.AppExecutors;
import com.example.anu.bakingapp.data.Recipe;
import com.example.anu.bakingapp.utils.BakingJsonUtils;
import com.example.anu.bakingapp.volley.AppController;

import org.json.JSONArray;

/**
 * provides an api for doing all the networking operations
 */
public class BakingNetworkDataSource {
    private static final String TAG = BakingNetworkDataSource.class.getSimpleName();
    private Context mContext;
    private AppExecutors mAppExecutors;

    public MutableLiveData<Recipe[]> getmDownloadedRecipes() {
        return mDownloadedRecipes;
    }

    private MutableLiveData<Recipe[]> mDownloadedRecipes;

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
                                Recipe[] recipes = BakingJsonUtils.getRecipeListFromJson(jsonArray);
                                mDownloadedRecipes.postValue(recipes);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }, error -> {});
                AppController.getInstance().addToRequestQueue(stringRequest, tag);
            });
    }


}
