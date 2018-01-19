package com.example.anu.bakingapp.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Recipe {
    @PrimaryKey
    private int id;

    private String name;
}
