package com.nagy.zsolt.bakingapp.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nagy.zsolt.bakingapp.R;

public class IngredientsAdapter extends BaseAdapter {

    private final Context mContext;
    private final String[] ingredients, quantities, measure;

    public IngredientsAdapter(Context context, String[] ingredients, String[] quantities, String[] measure) {
        this.mContext = context;
        this.ingredients = ingredients;
        this.quantities = quantities;
        this.measure = measure;
    }

    @Override
    public int getCount() {
        return ingredients.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {


        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.list_item_ingredient, null);
        }

        final TextView tvIngredients = (TextView) convertView.findViewById(R.id.tvTitle3);
        final TextView tvQuantities = (TextView) convertView.findViewById(R.id.quantityValue);
        final TextView tvMeasure = (TextView) convertView.findViewById(R.id.measureValue);

        tvIngredients.setText(ingredients[position]);
        tvQuantities.setText(quantities[position]);
        tvMeasure.setText(measure[position]);

        return convertView;
    }
}
