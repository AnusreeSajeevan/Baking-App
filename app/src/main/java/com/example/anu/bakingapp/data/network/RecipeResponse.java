package com.example.anu.bakingapp.data.network;


import com.example.anu.bakingapp.data.database.Recipe;
import com.google.gson.annotations.SerializedName;

public class RecipeResponse {
    public RecipeResponse(Recipe[] recipes) {
        this.recipes = recipes;
    }

    @SerializedName("")
    private Recipe[] recipes;

    public Recipe[] getRecipes() {
        return recipes;
    }
}
