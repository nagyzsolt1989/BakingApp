package com.nagy.zsolt.bakingapp.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nagy.zsolt.bakingapp.R;

public class StepDetailAdapter extends RecyclerView.Adapter<StepDetailAdapter.ViewHolder> {
    private String[] id, shortDescription, description, videoURL, thumbnailURL;
    Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_id;
        public TextView tv_shortDescription;
        public TextView tv_description;
        public TextView tv_videoURL;
        public TextView tv_thumbnailURL;

        public ViewHolder(View view) {
            super(view);
            tv_shortDescription = (TextView) view.findViewById(R.id.stepTitle);
            tv_description = (TextView) view.findViewById(R.id.stepDescription);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public StepDetailAdapter(Context context, String[] id, String[] shortDescription, String[] description, String[] videoURL, String[] thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StepDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recepie_detail_avticity, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tv_shortDescription.setText(shortDescription[position]);
        holder.tv_description.setText(description[position]);

        System.out.println(description[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return shortDescription.length;
    }
}
