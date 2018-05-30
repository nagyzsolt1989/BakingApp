package com.nagy.zsolt.bakingapp.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nagy.zsolt.bakingapp.R;

public class RecepieStepAdapter extends BaseAdapter {

    private final Context mContext;
    private final String[] recepieStepNames;

    public RecepieStepAdapter(Context context, String[] recepieStepNames) {
        this.mContext = context;
        this.recepieStepNames = recepieStepNames;
    }

    @Override
    public int getCount() {
        return recepieStepNames.length;
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
            convertView = layoutInflater.inflate(R.layout.step_list_item, null);
        }

        final TextView textView = (TextView) convertView.findViewById(R.id.tvTitle2);

        if (position == 0) {
            textView.setTextColor(Color.BLUE);
            textView.setAllCaps(true);
        }

        textView.setText(recepieStepNames[position]);
        return convertView;
    }
}
