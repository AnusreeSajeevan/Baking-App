package com.example.anu.bakingapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.anu.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txt_ingredient)
    TextView txtIngredient;
    @BindView(R.id.txt_quantity_measurement)
    TextView txtQuantityMeasurement;

    public IngredientsHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
