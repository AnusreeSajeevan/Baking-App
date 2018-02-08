package com.example.anu.bakingapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.support.annotation.NonNull;

@Entity(primaryKeys = {"recipe_id","ingredient"})
public class Ingredient {


    private long id;
    @ColumnInfo(name = "recipe_id")
    @NonNull
    private int recipeId;

    @NonNull
    private int quantity;

    @NonNull
    private String measure;

    @NonNull
    private String ingredient;

    public Ingredient(int recipeId, int quantity, String measure, String ingredient) {
        this.recipeId = recipeId;
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
