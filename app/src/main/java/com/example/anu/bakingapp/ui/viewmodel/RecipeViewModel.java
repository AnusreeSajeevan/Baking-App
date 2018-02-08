package com.example.anu.bakingapp.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.anu.bakingapp.data.BakingRepository;
import com.example.anu.bakingapp.data.Recipe;

import java.util.List;


public class RecipeViewModel extends ViewModel {
    public LiveData<List<Recipe>> getRecipeList() {
        return recipeList;
    }

    public RecipeViewModel(BakingRepository bakingRepository) {
        this.bakingRepository = bakingRepository;
        recipeList = bakingRepository.getRecipeList();
    }

    private final LiveData<List<Recipe>> recipeList;
    private final BakingRepository bakingRepository;

    public void deleteRecipes(){
        bakingRepository.deleteRecipes();
        bakingRepository.getRecipeList();
    }
}
