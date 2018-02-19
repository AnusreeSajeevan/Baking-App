package com.example.anu.bakingapp.utils;


import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.anu.bakingapp.data.Ingredient;
import com.example.anu.bakingapp.data.Recipe;
import com.example.anu.bakingapp.data.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private static final String KEY_SERVINGS= "servings";


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
    /**
     * method to get the recipe from the jsonArray
     * @param jsonArray the json array to get recipe list from
     * @return recipe list
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Recipe[] getRecipeListFromJson(JSONArray jsonArray) {
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            try {
                String thumbnailPath = "";
                recipeArrayList.add(new Recipe(jsonObject.getInt(KEY_RECIPE_ID),
                        jsonObject.getString(KEY_RECIPE_NAME),
                        jsonObject.getJSONArray(KEY_INGREDIENTS_ARRAY).toString(),
                        jsonObject.getJSONArray(KEY_STEPS_ARRAY).toString(), thumbnailPath, jsonObject.getInt(KEY_SERVINGS)));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return recipeArrayList.toArray(new Recipe[recipeArrayList.size()]);
    }

    /**
     * method to get the thumbnail url
     * @param jsonArray array to get the thumbnail from
     * @return {@literal thumbnailUrl}
     */
    public static String getThumbnailUrl(JSONArray jsonArray) throws Throwable {
        String thumbnailUrl = "";
        for (int i = jsonArray.length()-1;i>=0;i--){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (!jsonObject.getString(KEY_STEPS_THUMBNAIL_URL).equals("") ||
                    !jsonObject.getString(KEY_STEPS_THUMBNAIL_URL).isEmpty()){
                thumbnailUrl = jsonObject.getString(KEY_STEPS_THUMBNAIL_URL);
                break;
            }
            else if (!jsonObject.getString(KEY_STEPS_VIDEO_URL).equals("") ||
                    !jsonObject.getString(KEY_STEPS_VIDEO_URL).isEmpty()){
                thumbnailUrl = jsonObject.getString(KEY_STEPS_VIDEO_URL);
                break;
            }
        }

        return thumbnailUrl;
    }
}
