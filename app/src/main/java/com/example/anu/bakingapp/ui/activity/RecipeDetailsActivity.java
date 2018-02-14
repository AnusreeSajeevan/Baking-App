package com.example.anu.bakingapp.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.Recipe;
import com.example.anu.bakingapp.ui.RecipeDetailsFragment;
import com.example.anu.bakingapp.ui.StepDetailsMainFragment;
import com.example.anu.bakingapp.ui.viewmodel.RecipeDetailsViewModel;
import com.example.anu.bakingapp.ui.viewmodel.RecipeDetailsViewModelFactory;
import com.example.anu.bakingapp.data.Step;
import com.example.anu.bakingapp.utils.BakingJsonUtils;
import com.example.anu.bakingapp.utils.CurrentRecipeUtil;
import com.example.anu.bakingapp.utils.InjectorUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity{

    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();
    public static final String EXTRA_STEPS = "com.example.anu.bakingapp.extra.STEPS";
    public static final String EXTRA_CLICKED_POS = "com.example.anu.bakingapp.extdra.CLICKED_POS";
    private static final String EXTRA_IS_TABLET = "com.example.anu.bakingapp.extdra.IS_TABLET";
    private static int recipeId;
    private static Recipe recipe;
    private static FragmentManager fragmentManager;
    private RecipeDetailsViewModelFactory factory;
    private RecipeDetailsViewModel viewModel;
    public static boolean isTwoPaneUi;
    private CurrentRecipeUtil currentRecipeUtil;
    private boolean isAddToWidgetClicked = false;

    @BindView(R.id.master_list_fragment_container)
    FrameLayout masterListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        currentRecipeUtil = new CurrentRecipeUtil(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();

        recipeId = getIntent().getIntExtra(RecipeActivity.KEY_RECIPE_ID, -1);

        factory = InjectorUtils.provideRecipeDetailsActivityViewModelFactory(getApplicationContext());
        viewModel = ViewModelProviders.of(this, factory).get(RecipeDetailsViewModel.class);
        viewModel.setRecipeId(recipeId);
        viewModel.getRecipe().observe(this, recipes -> {
            recipe = recipes;

            getSupportActionBar().setTitle(recipes != null ? recipes.getName() : null);

            Fragment fragment = new RecipeDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(RecipeActivity.KEY_RECIPE, recipe);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.master_list_fragment_container, fragment)
                    .commit();
        });

        viewModel.getIsAdded().observe(this, isAddedToWidget -> {

            if (isAddToWidgetClicked){

                if (isAddedToWidget) {
                    Toast.makeText(RecipeDetailsActivity.this, "Recipe added to widget", Toast.LENGTH_SHORT).show();
                    currentRecipeUtil.setLatestRecipeDetails(recipeId, recipe.getName());

                }
                else
                    Toast.makeText(RecipeDetailsActivity.this, "Cannot add", Toast.LENGTH_SHORT).show();
            }
        });


        isTwoPaneUi = null != findViewById(R.id.layout_two_pane);


    }

    public void setStepFragment(int position) {
        Fragment fragment = new StepDetailsMainFragment();
        Bundle bundle = new Bundle();
        try {
            List<Step> stepsList = BakingJsonUtils.parseSteps(recipeId, recipe.getSteps());
            Fragment f = fragmentManager.findFragmentById(R.id.container_step_details);
            if(f != null) {
                fragmentManager.beginTransaction().remove(fragmentManager.findFragmentById(R.id.container_step_details)).commit();
            }
            bundle.putParcelableArrayList(EXTRA_STEPS, (ArrayList<? extends Parcelable>) stepsList);
            bundle.putInt(RecipeDetailsActivity.EXTRA_CLICKED_POS, position);
            bundle.putBoolean(RecipeDetailsActivity.EXTRA_IS_TABLET, true);
            fragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .add(R.id.container_step_details, fragment)
                    .commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add_to_widget:
                try {
                    isAddToWidgetClicked = true;
                    viewModel.insertIngredients(BakingJsonUtils.parseIngredients(recipe.getId(),recipe.getIngredients()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_to_widget, menu);
        return true;
    }
}
