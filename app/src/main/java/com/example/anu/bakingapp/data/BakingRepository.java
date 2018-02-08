package com.example.anu.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.anu.bakingapp.AppExecutors;
import com.example.anu.bakingapp.data.database.IngredientsDao;
import com.example.anu.bakingapp.data.database.RecipeDao;
import com.example.anu.bakingapp.data.network.BakingNetworkDataSource;

import java.util.ArrayList;
import java.util.List;

public class BakingRepository {

    private static final String TAG = BakingRepository.class.getSimpleName();

    private RecipeDao recipeDao;
    private IngredientsDao ingredientsDao;
    private BakingNetworkDataSource networkDataSource;
    private AppExecutors appExecutors;


    private MutableLiveData<Boolean> isAddedToWidget = new MutableLiveData<>();

    //for singleton instantiation
    private static final Object LOCK = new Object();
    private static BakingRepository newInstance;

    private boolean mInitialized = false;
    private List<Ingredient> ingredientLists = new ArrayList<>();

    private BakingRepository(RecipeDao recipeDao, IngredientsDao ingredientsDao,
                             BakingNetworkDataSource networkDataSource, AppExecutors appExecutors) {
        this.recipeDao = recipeDao;
        this.ingredientsDao = ingredientsDao;
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
    public synchronized static BakingRepository getInstance(RecipeDao recipeDao, IngredientsDao ingredientsDao,
                                                            BakingNetworkDataSource networkDataSource, AppExecutors appExecutors){
        if (null == newInstance){
            synchronized (LOCK){
                newInstance = new BakingRepository(recipeDao, ingredientsDao, networkDataSource, appExecutors);
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

    /**
     * method to insert ingredients
     * @return
     */
    public void insertIngredients(List<Ingredient> ingredients){
        ingredientLists = ingredients;
        new InsertIngredientsAsyncTask().execute();
    }

    class InsertIngredientsAsyncTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            Ingredient[] ingredients = ingredientLists.toArray(new Ingredient[ingredientLists.size()]);
            long[] ids = ingredientsDao.insertIngredients(ingredients);
            if (ids.length > 0)
                return true;
            else
                return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Log.d(TAG, "onPostExecute");
            Log.d(TAG, "aBoolean : " + aBoolean);
            isAddedToWidget.postValue(aBoolean);
        }
    }

    /*private class InsertIngredientsAsyncTask extends AsyncTask<List[] , Void, Boolean>{

        private IngredientsDao ingredientsDao;

        public InsertIngredientsAsyncTask(IngredientsDao ingredientsDao){
            this.ingredientsDao = ingredientsDao;
        }

        @Override
        protected Boolean doInBackground(List<Ingredient> lists) {
            boolean isAdded;

            for (Ingredient ingredient : lists){

            }
            for (int i = 0;i<lists.length;i++){
                ingredients[i].setRecipeId(lists[i].get(i).getRecipeId());
                ingredients[i].setQuantity(lists[i].get(i).getQuantity());
                ingredients[i].setMeasure(lists[i].get(i).getMeasure());
                ingredients[i].setIngredient(lists[i].get(i).getIngredient());
            }
            long[] ids = ingredientsDao.insertIngredients(ingredients);
            Log.d(TAG, "inseted ids : " + ids.length);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }*/

    public MutableLiveData<Boolean> getIsAddedToWidget() {
        initializeData();
        return isAddedToWidget;
    }
}
