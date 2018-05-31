package com.nagy.zsolt.bakingapp;


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

    JSONArray recepiesJsonArray;
    JSONArray recepieIngredients;
    JSONArray recepieSteps;
    String[] recepieNames;
    ListView mRecepieListView;

    // Mandatory empty constructor
    public MasterListFragment() {
    }

    // Inflates the Listview
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        // Get a reference to the GridView in the fragment_master_list xml layout file
        mRecepieListView = (ListView) rootView.findViewById(R.id.recepieList);

        getRecepies();

        // Return the root view
        return rootView;
    }

    public void getRecepies() {

        try {
            //Create Instance of GETAPIRequest and call it's
            //request() method
            String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
            GETAPIRequest getapiRequest = new GETAPIRequest();
            getapiRequest.request(getContext(), fetchGetResultListener, URL);
//            Toast.makeText(getContext(), "GET API called", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Implementing interfaces of FetchDataListener for GET api request
    FetchDataListener fetchGetResultListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONArray data) {
            //Fetch Complete. Now stop progress bar  or loader
            //you started in onFetchStart
            RequestQueueService.cancelProgressDialog();
            try {
                //Check result sent by our GETAPIRequest class
                if (data != null) {
                    recepiesJsonArray = data;
                    recepieNames = new String[recepiesJsonArray.length()];
                    for (int i = 0; i < recepiesJsonArray.length(); i++) {
                        JSONObject obj = recepiesJsonArray.getJSONObject(i);
                        recepieNames[i] = obj.optString(getString(R.string.recepieName));
                        recepieIngredients = obj.getJSONArray(getString(R.string.ingredients));
                        recepieSteps = obj.getJSONArray(getString(R.string.steps));
                        System.out.println(recepieNames[i]);

                    }

                    RecepieAdapter recepieAdapter = new RecepieAdapter(getContext(), recepieNames);
                    mRecepieListView.setAdapter(recepieAdapter);
                    mRecepieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            showRecepieDetails(position);
                        }
                    });

                } else {
                    RequestQueueService.showAlert(getString(R.string.noDataAlert), (FragmentActivity) getContext());
                }
            } catch (
                    Exception e) {
                RequestQueueService.showAlert(getString(R.string.exceptionAlert), (FragmentActivity) getContext());
                e.printStackTrace();
            }

        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            //Show if any error message is there called from GETAPIRequest class
            RequestQueueService.showAlert(msg, (FragmentActivity) getContext());
        }

        @Override
        public void onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(getActivity());
        }
    };

    private void showRecepieDetails(int position) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        intent.putExtra(DetailActivity.INGREDIENTS_JSONARRAY, recepieIngredients.toString());
        intent.putExtra(DetailActivity.STEPS_JSONARRAY, recepieSteps.toString());
        startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.fade_out);
    }

}

