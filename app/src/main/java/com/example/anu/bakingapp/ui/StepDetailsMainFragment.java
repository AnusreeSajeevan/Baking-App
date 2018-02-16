package com.example.anu.bakingapp.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.anu.bakingapp.utils.Constants.EXTRA_CLICKED_POS;
import static com.example.anu.bakingapp.utils.Constants.EXTRA_STEPS;
import static com.example.anu.bakingapp.utils.Constants.KEY_PAGE;
import static com.example.anu.bakingapp.utils.Constants.KEY_STEP;

public class StepDetailsMainFragment extends Fragment {

    private static final String TAG = StepDetailsMainFragment.class.getSimpleName();

    private static int currentStepPos;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<Step> stepList = new ArrayList<>();
    private Unbinder unbinder;
    private boolean isTabletView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            currentStepPos = getArguments().getInt(EXTRA_CLICKED_POS, -1);
            stepList = getArguments().getParcelableArrayList(EXTRA_STEPS);
            isTabletView = getArguments().getBoolean(EXTRA_STEPS);
        }
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_step_details_main_new, container, false);

        unbinder = ButterKnife.bind(this, view);
        if (null != getArguments()) {
            setupViewPager(viewpager);
            tabs.setupWithViewPager(viewpager);
            activateClickedTab(currentStepPos);
        }
        return view;
    }

    public void activateClickedTab(int pos) {
        tabs.getTabAt(currentStepPos).select();
    }

    private void setupViewPager(ViewPager viewpager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        for (int i=0;i<stepList.size();i++){
            Bundle bundle = new Bundle();

            bundle.putInt(KEY_PAGE, i);
            bundle.putParcelable(KEY_STEP, stepList.get(i));

            Fragment fragment = new StepDetailsFragment();
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, stepList.get(i).getShortDescription());
        }
        viewpager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /*
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

    public static void setParameters(int position) {
        currentStepPos = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_CLICKED_POS, currentStepPos);
        outState.putParcelableArrayList(EXTRA_STEPS, (ArrayList<? extends Parcelable>) stepList);
    }

}
