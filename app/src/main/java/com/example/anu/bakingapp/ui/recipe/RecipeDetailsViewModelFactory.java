package com.example.anu.bakingapp.ui.recipe;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.anu.bakingapp.data.BakingRepository;
import com.example.anu.bakingapp.data.database.Recipe;

public class RecipeDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private BakingRepository bakingRepository;

    public RecipeDetailsViewModelFactory(BakingRepository repository) {
        this.bakingRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeDetailsViewModel(bakingRepository);
    }
}
