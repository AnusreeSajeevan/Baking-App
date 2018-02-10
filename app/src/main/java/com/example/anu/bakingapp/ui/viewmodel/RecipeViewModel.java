package com.example.anu.bakingapp.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.example.anu.bakingapp.data.BakingRepository;
import com.example.anu.bakingapp.data.Recipe;
import com.example.anu.bakingapp.data.Thumbnail;
import com.example.anu.bakingapp.service.ListWidgetService;

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

    public void updateRecipeList(Thumbnail thumbnail){
        recipeList.getValue().get(0).setThumbnailPath(thumbnail.getPath());
    }


    public void getThumbnailUrls(Context context, List<Recipe> recipes) throws Throwable {
        Log.d("ThumbnailCountCheck","RecipeViewModel");
        bakingRepository.updateThumbnailUrls(context, recipes);
    }
}
