package com.example.anu.bakingapp.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Recipe.class}, version = 1)
public abstract class BakingDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static volatile BakingDatabase newInstance;
    private static final String DATABASE_NAME = "recipe";

    //abstract methods for dao
    public abstract RecipeDao getRecipeDao();

    public static BakingDatabase getNewInstance(Context context){
        if (null == newInstance){
            synchronized (LOCK){
                newInstance = Room.databaseBuilder(context, BakingDatabase.class, DATABASE_NAME).build();
            }
        }
        return newInstance;
    }
}
