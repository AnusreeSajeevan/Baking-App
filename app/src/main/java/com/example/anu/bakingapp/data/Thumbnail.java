package com.example.anu.bakingapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Thumbnail {

    @ColumnInfo(name = "recipe_id")
    @PrimaryKey
    private int recipeId;

    @NonNull
    private String path;

    public Thumbnail(int recipeId, String path) {
        this.recipeId = recipeId;
        this.path = path;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
