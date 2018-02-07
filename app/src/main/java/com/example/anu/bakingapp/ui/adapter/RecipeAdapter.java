package com.example.anu.bakingapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.database.Recipe;
import com.example.anu.bakingapp.utils.BakingJsonUtils;
import com.example.anu.bakingapp.utils.RecipeImageUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {
    private Context mContext;
    private List<Recipe> mRecipeList;
    private RecipeOnClickListener mRecipeOnClickListener;

    private static final String TAG = RecipeAdapter.class.getSimpleName();

    /**
     * interface to handle recipe selection
     */
    public interface RecipeOnClickListener {
        void onRecipeClick(int recipeId);
    }

    public RecipeAdapter(Context mContext, RecipeOnClickListener mRecipeOnClickListener) {
        this.mContext = mContext;
        this.mRecipeOnClickListener = mRecipeOnClickListener;
        this.mRecipeList = new ArrayList<>();
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_recipe_item, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeHolder holder, int position) {
        Recipe recipe = mRecipeList.get(holder.getAdapterPosition());
        holder.txtRecipeName.setText(recipe.getName());

        Picasso.with(mContext)
                .load(RecipeImageUtils.getRecipeImages().get(holder.getAdapterPosition()))
                .into(holder.imgBack);

        try {
            int ingredientsCount = 0;
            ingredientsCount = BakingJsonUtils.getIngredientsCount(recipe.getIngredients());
            holder.txtIngredientsCount.setText(String.valueOf(ingredientsCount));

            int stepsCount = 0;
            stepsCount = BakingJsonUtils.getStepsCount(recipe.getSteps());
            holder.txtNumSteps.setText(mContext.getResources().getQuantityString(R.plurals.step_count, stepsCount, stepsCount));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.layoutMain.setOnClickListener(view -> mRecipeOnClickListener.onRecipeClick(recipe.getId()));
    }


    @Override
    public int getItemCount() {
        if (mRecipeList.isEmpty())
            return 0;
        else
            return mRecipeList.size();
    }

    /**
     * method to update the recipe list
     *
     * @param recipeList new recipe list
     */
    public void setmRecipeList(List<Recipe> recipeList) {
        this.mRecipeList = recipeList;
        notifyDataSetChanged();
    }
}
