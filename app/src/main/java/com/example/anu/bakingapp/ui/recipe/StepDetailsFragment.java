package com.example.anu.bakingapp.ui.recipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anu.bakingapp.R;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * fragment that displays recipe step details
 */
public class StepDetailsFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.txt_description)
    TextView txtDescription;

    /**
     * empty constructor required for instantiating the fragment
     */
    public StepDetailsFragment() {
    }

    /**
     * inflates {@literal fragment_step_details} layout
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_step_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        txtDescription.setText("description...");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
