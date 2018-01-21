package com.example.anu.bakingapp.utils;



import com.example.anu.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeImageUtils {

    /**
     * list which stores images for the recipes
     */
    private static final List<Integer> recipeImages = new ArrayList<Integer>(){{
        add(R.drawable.nutella_pie);
        add(R.drawable.brownies);
        add(R.drawable.yellow_cake);
        add(R.drawable.cheese_cake);
    }};

    public static List<Integer> getRecipeImages(){
        return recipeImages;
    }
}
