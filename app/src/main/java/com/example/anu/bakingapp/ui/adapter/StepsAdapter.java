package com.example.anu.bakingapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.Step;
import com.example.anu.bakingapp.data.StepThumbnail;
import com.example.anu.bakingapp.utils.GlideApp;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsHolder> {

    private List<Step> mStepsList;
    private final Context mContext;
    private final OnStepClickListener stepClickListener;

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
        holder.layoutMain.setOnClickListener(view -> stepClickListener.onStepClick(holder.getAdapterPosition()));

        GlideApp.with(holder.imgBack)
                .load(steps.getThumbnailPath())
                .placeholder(R.drawable.thumbnail_placeholder)
                .error(R.drawable.thumbnail_placeholder)
                .into(holder.imgBack);
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

    /**
     * method to update step thumbnail
     *
     * @param stepThumbnails
     */
    public void updateThumbnail(List<StepThumbnail> stepThumbnails) {
        Log.d("checkkkkThumbnails","mStepsList size : " + mStepsList.size());
        Log.d("checkkkkThumbnails","stepThumbnails size : " + stepThumbnails.size());
        for (int i=0; i<stepThumbnails.size(); i++){
            Log.d("CheckStepThumbnailss","i : "+ i);
           StepThumbnail stepThumbnail = stepThumbnails.get(i);
            Log.d("CheckStepThumbnailss","stepThumbnail.getStepId() : "+ stepThumbnail.getStepId());
            if (stepThumbnail.getStepId()<mStepsList.size()) {
                if (mStepsList.get(stepThumbnail.getStepId()) != null) {
                    mStepsList.get(stepThumbnail.getStepId()).setThumbnailPath(stepThumbnail.getThumbnailPath());
                }
            }
        }
        notifyDataSetChanged();
    }
}
