package com.example.anu.bakingapp.ui.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.anu.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txt_brief_description)
    TextView txtBriefDescription;
    @BindView(R.id.layout_main)
    CardView layoutMain;
    @BindView(R.id.img_back)
    AppCompatImageView imgBack;

    public StepsHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
