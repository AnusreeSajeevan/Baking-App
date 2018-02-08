package com.example.anu.bakingapp.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.Ingredient;
import com.example.anu.bakingapp.data.Recipe;
import com.example.anu.bakingapp.ui.StepDetailsMainFragment;
import com.example.anu.bakingapp.ui.RecipeDetailsFragment;
import com.example.anu.bakingapp.ui.viewmodel.RecipeDetailsViewModel;
import com.example.anu.bakingapp.ui.viewmodel.RecipeDetailsViewModelFactory;
import com.example.anu.bakingapp.data.Step;
import com.example.anu.bakingapp.utils.BakingJsonUtils;
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
    public static final String EXTRA_IS_TABLET = "com.example.anu.bakingapp.extdra.IS_TABLET";
    private static int recipeId;
    private static Recipe recipe;
    static FragmentManager fragmentManager;
    private RecipeDetailsViewModelFactory factory;
    private RecipeDetailsViewModel viewModel;

    @BindView(R.id.master_list_fragment_container)
    FrameLayout masterListFragment;
    public static boolean isTwoPaneUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();

        Log.d(TAG, "onCreate : ");
//        recipeId = getIntent().getIntExtra(RecipeActivity.KEY_RECIPE_ID, -1);
        recipeId = getIntent().getIntExtra(RecipeActivity.KEY_RECIPE_ID, -1);
        Log.d("checkChanged","recipeId : " + recipeId);

        factory = InjectorUtils.provideRecipeDetailsActivityViewModelFactory(getApplicationContext());
        viewModel = ViewModelProviders.of(this, factory).get(RecipeDetailsViewModel.class);
        viewModel.setRecipeId(recipeId);
        viewModel.getRecipe().observe(this, new Observer<Recipe>() {
          @Override
          public void onChanged(@Nullable Recipe recipes) {
              recipe = recipes;

              getSupportActionBar().setTitle(recipes.getName());

              Fragment fragment = new RecipeDetailsFragment();
              Bundle bundle = new Bundle();
              bundle.putParcelable(RecipeActivity.KEY_RECIPE, recipe);
              fragment.setArguments(bundle);
              Log.d("checkinactivity","recipe : " + recipe);
              FragmentManager fragmentManager = getSupportFragmentManager();
              fragmentManager.beginTransaction()
                      .replace(R.id.master_list_fragment_container, fragment)
                      .commit();
          }
      });

        viewModel.getIsAdded().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isAddedToWidget) {
                if (isAddedToWidget)
                    Toast.makeText(RecipeDetailsActivity.this, "Recipe added to widget", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(RecipeDetailsActivity.this, "Cannot add", Toast.LENGTH_SHORT).show();
            }
        });


        if (null != findViewById(R.id.layout_two_pane)){
            isTwoPaneUi = true;
        }
        else {
            isTwoPaneUi = false;
        }


    }

    public static void setStepFragment(int position) {
        Log.d("replaceFragment","position : " + position);
        Fragment fragment = new StepDetailsMainFragment();
        Bundle bundle = new Bundle();
        try {
         Fragment f = fragmentManager.findFragmentById(R.id.container_step_details);
            if(f == null) {
                List<Step> stepsList = BakingJsonUtils.parseSteps(recipe.getSteps().toString());
                bundle.putParcelableArrayList(EXTRA_STEPS, (ArrayList<? extends Parcelable>) stepsList);
                bundle.putInt(RecipeDetailsActivity.EXTRA_CLICKED_POS, position);
                bundle.putBoolean(RecipeDetailsActivity.EXTRA_IS_TABLET, true);
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.container_step_details, fragment)
                        .commit();
                Log.d("replaceFragment","if");
            }
            else {


              /*  Log.d("replaceFragment","else");
                Bundle bdl = f.getArguments();
                bdl.putInt(RecipeDetailsActivity.EXTRA_CLICKED_POS, position);*/
            }
          /*  List<Step> stepsList = BakingJsonUtils.parseSteps(recipe.getSteps().toString());
            bundle.putParcelableArrayList(EXTRA_STEPS, (ArrayList<? extends Parcelable>) stepsList);
            bundle.putInt(RecipeDetailsActivity.EXTRA_CLICKED_POS, position);
            bundle.putBoolean(RecipeDetailsActivity.EXTRA_IS_TABLET, true);
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .addToBackStack(fragment.getClass().getName())
                    .replace(R.id.container_step_details, fragment)
                    .commit();*/
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
                    viewModel.insertIngredients(BakingJsonUtils.parseIngredients(recipe.getIngredients()));
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
