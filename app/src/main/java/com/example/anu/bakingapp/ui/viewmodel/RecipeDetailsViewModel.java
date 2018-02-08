package com.example.anu.bakingapp.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.anu.bakingapp.data.BakingRepository;
import com.example.anu.bakingapp.data.Ingredient;
import com.example.anu.bakingapp.data.Recipe;

import java.util.List;


public class RecipeDetailsViewModel extends ViewModel {
    private LiveData<Recipe> recipe;
    private int recipeId;
    private BakingRepository bakingRepository;
    private LiveData<Boolean> isAdded;

    public RecipeDetailsViewModel(BakingRepository bakingRepository) {
       this.bakingRepository = bakingRepository;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public LiveData<Recipe> getRecipe() {
        return bakingRepository.getRecipeWithId(recipeId);
    }

    public void setRecipe(LiveData<Recipe> recipe) {
        this.recipe = recipe;
    }

    /**
     * wrapper method to insert ingredients to the database,
     * this way, the implementation for insert is completely hidden from the ui
     * @param ingredients list of ingredients
     */
    public void insertIngredients(List<Ingredient> ingredients){
        bakingRepository.insertIngredients(ingredients);
    }

    public LiveData<Boolean> getIsAdded() {
        return bakingRepository.getIsAddedToWidget();
    }
}
