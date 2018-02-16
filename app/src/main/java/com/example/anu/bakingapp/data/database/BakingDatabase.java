package com.example.anu.bakingapp.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.anu.bakingapp.data.Ingredient;
import com.example.anu.bakingapp.data.Recipe;
import com.example.anu.bakingapp.data.StepThumbnail;
import com.example.anu.bakingapp.data.Thumbnail;

@Database(entities = {Recipe.class, Ingredient.class, Thumbnail.class, StepThumbnail.class}, version = 12)
public abstract class BakingDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static volatile BakingDatabase newInstance;
    private static final String DATABASE_NAME = "recipe";

    //abstract methods for dao
    public abstract RecipeDao getRecipeDao();
    public abstract IngredientsDao getIngredientsDao();
    public abstract ThumbnailDao getThumbnailDao();
    public abstract StepThumbnailDao getStepsThumbnailDao();

    public static BakingDatabase getNewInstance(Context context){
        if (null == newInstance){
            synchronized (LOCK){
                newInstance = Room.databaseBuilder(context, BakingDatabase.class, DATABASE_NAME)
//                .fallbackToDestructiveMigration()
                .build();
            }
        }
        return newInstance;
    }
}
