package com.example.anu.bakingapp.ui.recipe;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.database.Recipe;
import com.example.anu.bakingapp.ui.adapter.RecipeAdapter;
import com.example.anu.bakingapp.utils.InjectorUtils;
import com.example.anu.bakingapp.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends LifecycleActivity implements RecipeAdapter.RecipeOnClickListener {

    @BindView(R.id.recycler_view_recipes)
    RecyclerView recyclerViewRecipes;
    @BindView(R.id.txt_error)
    TextView txtError;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.layout_error)
    RelativeLayout layoutError;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private RecipeAdapter mRecipeAdapter;
    private static final String TAG = RecipeActivity.class.getSimpleName();
    private int mPosition = RecyclerView.NO_POSITION;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        setupToolbar();

        RecipeViewModelFactory factory = InjectorUtils.provideRecipeActivityViewModelFactory(this);
        RecipeViewModel viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);

        viewModel.getRecipeList().observe(this, newRecipes -> {
            mRecipeAdapter.setmRecipeList(newRecipes);


//            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
//            recyclerViewRecipes.smoothScrollToPosition(mPosition);

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
     * method to set toolbar
     */
    private void setupToolbar() {
        toolbar.setTitle(getResources().getString(R.string.recipes));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
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
            swipeRefreshLayout.setEnabled(false);
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
    public void onRecipeClick(int recipeId) {
        Toast.makeText(this, String.valueOf(recipeId), Toast.LENGTH_SHORT).show();
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
