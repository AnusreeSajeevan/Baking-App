package com.example.anu.bakingapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.anu.bakingapp.data.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(Recipe... recipes);

    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> getRecipes();

    @Query("DELETE FROM recipe")
    void deleteOldRecipes();

    @Query("SELECT COUNT(id) from recipe")
    int getRecipeCount();

    @Query("SELECT * FROM recipe WHERE id = :id")
    LiveData<Recipe> getRecipeWithId(int id);

    @Query("SELECT thumbnailPath FROM recipe WHERE id = :id")
    String getRecipeThumbnail(int id);

    @Query("UPDATE recipe SET thumbnailPath = :path WHERE id = :id")
    int updateRecipeThumbnail(int id, String path);
}
