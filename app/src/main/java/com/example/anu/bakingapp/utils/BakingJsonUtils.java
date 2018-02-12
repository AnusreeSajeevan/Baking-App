package com.example.anu.bakingapp.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.anu.bakingapp.data.Ingredient;
import com.example.anu.bakingapp.data.Recipe;
import com.example.anu.bakingapp.data.Step;
import com.example.anu.bakingapp.data.Thumbnail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BakingJsonUtils {

    private static final String TAG = BakingJsonUtils.class.getSimpleName();

    private static final String KEY_RECIPE_ID = "id";
    private static final String KEY_RECIPE_NAME = "name";

    private static final String KEY_INGREDIENTS_ARRAY = "ingredients";
    private static final String KEY_INGREDIENTS_QUANTITY = "quantity";
    private static final String KEY_INGREDIENTS_MEASURE = "measure";
    private static final String KEY_INGREDIENT = "ingredient";

    private static final String KEY_STEPS_ARRAY= "steps";
    private static final String KEY_STEPS_ID= "id";
    private static final String KEY_STEPS_SHORT_DESCRIPTION= "shortDescription";
    private static final String KEY_STEPS_DESCRIPTION= "description";
    private static final String KEY_STEPS_VIDEO_URL= "videoURL";
    private static final String KEY_STEPS_THUMBNAIL_URL= "thumbnailURL";


  /*  *//**
     * method to get the recipe from the jsonArray
     * @param jsonArray the json array to get recipe list from
     * @return recipe list
     *//*
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Recipe[] getRecipeListFromJson(JSONArray jsonArray) throws JSONException {
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            recipeArrayList.add(new Recipe(jsonObject.getInt(KEY_RECIPE_ID),
                    jsonObject.getString(KEY_RECIPE_NAME),
                    jsonObject.getJSONArray(KEY_INGREDIENTS_ARRAY).toString(),
                    jsonObject.getJSONArray(KEY_STEPS_ARRAY).toString()));
        }
        return recipeArrayList.toArray(new Recipe[recipeArrayList.size()]);
    }*/

    /**
     * method to get number of ingredients
     * @param ingredients ingredients to get the counts from
     * @return number of ingredients
     */
    public static int getIngredientsCount(String ingredients) throws JSONException {
        JSONArray jsonArrayIngredients = new JSONArray(ingredients);
        return jsonArrayIngredients.length();
    }

    /**
     * method to get number of steps
     * @param steps list of step objects
     * @return number of steps
     */
    public static int getStepsCount(String steps) throws JSONException {
        JSONArray jsonArraySteps = new JSONArray(steps);
        return jsonArraySteps.length();
    }

    /**
     * method to parse recipe details to get ingredients and steps
     * @param recipeId idod the recipe
     * @param ingredients
     *
//    public static /*List<Ingredient>*/
    public static List<Ingredient> parseIngredients(int recipeId, String ingredients) throws JSONException {
        JSONArray jsonArrayIngredients = new JSONArray(ingredients);
        List<Ingredient> ingredientList = new ArrayList<>();
        for (int i = 0;i<jsonArrayIngredients.length();i++){
            JSONObject jsonObject = jsonArrayIngredients.getJSONObject(i);
            ingredientList.add(new Ingredient(recipeId,
                    jsonObject.getInt(KEY_INGREDIENTS_QUANTITY),
                    jsonObject.getString(KEY_INGREDIENTS_MEASURE),
                    jsonObject.getString(KEY_INGREDIENT)));
        }
        return ingredientList;
    }
    /**
     * method to parse steps details to get steps
     * @param steps step object
     */
    public static List<Step> parseSteps(int recipeId, String steps) throws JSONException {
        JSONArray jsonArraySteps = new JSONArray(steps);
        List<Step> stepsList = new ArrayList<>();
        for (int i = 0;i<jsonArraySteps.length();i++){
            JSONObject jsonObject = jsonArraySteps.getJSONObject(i);
            String video = null;
            String thumbNail = null;

            /*
            check for null video and thumbnail
            */

            if (null != jsonObject.getString(KEY_STEPS_VIDEO_URL))
                video = jsonObject.getString(KEY_STEPS_VIDEO_URL);

            if (null != jsonObject.getString(KEY_STEPS_THUMBNAIL_URL))
                thumbNail = jsonObject.getString(KEY_STEPS_THUMBNAIL_URL);

            stepsList.add(new Step(
                    jsonObject.getInt(KEY_STEPS_ID),
                    recipeId,
                    jsonObject.getString(KEY_STEPS_SHORT_DESCRIPTION),
                    jsonObject.getString(KEY_STEPS_DESCRIPTION),
                    video,
                    thumbNail, i));
        }
        return stepsList;
    }

    public void getRecipeThumbnail(ArrayList<Recipe> result) {
        Log.d("CheckThumbnailurl","result : " + result);
        ArrayList<Thumbnail> thumbnails = new ArrayList<>();
       /* for (int i = 0; i < result.size(); i++) {
            Recipe recipe = result.get(i);
            if (recipe.getImage() == null || recipe.getImage().isEmpty()) {
                int stepCount = recipe.getSteps().size();
                for (int j = stepCount - 1; j >= 0; j--) {
                    if (recipe.getSteps().get(j).getThumbnailURL() != null &&
                            !recipe.getSteps().get(j).getThumbnailURL().isEmpty()) {
                        recipe.setImage(recipe.getSteps().get(j).getThumbnailURL());
                        break;
                    } else if (recipe.getSteps().get(j).getVideoURL() != null &&
                            !recipe.getSteps().get(j).getVideoURL().isEmpty()) {
//                        String[] params = new String[2];
//                        params[0] = recipe.getSteps().get(j).getVideoURL(); // url
//                        params[1] = String.valueOf(i); // position
                        Thumbnail thumbnail = new Thumbnail();
                        thumbnail.setPath(recipe.getSteps().get(j).getVideoURL());
                        thumbnail.setPosition(i);
                        thumbnails.add(thumbnail);
                        break;
                    }
                }
            }
        }*/
/*
        if (thumbnails.size() > 0 && !mView.isRunningTest()) {
            new GetVideoThumbnailTask().execute(thumbnails.toArray(new Thumbnail[thumbnails.size()]));
        }*/
    }

    /**
     * method to get the recipe from the jsonArray
     * @param jsonArray the json array to get recipe list from
     * @return recipe list
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Recipe[] getRecipeListFromJson(JSONArray jsonArray) throws JSONException {
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            try {
//                String thumbnailPath = getThumbnailUrl(jsonObject.getJSONArray(KEY_STEPS_ARRAY));
                String thumbnailPath = "";
                recipeArrayList.add(new Recipe(jsonObject.getInt(KEY_RECIPE_ID),
                        jsonObject.getString(KEY_RECIPE_NAME),
                        jsonObject.getJSONArray(KEY_INGREDIENTS_ARRAY).toString(),
                        jsonObject.getJSONArray(KEY_STEPS_ARRAY).toString(), thumbnailPath));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return recipeArrayList.toArray(new Recipe[recipeArrayList.size()]);
    }

    /**
     * method to get the thumbnail url
     * @param jsonArray
     * @return
     */
    public static String getThumbnailUrl(JSONArray jsonArray) throws Throwable {
        Log.d("getThumbnailUrl", "getThumbnailUrl");
        String thumbnailUrl = "";
        for (int i = jsonArray.length()-1;i>=0;i--){
            Log.d("getThumbnailUrl", "i : " + i);
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (!jsonObject.getString(KEY_STEPS_THUMBNAIL_URL).equals("") ||
                    !jsonObject.getString(KEY_STEPS_THUMBNAIL_URL).isEmpty()){
                Log.d("getThumbnailUrl", "if");
                thumbnailUrl = jsonObject.getString(KEY_STEPS_THUMBNAIL_URL);
                break;
            }
            else if (!jsonObject.getString(KEY_STEPS_VIDEO_URL).equals("") ||
                    !jsonObject.getString(KEY_STEPS_VIDEO_URL).isEmpty()){
                Log.d("getThumbnailUrl", "else");
                thumbnailUrl = jsonObject.getString(KEY_STEPS_VIDEO_URL);
                break;
            }
        }

/*        Bitmap bitmap = retriveVideoFrameFromVideo(thumbnailUrl);
        Log.d("getBitmap","1");
        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, false);
            Log.d("getBitmap","bitmap : " + bitmap);
            Context context = null;
            FileOutputStream out = null;
            try {
                out = new FileOutputStream("bakeeeee");
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
//        Log.d("getThumbnailUrl","thumbnailUrl : " + thumbnailUrl);
        return thumbnailUrl;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
