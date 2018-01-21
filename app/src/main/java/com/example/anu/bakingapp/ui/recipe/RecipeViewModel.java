package com.example.anu.bakingapp.ui.recipe;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.anu.bakingapp.data.BakingRepository;
import com.example.anu.bakingapp.data.database.Recipe;

import java.util.List;


public class RecipeViewModel extends ViewModel {
    public LiveData<List<Recipe>> getRecipeList() {
        return recipeList;
    }

    public RecipeViewModel(BakingRepository bakingRepository) {
        this.bakingRepository = bakingRepository;
        recipeList = bakingRepository.getRecipeList();
    }

    private LiveData<List<Recipe>> recipeList;
    private BakingRepository bakingRepository;

    public void deleteRecipes(){
        bakingRepository.deleteRecipes();
        bakingRepository.getRecipeList();
    }
}
