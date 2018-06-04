package com.nagy.zsolt.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecepieStepFragment extends Fragment {

    String recepieStepTitle, recepieStepDescription;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public RecepieStepFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recepieStepTitle = getArguments().getString("StepTitle");
            recepieStepDescription = getArguments().getString("StepDescription");
        }
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.content_recepie_detail, container, false);

        final TextView recepieStepTitleTV = (TextView) rootView.findViewById(R.id.recepieStepTitle);
        final TextView recepieStepDescriptionTV = (TextView) rootView.findViewById(R.id.recepieStepDescription);

        recepieStepTitleTV.setText(recepieStepTitle);
        recepieStepDescriptionTV.setText(recepieStepDescription);
//            final ImageView imageView = (ImageView) rootView.findViewById(R.id.body_part_image_view);
//
//            // If a list of image ids exists, set the image resource to the correct item in that list
//            // Otherwise, create a Log statement that indicates that the list was not found
//            if(mImageIds != null){
//                // Set the image resource to the list item at the stored index
//                imageView.setImageResource(mImageIds.get(mListIndex));
//
//                // Set a click listener on the image view
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        // Increment position as long as the index remains <= the size of the image ids list
//                        if(mListIndex < mImageIds.size()-1) {
//                            mListIndex++;
//                        } else {
//                            // The end of list has been reached, so return to beginning index
//                            mListIndex = 0;
//                        }
//                        // Set the image resource to the new list item
//                        imageView.setImageResource(mImageIds.get(mListIndex));
//                    }
//                });

//            } else {
//                Log.v(TAG, "This fragment has a null list of image id's");
//            }

        // Return the rootView
        return rootView;
    }

    // Setter methods for keeping track of the list images this fragment can display and which image
    // in the list is currently being displayed

//    public void setImageIds(List<Integer> imageIds) {
//        mImageIds = imageIds;
//    }
//
//    public void setListIndex(int index) {
//        mListIndex = index;
//    }
//
//    /**
//     * Save the current state of this fragment
//     */
//    @Override
//    public void onSaveInstanceState(Bundle currentState) {
//        currentState.putIntegerArrayList(IMAGE_ID_LIST, (ArrayList<Integer>) mImageIds);
//        currentState.putInt(LIST_INDEX, mListIndex);
//    }


}
