package com.example.anu.bakingapp.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.anu.bakingapp.data.BakingRepository;
import com.example.anu.bakingapp.data.Ingredient;
import com.example.anu.bakingapp.data.Recipe;
import com.example.anu.bakingapp.data.Step;
import com.example.anu.bakingapp.data.StepThumbnail;

import java.util.List;


public class RecipeDetailsViewModel extends ViewModel {
    private LiveData<Recipe> recipe;
    private int recipeId;
    private final BakingRepository bakingRepository;
    private LiveData<Boolean> isAdded;
    private LiveData<List<StepThumbnail>> stepThumbnailList;
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


    public void setStepThumbnailList(LiveData<List<StepThumbnail>> stepThumbnailList) {
        this.stepThumbnailList = stepThumbnailList;
    }

    public LiveData<List<StepThumbnail>> getStepThumbnailList(int recipeId) {
        return bakingRepository.getStepThumbnailList(recipeId);
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

    public void setStepThumbnails(Context context, int recipeId, List<Step> stepList) throws Throwable {
        bakingRepository.getStepThumbnails(context, recipeId, stepList);
    }

   /* public LiveData<List<StepThumbnail>> getStepThumbnailsList(int recipeId) throws Throwable {
        return bakingRepository.setStepThumbnails(recipeId);
    }*/
}
