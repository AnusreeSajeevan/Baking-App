package com.example.anu.bakingapp.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.anu.bakingapp.data.Thumbnail;

@Dao
public interface ThumbnailDao {

    @Query("SELECT * FROM thumbnail WHERE recipe_id = :recipeId")
    Thumbnail getThumbnailCountWithId(int recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertThumbnails(Thumbnail... lists);
}
