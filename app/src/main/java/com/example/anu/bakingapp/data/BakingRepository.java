package com.example.anu.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.anu.bakingapp.AppExecutors;
import com.example.anu.bakingapp.data.database.Ingredient;
import com.example.anu.bakingapp.data.database.Recipe;
import com.example.anu.bakingapp.data.database.RecipeDao;
import com.example.anu.bakingapp.data.network.BakingNetworkDataSource;

import java.util.List;

public class BakingRepository {
    private RecipeDao recipeDao;
    private BakingNetworkDataSource networkDataSource;
    private AppExecutors appExecutors;

    //for singleton instantiation
    private static final Object LOCK = new Object();
    private static BakingRepository newInstance;

    private boolean mInitialized = false;

    private BakingRepository(RecipeDao recipeDao, BakingNetworkDataSource networkDataSource, AppExecutors appExecutors) {
        this.recipeDao = recipeDao;
        this.networkDataSource = networkDataSource;
        this.appExecutors = appExecutors;
        LiveData<Recipe[]> recipes = networkDataSource.getmDownloadedRecipes();
        recipes.observeForever(newRecipesFromNetwork->{
            /*
             when mDownloadedRecipes changes, it will triggers a database save.
             old recipes will be deleted from the database and new values will be saves into it.
             database operations ust be done off of the main thread
             */
            appExecutors.networkIO().execute(()->{
                recipeDao.deleteOldRecipes();
                recipeDao.bulkInsert(newRecipesFromNetwork);
            });
        });
    }

    /**
     * make repository singleton
     */
    public synchronized static BakingRepository getInstance(RecipeDao recipeDao, BakingNetworkDataSource networkDataSource, AppExecutors appExecutors){
        if (null == newInstance){
            synchronized (LOCK){
                newInstance = new BakingRepository(recipeDao, networkDataSource, appExecutors);
            }
        }
        return newInstance;
    }

    private void initializeData(){
        /*
         initialization should happen only once per app lifetime
         if it has already been initialized, we don't have to do anything.
         */
        if (mInitialized)
            return;

        mInitialized = true;

        /*
         use {@link isfetchNeeded} to determine whether to fetch recipe from the network
         */
        appExecutors.diskIO().execute(()->{
            if (isFetchNeeded())
                networkDataSource.fetchRecipes();
        });
        
    }


    /**
     * method to determine whether fetch is needed
     * @return fetch is needed or not
     */
    private boolean isFetchNeeded(){
        /*
         get recipe count from database
         {@literal fetchNeeded} will be false if count>0, true otherwise
         */
        int recipeCount = recipeDao.getRecipeCount();
        boolean fetchNeeded;
        if (recipeCount>0)
            fetchNeeded = false;
        else
            fetchNeeded = true;

        return fetchNeeded;
    }

    /**
     * method to get recipes from dao
     * @return recipe list
     */
    public LiveData<List<Recipe>> getRecipeList(){
        initializeData();
        return recipeDao.getRecipes();
    }

    public LiveData<Recipe> getRecipeWithId(int recipeId){
        initializeData();
        Log.d("CheckRepoo","1");
        Log.d("CheckRepoo","recipeDao.getRecipeWithId(recipeId) : "+recipeDao.getRecipeWithId(recipeId));
        return recipeDao.getRecipeWithId(recipeId);
    }

    /**
     *
     */
    public void deleteRecipes(){
        appExecutors.diskIO().execute(()->{
            recipeDao.deleteOldRecipes();
            if (isFetchNeeded())
                networkDataSource.fetchRecipes();
        });
    }
}
