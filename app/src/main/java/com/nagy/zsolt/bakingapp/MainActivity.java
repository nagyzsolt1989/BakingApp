package com.nagy.zsolt.bakingapp;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nagy.zsolt.bakingapp.api.FetchDataListener;
import com.nagy.zsolt.bakingapp.api.GETAPIRequest;
import com.nagy.zsolt.bakingapp.api.RequestQueueService;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    JSONArray recepiesJsonArray;
    String[] recepieNames;
    ListView mRecepieListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getRecepies();
        mRecepieListView = findViewById(R.id.recepieList);
    }

    public void getRecepies() {

        try {
            //Create Instance of GETAPIRequest and call it's
            //request() method
            String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
            GETAPIRequest getapiRequest = new GETAPIRequest();
            getapiRequest.request(getApplicationContext(), fetchGetResultListener, URL);
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
                        System.out.println(recepieNames[i]);
                    }

                    ArrayAdapter<String> recepieAdapter =
                            new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, recepieNames);

                    mRecepieListView.setAdapter(recepieAdapter);


                } else {
                    RequestQueueService.showAlert(getString(R.string.noDataAlert), (FragmentActivity) getApplicationContext());
                }
            } catch (
                    Exception e) {
                RequestQueueService.showAlert(getString(R.string.exceptionAlert), (FragmentActivity) getApplicationContext());
                e.printStackTrace();
            }

        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            //Show if any error message is there called from GETAPIRequest class
            RequestQueueService.showAlert(msg, (FragmentActivity) getApplicationContext());
        }

        @Override
        public void onFetchStart() {
            //Start showing progressbar or any loader you have
//            RequestQueueService.showProgressDialog(this);
        }
    };

}