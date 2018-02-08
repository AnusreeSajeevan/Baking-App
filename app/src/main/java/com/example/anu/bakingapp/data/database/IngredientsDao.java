package com.example.anu.bakingapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.anu.bakingapp.data.Ingredient;

import java.util.List;

@Dao
public interface IngredientsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIngredients(Ingredient ingredient);

    @Query("SELECT * FROM ingredient")
    LiveData<List<Ingredient>> getIngredients();

    @Query("SELECT * FROM ingredient WHERE recipe_id = :recipId")
    LiveData<Ingredient> getIngredientWithId(int recipId);

}
