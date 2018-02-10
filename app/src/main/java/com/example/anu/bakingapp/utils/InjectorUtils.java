/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.anu.bakingapp.utils;

import android.content.Context;

import com.example.anu.bakingapp.AppExecutors;
import com.example.anu.bakingapp.data.BakingRepository;
import com.example.anu.bakingapp.data.database.BakingDatabase;
import com.example.anu.bakingapp.data.network.BakingNetworkDataSource;
import com.example.anu.bakingapp.ui.viewmodel.RecipeDetailsViewModelFactory;
import com.example.anu.bakingapp.ui.viewmodel.RecipeViewModelFactory;

/**
 * Provides static methods to inject the various classes
 */
public class InjectorUtils {

    private static BakingRepository provideRepository(Context context) {
        BakingDatabase database = BakingDatabase.getNewInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        BakingNetworkDataSource networkDataSource =
                BakingNetworkDataSource.getNewInstance(context.getApplicationContext(), executors);
        return BakingRepository.getInstance(database.getRecipeDao(), database.getIngredientsDao(), database.getThumbnailDao(),
                networkDataSource, executors);
    }

    public static RecipeViewModelFactory provideRecipeActivityViewModelFactory(Context context) {
        BakingRepository repository = provideRepository(context.getApplicationContext());
        return new RecipeViewModelFactory(repository);
    }

    public static RecipeDetailsViewModelFactory provideRecipeDetailsActivityViewModelFactory(Context context) {
        BakingRepository repository = provideRepository(context.getApplicationContext());
        return new RecipeDetailsViewModelFactory(repository);
    }

}