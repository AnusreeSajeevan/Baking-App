package com.example.anu.bakingapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.anu.bakingapp.model.Step;
import com.example.anu.bakingapp.ui.StepDetailsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Design on 04-02-2018.
 */

public class StepsPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_PAGES;
    private List<Step> stepList = new ArrayList<>();

    public StepsPagerAdapter(FragmentManager fm, List<Step> stepList) {
        super(fm);
        NUM_PAGES = stepList.size();
        this.stepList = stepList;
    }

    @Override
    public Fragment getItem(int position) {
        if (position < NUM_PAGES)
            return StepDetailsFragment.newInstance(position, stepList.get(position));
        else
            return null;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
