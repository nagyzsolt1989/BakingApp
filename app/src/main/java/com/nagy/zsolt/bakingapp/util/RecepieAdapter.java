package com.nagy.zsolt.bakingapp.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nagy.zsolt.bakingapp.R;

/**
 * Created by Zsolti on 2018.03.07..
 */

public class RecepieAdapter extends BaseAdapter {

    private final Context mContext;
    private final String[] recepieNames;

    public RecepieAdapter(Context context, String[] recepieNames) {
        this.mContext = context;
        this.recepieNames = recepieNames;
    }

    @Override
    public int getCount() {
        return recepieNames.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {


        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.img_row);
        final TextView textView = (TextView) convertView.findViewById(R.id.tvTitle);

        switch (position) {
            case 0:
                imageView.setImageResource(R.drawable.sweet1);
                break;
            case 1:
                imageView.setImageResource(R.drawable.sweet2);
                break;
            case 2:
                imageView.setImageResource(R.drawable.sweet3);
                break;
            case 3:
                imageView.setImageResource(R.drawable.sweet4);
                break;
            default:
                imageView.setImageResource(R.drawable.sweet4);
        }

        textView.setText(recepieNames[position]);
        return convertView;
    }
}

