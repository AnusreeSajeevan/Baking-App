package com.example.anu.bakingapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

@Entity(primaryKeys = {"recipe_id","step_id"})
public class StepThumbnail {

    @ColumnInfo(name = "recipe_id")
    @NonNull
    private int recipeId;

    @NonNull
    @ColumnInfo(name = "step_id")
    private int stepId;

    @ColumnInfo(name = "thumbnail_path")
    private String thumbnailPath;

    public StepThumbnail(@NonNull int recipeId, @NonNull int stepId, String thumbnailPath) {
        this.recipeId = recipeId;
        this.stepId = stepId;
        this.thumbnailPath = thumbnailPath;
    }

    @NonNull
    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(@NonNull int recipeId) {
        this.recipeId = recipeId;
    }

    @NonNull
    public int getStepId() {
        return stepId;
    }

    public void setStepId(@NonNull int stepId) {
        this.stepId = stepId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}
