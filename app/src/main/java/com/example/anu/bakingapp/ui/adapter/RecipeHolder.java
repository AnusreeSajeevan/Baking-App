package com.example.anu.bakingapp.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anu.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.txt_recipe_name)
    TextView txtRecipeName;
    @BindView(R.id.txt_ingredients_count)
    TextView txtIngredientsCount;
    @BindView(R.id.layout_main)
    CardView layoutMain;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_num_steps)
    TextView txtNumSteps;
    @BindView(R.id.txt_servings)
    TextView txtServings;

    public RecipeHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
