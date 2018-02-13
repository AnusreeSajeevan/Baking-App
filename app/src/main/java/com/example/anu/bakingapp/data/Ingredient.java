package com.example.anu.bakingapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

@Entity(primaryKeys = {"recipe_id","ingredients"})
public class Ingredient {

    @ColumnInfo(name = "recipe_id")
    @NonNull
    private int recipeId;

    @NonNull
    private int quantity;

    @NonNull
    private String measure;

    @NonNull
    private String ingredients;

    public Ingredient(int recipeId, int quantity, @NonNull String measure, @NonNull String ingredients) {
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredients = ingredients;
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

    public void setMeasure(@NonNull String measure) {
        this.measure = measure;
    }

    public void setIngredients(@NonNull String ingredients) {
        this.ingredients = ingredients;
    }

    public int getQuantity() {
        return quantity;
    }

    @NonNull
    public String getMeasure() {
        return measure;
    }

    @NonNull
    public String getIngredients() {
        return ingredients;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
