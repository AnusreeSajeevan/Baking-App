package com.example.anu.bakingapp.ui.recipe;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.anu.bakingapp.data.BakingRepository;
import com.example.anu.bakingapp.data.database.Ingredient;
import com.example.anu.bakingapp.data.database.Recipe;
import com.example.anu.bakingapp.utils.BakingJsonUtils;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RecipeDetailsViewModel extends ViewModel {
    private LiveData<Recipe> recipe;
    private int recipeId;
    private BakingRepository bakingRepository;

    public RecipeDetailsViewModel(BakingRepository bakingRepository) {
       this.bakingRepository = bakingRepository;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public LiveData<Recipe> getRecipe() {
        Log.d("CheckRepoo", "1 1  1 : " +bakingRepository.getRecipeWithId(recipeId));
        return bakingRepository.getRecipeWithId(recipeId);
    }

    public void setRecipe(LiveData<Recipe> recipe) {
        this.recipe = recipe;
    }
}
