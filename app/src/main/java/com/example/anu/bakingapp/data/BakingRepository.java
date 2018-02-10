package com.example.anu.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.example.anu.bakingapp.AppExecutors;
import com.example.anu.bakingapp.data.database.IngredientsDao;
import com.example.anu.bakingapp.data.database.RecipeDao;
import com.example.anu.bakingapp.data.database.ThumbnailDao;
import com.example.anu.bakingapp.data.network.BakingNetworkDataSource;
import com.example.anu.bakingapp.utils.BakingJsonUtils;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BakingRepository {

    private static final String TAG = BakingRepository.class.getSimpleName();

    private final RecipeDao recipeDao;
    private final IngredientsDao ingredientsDao;
    private final ThumbnailDao thumbnailDao;
    private final BakingNetworkDataSource networkDataSource;
    private final AppExecutors appExecutors;


    private final MutableLiveData<Boolean> isAddedToWidget = new MutableLiveData<>();

    //for singleton instantiation
    private static final Object LOCK = new Object();
    private static BakingRepository newInstance;

    private boolean mInitialized = false;
    private List<Ingredient> ingredientLists = new ArrayList<>();
    private Context context;

    private BakingRepository(RecipeDao recipeDao, IngredientsDao ingredientsDao, ThumbnailDao thumbnailDao,
                             BakingNetworkDataSource networkDataSource, AppExecutors appExecutors) {
        this.recipeDao = recipeDao;
        this.ingredientsDao = ingredientsDao;
        this.thumbnailDao = thumbnailDao;
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
    public synchronized static BakingRepository getInstance(RecipeDao recipeDao, IngredientsDao ingredientsDao, ThumbnailDao thumbnailDao,
                                                            BakingNetworkDataSource networkDataSource, AppExecutors appExecutors){
        if (null == newInstance){
            synchronized (LOCK){
                newInstance = new BakingRepository(recipeDao, ingredientsDao, thumbnailDao, networkDataSource, appExecutors);
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
         use {@link isFetchNeeded()} to determine whether to fetch recipe from the network
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
        fetchNeeded = recipeCount <= 0;

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
            return ids.length > 0;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            isAddedToWidget.postValue(aBoolean);
        }
    }

    public MutableLiveData<Boolean> getIsAddedToWidget() {
        initializeData();
        return isAddedToWidget;
    }

    public void updateThumbnailUrls(Context context, List<Recipe> recipes) throws Throwable {
        Log.d("ThumbnailCountCheck","BakingRepository");
        Log.d("ThumbnailCountCheck","recipes count : " + recipes.size());
        this.context = context;
            appExecutors.networkIO().execute(()->{
                List<Thumbnail> thumbnails = new ArrayList<>();
                for (Recipe recipe : recipes) {
                    Log.d("ThumbnailCountCheck", "for");
                    int recipeId = recipe.getId();//get thumbnail url from recipe table, it is used to determine recipe thumbnail has been loaded already
                    String thumbnailUrlInRecipeTable = recipeDao.getRecipeThumbnail(recipeId);
                    Log.d("ThumbnailCountCheck","thumbnailUrlInRecipeTable : " +thumbnailUrlInRecipeTable);

                    if (thumbnailUrlInRecipeTable.equals("") || thumbnailUrlInRecipeTable == null){
                        Log.d("ThumbnailCountCheck","in if");

                        //get number of rows in the thumbnail table with correcponding recipe id
                        Thumbnail thumbnail = thumbnailDao.getThumbnailCountWithId(recipeId);
                        Log.d("ThumbnailCountCheck","....");

                        if (thumbnail != null){
                            /**
                             * current recipe thumbnail url is already stored in thumbnail table
                             * get path from it and save to recipe table for the corresponding recipe
                             */
                            int numUpdatedColumns = recipeDao.updateRecipeThumbnail(recipeId, thumbnail.getPath());
                            Log.d("ThumbnailCountCheck","numUpdatedColumns : " + numUpdatedColumns);
                        }
                        else {
                            /**
                             * thumbnail does not already exist in thumbnail table
                             * create thumbnail
                             */
                            Log.d("ThumbnailCountCheck","does not already exists");
                            try {
                                String thumbnailUrl = BakingJsonUtils.getThumbnailUrl(new JSONArray(recipe.getSteps()));
                                Log.d("ThumbnailCountCheck","thumbnail url : " + thumbnailUrl);
                                if (!thumbnailUrl.isEmpty() || !thumbnail.equals("")){//**//* && !mView.isRunningTest()*//**//*) {
                                        thumbnails.add(new Thumbnail(recipe.getId(), thumbnailUrl));
                                }
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    }
                }

                //create thumbnail paths
                if (thumbnails.size() != 0 || !thumbnails.isEmpty()){
                    new GetVideoThumbnailTask().execute(thumbnails.toArray(new Thumbnail[thumbnails.size()]));
                }
            });
       // boolean isThumbnailExists = getIsThumbnailExists();
        /*Log.d("getBitmapp","getBitmap");
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
        if (thumbnails.size() > 0*//**//* && !mView.isRunningTest()*//**//*) {
            new BakingNetworkDataSource.GetVideoThumbnailTask().execute(thumbnails.toArray(new Thumbnail[thumbnails.size()]));
        }*/
    }

     private class GetVideoThumbnailTask extends AsyncTask<Thumbnail, Thumbnail, Thumbnail[]> {
        @Override
        protected Thumbnail[] doInBackground(Thumbnail... thumbnails) {
            Log.d("GetVideoThumbnailTask","doInBackground");
            Thumbnail[] thumbnailResult = new Thumbnail[thumbnails.length];
            for (int i = 0; i < thumbnails.length; i++) {
                Log.d("GetVideoThumbnailTask","i : " + i);
                Bitmap bitmap = null;
                MediaMetadataRetriever mediaMetadataRetriever = null;
                try {
                    mediaMetadataRetriever = new MediaMetadataRetriever();
                    Log.d("GetVideoThumbnailTask","mediaMetadataRetriever");
                    Log.d("GetVideoThumbnailTask","thumbnails[i].getPath() : " + thumbnails[i].getPath());
                    mediaMetadataRetriever.setDataSource(thumbnails[i].getPath(), new HashMap<String, String>());
                    bitmap = mediaMetadataRetriever.getFrameAtTime(8000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (mediaMetadataRetriever != null) {
                        mediaMetadataRetriever.release();
                    }
                }

                String path = "";
                if (bitmap != null) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
                    path = MediaStore.Images.Media.insertImage(
                            context.getContentResolver(),
                            bitmap, "tmp_recipe_thumb" + thumbnails[i].getRecipeId(), null);
                    Log.d("GetVideoThumbnailTask","path : " + path);
                }

                Thumbnail thumbnail = new Thumbnail(thumbnails[i].getRecipeId(), path);
                thumbnailResult[i] = thumbnail;
            }

            return thumbnailResult;
        }

        @Override
        protected void onPostExecute(Thumbnail[] result) {
            appExecutors.diskIO().execute(()->{
                thumbnailDao.insertThumbnails(result);
                for (int i=0;i<result.length;i++){
                    recipeDao.updateRecipeThumbnail(result[i].getRecipeId(), result[i].getPath());
                }
            });

        }
    }

/*    private boolean getIsThumbnailExists() {
        thumbnailDao.getThumbnailCountWithId();
    }*/


}
