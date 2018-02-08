package com.example.anu.bakingapp.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.anu.bakingapp.data.BakingRepository;

public class RecipeDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final BakingRepository bakingRepository;

    public RecipeDetailsViewModelFactory(BakingRepository repository) {
        this.bakingRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeDetailsViewModel(bakingRepository);
    }
}
