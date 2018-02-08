package com.example.anu.bakingapp.utils;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

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
            recipeArrayList.add(new Recipe(jsonObject.getInt(KEY_RECIPE_ID),
                    jsonObject.getString(KEY_RECIPE_NAME),
                    jsonObject.getJSONArray(KEY_INGREDIENTS_ARRAY).toString(),
                    jsonObject.getJSONArray(KEY_STEPS_ARRAY).toString()));
        }
        Recipe[] recipes = recipeArrayList.toArray(new Recipe[recipeArrayList.size()]);
        return recipes;
    }

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
     * @param steps
     * @return number of steps
     */
    public static int getStepsCount(String steps) throws JSONException {
        JSONArray jsonArraySteps = new JSONArray(steps);
        return jsonArraySteps.length();
    }

    /**
     * method to parse recipe details to get ingredients and steps
     * @param recipeId
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
     * @param steps
     */
    public static List<Step> parseSteps(String steps) throws JSONException {
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
                    jsonObject.getString(KEY_STEPS_SHORT_DESCRIPTION),
                    jsonObject.getString(KEY_STEPS_DESCRIPTION),
                    video,
                    thumbNail));
        }
        return stepsList;
    }
}
