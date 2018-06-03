package com.nagy.zsolt.bakingapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nagy.zsolt.bakingapp.api.FetchDataListener;
import com.nagy.zsolt.bakingapp.api.GETAPIRequest;
import com.nagy.zsolt.bakingapp.api.RequestQueueService;
import com.nagy.zsolt.bakingapp.util.RecepieAdapter;

import org.json.JSONArray;
import org.json.JSONObject;


public class MasterListFragment extends Fragment {

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnStepClickListener mCallback;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnStepClickListener {
        void onStepSelected(int position, String recepieSteps);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }


    // Mandatory empty constructor
    public MasterListFragment() {
    }

    // Inflates the Listview
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

//        // Get a reference to the GridView in the fragment_master_list xml layout file
// RecepieStepAdapter recepieAdapter = new RecepieStepAdapter(getApplicationContext(), stepNames);
//        recepieStepListView.setAdapter(recepieAdapter);
//
//        recepieStepListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                // Trigger the callback method and pass in the position that was clicked
//                mCallback.onStepSelected(position, recepieSteps);
//            }
//        });
        // Return the root view
        return rootView;
    }

}

