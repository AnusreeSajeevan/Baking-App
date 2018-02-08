package com.example.anu.bakingapp.ui.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.ui.StepDetailsMainFragment;
import com.example.anu.bakingapp.data.Step;

import java.util.ArrayList;
import java.util.List;

import static com.example.anu.bakingapp.ui.activity.RecipeDetailsActivity.EXTRA_STEPS;


public class StepDetailsActivity extends AppCompatActivity {

    private List<Step> steps = new ArrayList<>();
    private int clickedPos;
    private static final String TAG = StepDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        steps = getIntent().getParcelableArrayListExtra(EXTRA_STEPS);
        clickedPos = getIntent().getIntExtra(RecipeDetailsActivity.EXTRA_CLICKED_POS, -1);

        Fragment fragment = new StepDetailsMainFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_STEPS, (ArrayList<? extends Parcelable>) steps);
        bundle.putInt(RecipeDetailsActivity.EXTRA_CLICKED_POS, clickedPos);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().
                add(R.id.container_main, fragment)
                .commit();
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
