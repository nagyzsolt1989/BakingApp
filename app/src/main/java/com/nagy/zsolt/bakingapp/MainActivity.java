package com.nagy.zsolt.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.nagy.zsolt.bakingapp.util.RecepieAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    JSONArray recepiesJsonArray;
    public static String[] recepieNames, recepieSteps, recepieIngredients;
    ListView mRecepieListView;

    public static final String SHARED_PREF_NAME = "RECEPIE";
    public static final String RECEPIE_JSON_ARRAY = "RECEPIE_JSON_ARRAY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecepieListView = (ListView) findViewById(R.id.recepieList);

        getRecepies();
    }

    public void getRecepies() {

        try {
            //Create Instance of GETAPIRequest and call it's
            //request() method
            String URL = getString(R.string.RECEPIE_URL);
            GETAPIRequest getapiRequest = new GETAPIRequest();
            getapiRequest.request(this, fetchGetResultListener, URL);
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
                    recepieSteps = new String[recepiesJsonArray.length()];
                    recepieIngredients = new String[recepiesJsonArray.length()];
                    for (int i = 0; i < recepiesJsonArray.length(); i++) {
                        JSONObject obj = recepiesJsonArray.getJSONObject(i);
                        recepieNames[i] = obj.optString(getString(R.string.recepieName));
                        recepieSteps[i] = obj.optString(getString(R.string.steps));
                        recepieIngredients[i] = obj.optString(getString(R.string.ingredients));
                    }

                    RecepieAdapter recepieAdapter = new RecepieAdapter(getApplicationContext(), recepieNames);
                    mRecepieListView.setAdapter(recepieAdapter);
                    mRecepieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            showRecepieDetails(position);
                        }
                    });

                    saveToSharedPref(getApplicationContext(), data);

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
            RequestQueueService.showProgressDialog(MainActivity.this);
        }
    };

    private void showRecepieDetails(int position) {
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        intent.putExtra(DetailActivity.INGREDIENTS_JSONARRAY, recepieIngredients[position].toString());
        intent.putExtra(DetailActivity.STEPS_JSONARRAY, recepieSteps[position].toString());
        startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.fade_out);
    }

    public static void saveToSharedPref(Context mContext, JSONArray data){
        SharedPreferences mPrefs = mContext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(RECEPIE_JSON_ARRAY, data.toString());
        prefsEditor.commit();
    }

    public static String getRecepieStepNames(int position) {
        return recepieSteps[position];
    }

    public static String[] getRecepieNames() {
        return recepieNames;
    }
}