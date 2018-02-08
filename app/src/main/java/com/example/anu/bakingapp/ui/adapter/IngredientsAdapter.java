package com.example.anu.bakingapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.Ingredient;

import java.util.List;

/**
 * Created by Design on 23-01-2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsHolder> {

    private List<Ingredient> mIngredientList;
    private Context mContext;

    public IngredientsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public IngredientsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ingredients_item, parent, false);
        return new IngredientsHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsHolder holder, int position) {
        Ingredient ingredient = mIngredientList.get(holder.getAdapterPosition());
        holder.txtIngredient.setText(ingredient.getIngredient());
        holder.txtQuantityMeasurement.setText(ingredient.getQuantity() + mContext.getResources().getString(R.string.space) + ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        if (mIngredientList.size() != 0)
            return mIngredientList.size();
        else
            return 0;
    }

    /**
     * method to set ingredients list
     * @param ingredientList
     */
    public void setIngredientList(List<Ingredient> ingredientList){
        this.mIngredientList = ingredientList;
        notifyDataSetChanged();
    }
}
