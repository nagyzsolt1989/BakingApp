package com.nagy.zsolt.bakingapp;


import android.content.ClipData;
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
import com.nagy.zsolt.bakingapp.util.RecepieStepAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RecepieStepListFragment extends Fragment {

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnStepClickListener mCallback;
    ListView recepieStepListView;
    String stepNames;
    String[] recepieStepNames;
    int position;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnStepClickListener {
        void onStepSelected(int position);
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
    public RecepieStepListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt("Position");
        }


    }

    // Inflates the Listview
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        stepNames = MainActivity.getRecepieStepNames(position);

        JSONArray stepsJSONArray = null;
        try {
            stepsJSONArray = new JSONArray(stepNames);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recepieStepNames = new String[stepsJSONArray.length()+1];
        recepieStepNames[0] = "Ingredients";
        for (int i = 0; i < stepsJSONArray.length(); i++) {
            JSONObject obj = stepsJSONArray.optJSONObject(i);
            recepieStepNames[i+1] = obj.optString("shortDescription");
            System.out.println("Recepie Steps" + recepieStepNames[i]);
        }

        recepieStepListView = rootView.findViewById(R.id.recepieStepList);
        RecepieStepAdapter recepieAdapter = new RecepieStepAdapter(getContext(), recepieStepNames);
        recepieStepListView.setAdapter(recepieAdapter);

        recepieStepListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Trigger the callback method and pass in the position that was clicked
                System.out.println("Was clicked" + position);
                mCallback.onStepSelected(position);
            }
        });

        return rootView;
    }

}

