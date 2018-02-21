package com.example.anu.bakingapp.ui.activity;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.Step;
import com.example.anu.bakingapp.ui.StepDetailsMainFragment;
import com.example.anu.bakingapp.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.anu.bakingapp.utils.Constants.EXTRA_CLICKED_POS;
import static com.example.anu.bakingapp.utils.Constants.EXTRA_RECIPE_NAME;
import static com.example.anu.bakingapp.utils.Constants.EXTRA_STEPS;


public class StepDetailsActivity extends AppCompatActivity {

    private List<Step> steps = new ArrayList<>();
    private int clickedPos;
    private String recipeName;
    private static final String TAG = StepDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        steps = getIntent().getParcelableArrayListExtra(EXTRA_STEPS);
        clickedPos = getIntent().getIntExtra(EXTRA_CLICKED_POS, -1);
        recipeName = getIntent().getStringExtra(EXTRA_RECIPE_NAME);
        setupFragment(savedInstanceState, clickedPos);
       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(recipeName  + " " +
                getResources().getString(R.string.steps));


        if (null == savedInstanceState){
            Fragment fragment = new StepDetailsMainFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(EXTRA_STEPS, (ArrayList<? extends Parcelable>) steps);
            bundle.putInt(EXTRA_CLICKED_POS, clickedPos);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().
                    add(R.id.container_main, fragment)
                    .commit();
        }*/
    }

    private void setupFragment(Bundle savedInstanceState, int stepIndex) {
        Log.d("NewCheckinggggg","setupFragment");
        if (getSupportActionBar() != null) {
            Log.d("NewCheckinggggg","if 1");
            getSupportActionBar().setTitle(steps.get(stepIndex).getShortDescription());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Log.d("NewCheckinggggg","if 2");
                getSupportActionBar().hide();
            }
        }

        if (null == savedInstanceState){
            Fragment fragment = new StepDetailsMainFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(EXTRA_STEPS, (ArrayList<? extends Parcelable>) steps);
            bundle.putInt(EXTRA_CLICKED_POS, stepIndex);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().
                    replace(R.id.container_main, fragment)
                    .commit();
        }

/*

        if (savedInstanceState != null) {
            fragment = (StepDetailsMainFragment) getSupportFragmentManager().getFragment(savedInstanceState, "StepDetailsMainFragment");
        } else {
            fragment = StepDetailsMainFragment.newInstance(steps.get(stepIndex), false);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rl_container, fragment)
                .commit();*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
