package com.example.anu.bakingapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.anu.bakingapp.data.StepThumbnail;
import com.example.anu.bakingapp.data.Thumbnail;

import java.util.List;

@Dao
public interface StepThumbnailDao {

    @Query("SELECT COUNT(*) FROM stepthumbnail WHERE recipe_id = :recipeId AND step_id = :stepId")
    int getThumbnailCountWithId(int recipeId, int stepId);

    @Query("SELECT * FROM stepthumbnail WHERE recipe_id = :recipeId")
    LiveData<List<StepThumbnail>> getAllThumbnails(int recipeId);

    @Query("SELECT * FROM stepthumbnail WHERE recipe_id = :recipeId")
    StepThumbnail[] getThumbnailsWithRecipeId(int recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertStepThumbnails(StepThumbnail... lists);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSingleStepThumbnail(StepThumbnail stepThumbnail);
}
