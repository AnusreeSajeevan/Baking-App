package com.example.anu.bakingapp.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.anu.bakingapp.data.BakingRepository;
import com.example.anu.bakingapp.data.Recipe;


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
