package com.example.anu.bakingapp.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.Recipe;
import com.example.anu.bakingapp.ui.adapter.RecipeAdapter;
import com.example.anu.bakingapp.ui.viewmodel.RecipeViewModel;
import com.example.anu.bakingapp.ui.viewmodel.RecipeViewModelFactory;
import com.example.anu.bakingapp.utils.InjectorUtils;
import com.example.anu.bakingapp.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity implements RecipeAdapter.RecipeOnClickListener {

    @BindView(R.id.recycler_view_recipes)
    RecyclerView recyclerViewRecipes;
    @BindView(R.id.txt_error)
    TextView txtError;
    @BindView(R.id.imageView)
    android.support.v7.widget.AppCompatImageView imageView;
    @BindView(R.id.layout_error)
    RelativeLayout layoutError;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private RecipeAdapter mRecipeAdapter;
    private static final String TAG = RecipeActivity.class.getSimpleName();
    private int mPosition = RecyclerView.NO_POSITION;
    private BroadcastReceiver broadcastReceiver;
    public static final String KEY_RECIPE_ID = "recipe_id";
    public static final String KEY_RECIPE = "recipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        RecipeViewModelFactory factory = InjectorUtils.provideRecipeActivityViewModelFactory(this);
        RecipeViewModel viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);

        viewModel.getRecipeList().observe(this, newRecipes -> {
            mRecipeAdapter.setmRecipeList(newRecipes);
            setViewsVisibility(newRecipes);

        });

        setupRecyclerView();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(RecipeActivity.this, "Received", Toast.LENGTH_SHORT).show();
                onStart();
            }
        };

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (NetworkUtils.isNetworkAvailable(RecipeActivity.this)) {
                viewModel.deleteRecipes();
            }
            else {
                swipeRefreshLayout.setRefreshing(false);
                txtError.setText(getResources().getString(R.string.no_internet_connection));
            }
        });
    }

    /**
     * method to toggle view's visibility
     *
     * @param newRecipes new recipe list
     */
    private void setViewsVisibility(List<Recipe> newRecipes) {
        if (newRecipes != null && newRecipes.size() != 0) {
            swipeRefreshLayout.setRefreshing(false);
            layoutError.setVisibility(View.GONE);
            recyclerViewRecipes.setVisibility(View.VISIBLE);
//            swipeRefreshLayout.setEnabled(false);
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(true);
            if (NetworkUtils.isNetworkAvailable(this)) {
                txtError.setText(getResources().getString(R.string.fetching));
            } else {
                txtError.setText(getResources().getString(R.string.no_internet_connection));
            }
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
            layoutError.setVisibility(View.VISIBLE);
            recyclerViewRecipes.setVisibility(View.GONE);
        }
    }

    /**
     * method to set up the adapter and recyclerview
     */
    private void setupRecyclerView() {
        int columnCount = getColumnCount();
        recyclerViewRecipes.setLayoutManager(new GridLayoutManager(this, columnCount));
        recyclerViewRecipes.setNestedScrollingEnabled(false);
        mRecipeAdapter = new RecipeAdapter(this, this);
        recyclerViewRecipes.setAdapter(mRecipeAdapter);
    }

    /**
     * method to get the column count of recipe cards, based on screen orientation,
     * 1 for portrait, 2 for landscape
     *
     * @return column count
     */
    private int getColumnCount() {
        int count;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            count = 1;
        else
            count = 3;

        return count;
    }

    /**
     * method to handle item click on recipe list
     *
     * @param recipeId clicked recipe id
     */
    @Override
    public void onRecipeClick(int  recipeId) {
        Intent i = new Intent(RecipeActivity.this, RecipeDetailsActivity.class);
        i.putExtra(KEY_RECIPE_ID, recipeId);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("connection_state_changed"));
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
