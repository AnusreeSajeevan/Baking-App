package com.example.anu.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.model.Step;
import com.example.anu.bakingapp.ui.adapter.StepsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepDetailsMainFragment extends Fragment {

    private static final String TAG = StepDetailsMainFragment.class.getSimpleName();

    private static int currentStepPos;
    public List<Step> stepList = new ArrayList<>();
    FragmentPagerAdapter fragmentPagerAdapter;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            currentStepPos = getArguments().getInt(RecipeDetailsActivity.EXTRA_CLICKED_POS, -1);
            stepList = getArguments().getParcelableArrayList(RecipeDetailsActivity.EXTRA_STEPS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_step_details_main, container, false);

        unbinder = ButterKnife.bind(this, view);

        fragmentPagerAdapter = new StepsPagerAdapter(getActivity().getSupportFragmentManager(), stepList);
        viewPager.setAdapter(fragmentPagerAdapter);
        Log.d(TAG, "currentStepPos : " + currentStepPos);
        viewPager.setCurrentItem(currentStepPos);
        getActivity().setTitle(stepList.get(currentStepPos).getShortDescription());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getActivity().setTitle(stepList.get(position).getShortDescription());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    /**
     * method to get clicked step details
     * @param steps
     * @return
     */
    private Step getClickedStepDetails(List<Step> steps) {
        return steps.get(currentStepPos);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}