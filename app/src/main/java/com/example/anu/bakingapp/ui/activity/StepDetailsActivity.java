package com.example.anu.bakingapp.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.Step;
import com.example.anu.bakingapp.ui.StepDetailsMainFragment;
import com.example.anu.bakingapp.utils.CurrentRecipeUtil;

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
    private CurrentRecipeUtil currentRecipeUtil;
//    public static OrientationCallbacks orientationCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        steps = getIntent().getParcelableArrayListExtra(EXTRA_STEPS);
        clickedPos = getIntent().getIntExtra(EXTRA_CLICKED_POS, -1);
        recipeName = getIntent().getStringExtra(EXTRA_RECIPE_NAME);

        currentRecipeUtil = new CurrentRecipeUtil(this);
        setOrientation();

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

    public interface OrientationCallbacks{
        void onOrientationChange();
    }


    /**
     * method to set the orientation
     */
    private void setOrientation() {
        currentRecipeUtil.setOrientation(getResources().getConfiguration().orientation);
       /* orientationCallbacks = new OrientationCallbacks() {
            @Override
            public void onOrientationChange() {
                Toast.makeText(StepDetailsActivity.this, "Changes",Toast.LENGTH_SHORT).show();

            }
        };

        orientationCallbacks.onOrientationChange();*/
    }

    private void setupFragment(Bundle savedInstanceState, int stepIndex) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipeName  + " " + getResources().getString(R.string.steps));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                getSupportActionBar().hide();
            }
        }
        Fragment fragment = new StepDetailsMainFragment();
        if (null == savedInstanceState){
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(EXTRA_STEPS, (ArrayList<? extends Parcelable>) steps);
            bundle.putInt(EXTRA_CLICKED_POS, stepIndex);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().
                    replace(R.id.container_main, fragment)
                    .commit();
        }
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
