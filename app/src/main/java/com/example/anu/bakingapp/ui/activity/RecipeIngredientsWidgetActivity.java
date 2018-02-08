package com.example.anu.bakingapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.anu.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientsWidgetActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @BindView(R.id.recyclerview_ingredients)
    RecyclerView recyclerviewIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients_widget);
        ButterKnife.bind(this);
    }
}
