package com.example.anu.bakingapp.ui.recipe;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.anu.bakingapp.data.BakingRepository;

public class RecipeViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private BakingRepository repository;

    public RecipeViewModelFactory(BakingRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeViewModel(repository);
    }
}
