package com.example.anu.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.Ingredient;
import com.example.anu.bakingapp.data.Recipe;
import com.example.anu.bakingapp.data.Step;
import com.example.anu.bakingapp.ui.activity.RecipeDetailsActivity;
import com.example.anu.bakingapp.ui.activity.StepDetailsActivity;
import com.example.anu.bakingapp.ui.adapter.IngredientsAdapter;
import com.example.anu.bakingapp.ui.adapter.StepsAdapter;
import com.example.anu.bakingapp.ui.viewmodel.RecipeDetailsViewModel;
import com.example.anu.bakingapp.ui.viewmodel.RecipeDetailsViewModelFactory;
import com.example.anu.bakingapp.utils.BakingJsonUtils;
import com.example.anu.bakingapp.utils.InjectorUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.anu.bakingapp.utils.Constants.EXTRA_CLICKED_POS;
import static com.example.anu.bakingapp.utils.Constants.EXTRA_RECIPE_NAME;
import static com.example.anu.bakingapp.utils.Constants.EXTRA_STEPS;
import static com.example.anu.bakingapp.utils.Constants.KEY_RECIPE;

/**
 * master list fragment
 */
public class RecipeDetailsFragment extends Fragment implements StepsAdapter.OnStepClickListener {

    private static final String KEY_SCROLL_POSITION = "scroll_position";
    @BindView(R.id.txt_ingredients_title)
    TextView txtIngredientsTitle;
    @BindView(R.id.recycler_view_ingredients)
    RecyclerView recyclerViewIngredients;
    @BindView(R.id.txt_steps_title)
    TextView txtStepsTitle;
    @BindView(R.id.recycler_view_steps)
    RecyclerView recyclerViewSteps;
    @BindView(R.id.nested_sctollview)
    NestedScrollView nestedSctollview;

    private Unbinder unbinder;
    private static final String TAG = RecipeDetailsFragment.class.getSimpleName();
    private IngredientsAdapter mIngredientsAdapter;
    private StepsAdapter msStepsAdapter;
    private int recipeId;
    private static List<Ingredient> ingredientList = new ArrayList<>();
    private List<Step> stepList = new ArrayList<>();
    private LinearLayoutManager layoutManagerIngredients;
    private LinearLayoutManager layoutManagerSteps;
    private Recipe recipe;
    private RecipeDetailsViewModelFactory factory;
    public RecipeDetailsViewModel viewModel;
    private int currentVisiblePosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            recipe = getArguments().getParcelable(KEY_RECIPE);

        }
    }
    public RecipeDetailsFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //restore the current scroll position
        if (null != savedInstanceState){
            final int[] position = savedInstanceState.getIntArray("scroll_position");
            if(position != null)
                nestedSctollview.post(() -> nestedSctollview.scrollTo(position[0], position[1]));
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_recipe_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        factory = InjectorUtils.provideRecipeDetailsActivityViewModelFactory(getActivity());
        viewModel = ViewModelProviders.of(this, factory).get(RecipeDetailsViewModel.class);
        try {
            viewModel.getStepThumbnailList(recipe.getId()).observe(getActivity(), newStepsThumbnailList -> {
                if (newStepsThumbnailList.size() != 0)
                    msStepsAdapter.updateThumbnail(newStepsThumbnailList);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        setUpIngredientsRecyclerView();
        setUpStepsRecyclerView();
        return view;
    }

    private void setUpStepsRecyclerView() {
        layoutManagerSteps = new LinearLayoutManager(getActivity());
        msStepsAdapter = new StepsAdapter(getActivity(), this);
        recyclerViewSteps.setLayoutManager(layoutManagerSteps);
        recyclerViewSteps.setAdapter(msStepsAdapter);
        recyclerViewSteps.setNestedScrollingEnabled(false);
        try {
            stepList = BakingJsonUtils.parseSteps(recipeId, recipe.getSteps());
            msStepsAdapter.setStepsList(stepList);
            viewModel.setStepThumbnails(getActivity(), recipe.getId(), stepList);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * method to set up {@literal recyclerViewIngredients}
     */
    private void setUpIngredientsRecyclerView() {
        layoutManagerIngredients = new LinearLayoutManager(getActivity());
        mIngredientsAdapter = new IngredientsAdapter(getActivity());
        recyclerViewIngredients.setLayoutManager(layoutManagerIngredients);
        recyclerViewIngredients.setAdapter(mIngredientsAdapter);
        recyclerViewIngredients.setNestedScrollingEnabled(false);
        try {
            ingredientList = BakingJsonUtils.parseIngredients(recipeId, recipe.getIngredients());
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

    @Override
    public void onStepClick(int position) {
        if (RecipeDetailsActivity.isTwoPaneUi) {
            RecipeDetailsActivity recipeDetailsActivity = new RecipeDetailsActivity();
            recipeDetailsActivity.setStepFragment(position);
        } else {
            Intent i = new Intent(getActivity(), StepDetailsActivity.class);
            i.putParcelableArrayListExtra(EXTRA_STEPS, (ArrayList<? extends Parcelable>) stepList);
            i.putExtra(EXTRA_CLICKED_POS, position);
            i.putExtra(EXTRA_RECIPE_NAME, recipe.getName());
            startActivity(i);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //save current scroll position
        outState.putIntArray(KEY_SCROLL_POSITION,
             new int[]{ nestedSctollview.getScrollX(), nestedSctollview.getScrollY()});
    }
}
