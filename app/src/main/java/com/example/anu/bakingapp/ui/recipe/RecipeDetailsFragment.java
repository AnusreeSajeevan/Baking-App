package com.example.anu.bakingapp.ui.recipe;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.database.Ingredient;
import com.example.anu.bakingapp.data.database.Recipe;
import com.example.anu.bakingapp.ui.StepDetailsActivity;
import com.example.anu.bakingapp.ui.adapter.IngredientsAdapter;
import com.example.anu.bakingapp.ui.adapter.StepsAdapter;
import com.example.anu.bakingapp.utils.BakingJsonUtils;
import com.example.anu.bakingapp.utils.InjectorUtils;

import org.json.JSONException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.anu.bakingapp.ui.recipe.RecipeDetailsActivity.EXTRA_CLICKED_POS;
import static com.example.anu.bakingapp.ui.recipe.RecipeDetailsActivity.EXTRA_STEPS;

/**
 * master list fragment
 */
public class RecipeDetailsFragment extends Fragment implements StepsAdapter.OnStepClickListener {

    @BindView(R.id.txt_ingredients_title)
    TextView txtIngredientsTitle;
    @BindView(R.id.recycler_view_ingredients)
    RecyclerView recyclerViewIngredients;
    @BindView(R.id.txt_steps_title)
    TextView txtStepsTitle;
    @BindView(R.id.recycler_view_steps)
    RecyclerView recyclerViewSteps;

    Unbinder unbinder;
    private static final String TAG = RecipeDetailsFragment.class.getSimpleName();
    private IngredientsAdapter mIngredientsAdapter;
    private StepsAdapter msStepsAdapter;
    private int recipeId;
    private static  List<Ingredient> ingredientList = new ArrayList<>();
    private List<Step> stepList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    private Recipe recipe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()){
            recipe = getArguments().getParcelable(RecipeActivity.KEY_RECIPE);

        }
    }

    public RecipeDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_recipe_details, container, false);

        unbinder = ButterKnife.bind(this, view);

        setUpIngredientsRecyclerView();
        setUpStepsRecyclerView();

        return view;
    }

    private void setUpStepsRecyclerView() {
        msStepsAdapter = new StepsAdapter(getActivity(), this);
        recyclerViewSteps.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewSteps.setAdapter(msStepsAdapter);
        recyclerViewSteps.setNestedScrollingEnabled(false);
        try {
            stepList = BakingJsonUtils.parseSteps(recipe.getSteps());
            msStepsAdapter.setStepsList(stepList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * method to set up {@literal recyclerViewIngredients}
     */
    private void setUpIngredientsRecyclerView() {
        mIngredientsAdapter = new IngredientsAdapter(getActivity());
        recyclerViewIngredients.setLayoutManager(linearLayoutManager);
        recyclerViewIngredients.setAdapter(mIngredientsAdapter);
        recyclerViewIngredients.setNestedScrollingEnabled(false);
        try {
            ingredientList = BakingJsonUtils.parseIngredients(recipe.getIngredients());
            mIngredientsAdapter.setIngredientList(ingredientList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviders.of(getActivity()).get(RecipeDetailsViewModel.class).getDataIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                Log.d("checkingredientss","onChanged");
                ingredientList = ingredients;
                mIngredientsAdapter.setIngredientList(ingredientList);
            }
        });

        ViewModelProviders.of(getActivity()).get(RecipeDetailsViewModel.class).getDataSteps().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                stepList = steps;
                msStepsAdapter.setStepsList(stepList);
            }
        });
    }*/

    @Override
    public void onStepClick(int position) {
        if (RecipeDetailsActivity.isTwoPaneUi){
            RecipeDetailsActivity.setStepFragment(position);
        }else {
            Log.d("CheckStepClick","onStepClick fragment");
            Intent i = new Intent(getActivity(), StepDetailsActivity.class);
            i.putParcelableArrayListExtra(EXTRA_STEPS, (ArrayList<? extends Parcelable>) stepList);
            i.putExtra(EXTRA_CLICKED_POS, position);
            startActivity(i);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("CheckPositions","ingreients : " + linearLayoutManager.findFirstVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }
}
