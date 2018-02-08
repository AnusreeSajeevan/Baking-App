package com.example.anu.bakingapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;

@Entity
public class Ingredient {

    @Ignore
    public static final int INVALID_RECIPE_ID = -1;

    // The authority, which is how your code knows which Content Provider to access
    @Ignore
    public static final String AUTHORITY = "com.example.anu.bakingapp.data.database";

    // The base content URI = "content://" + <authority>
    @Ignore
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    @Ignore
    public static final String COLUMN_RECIPE_ID = "recipe_id";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_MEASURE = "measure";
    public static final String COLUMN_INGREDIENT = "ingredient";

    // Define the possible paths for accessing data in this contract
    // This is the path for the "ingredients" directory
    @Ignore
    public static final String PATH_RECIPES = "recipes";


    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "recipe_id")
    private int recipeId;
    private int quantity;
    private String measure;
    private String ingredient;

    public Ingredient(int quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public int getRecipe() {
        return recipeId;
    }

    public void setRecipe(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
