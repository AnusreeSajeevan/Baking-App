package com.example.anu.bakingapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.Step;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsHolder> {

    private List<Step> mStepsList;
    private Context mContext;
    private OnStepClickListener stepClickListener;

    public StepsAdapter(Context mContext, OnStepClickListener stepClickListener) {
        this.mContext = mContext;
        this.stepClickListener = stepClickListener;
    }

    public interface OnStepClickListener{
        void onStepClick(int position);
    }

    @Override
    public StepsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_steps_item, parent, false);
        return new StepsHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsHolder holder, final int position) {
        final Step steps = mStepsList.get(holder.getAdapterPosition());
        holder.txtBriefDescription.setText(steps.getShortDescription());
        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepClickListener.onStepClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mStepsList.size() != 0)
            return mStepsList.size();
        else
            return 0;
    }

    /**
     * method to set steps list
     *
     * @param stepsList
     */
    public void setStepsList(List<Step> stepsList) {
        this.mStepsList = stepsList;
        notifyDataSetChanged();
    }
}
