package com.nagy.zsolt.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
import com.nagy.zsolt.bakingapp.data.RecepieContract;
import com.nagy.zsolt.bakingapp.data.RecepieDBHelper;
import com.nagy.zsolt.bakingapp.util.RecepieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

import static com.nagy.zsolt.bakingapp.data.Constants.Keys.RECEPIE_JSON_ARRAY;
import static com.nagy.zsolt.bakingapp.data.Constants.Keys.SHARED_PREF_NAME;

public class MainActivity extends AppCompatActivity {

    JSONArray recepiesJsonArray;
    public static String[] recepieNames, recepieSteps, recepieIngredients, ingredients, quantities, measure;
    ListView mRecepieListView;
    Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        mRecepieListView = (ListView) findViewById(R.id.recepieList);

        RecepieDBHelper dbHelper = new RecepieDBHelper(mContext);

        dbHelper.getWritableDatabase();

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
                        System.out.println("Vakanu" + recepieIngredients[i]);
                    }

                    RecepieAdapter recepieAdapter = new RecepieAdapter(mContext, recepieNames);
                    mRecepieListView.setAdapter(recepieAdapter);
                    mRecepieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            showRecepieDetails(position);
                        }
                    });

                    for (int m = 0; m < recepieNames.length; m++) {
                        JSONArray ingredientsJSONArray = null;
                        try {
                            ingredientsJSONArray = new JSONArray(recepieIngredients[m]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ingredients = new String[ingredientsJSONArray.length()];
                        quantities = new String[ingredientsJSONArray.length()];
                        measure = new String[ingredientsJSONArray.length()];

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        if (!prefs.getBoolean("firstTime", false)) {
                            for (int i = 0; i < ingredientsJSONArray.length(); i++) {
                                ingredients[i] = ingredientsJSONArray.optJSONObject(i).optString("ingredient");
                                quantities[i] = ingredientsJSONArray.optJSONObject(i).optString("quantity");
                                measure[i] = ingredientsJSONArray.optJSONObject(i).optString("measure");
                                System.out.println("Recepie Name: " + recepieNames[m] + "\nIngredient" + ingredients[i] + "\nQuantity" + quantities[i] + "\nMeasure" + measure[i]);


                                saveToIngredientsToDB(recepieNames[m], ingredients[i], quantities[i], measure[i]);
                            }
                        }

                        // mark first time has runned.
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("firstTime", true);
                        editor.commit();


                    }
                    saveToSharedPref(mContext, data);

                } else {
                    RequestQueueService.showAlert(getString(R.string.noDataAlert), (FragmentActivity) mContext);
                }
            } catch (
                    Exception e) {
                RequestQueueService.showAlert(getString(R.string.exceptionAlert), (FragmentActivity) mContext);
                e.printStackTrace();
            }

        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            //Show if any error message is there called from GETAPIRequest class
            RequestQueueService.showAlert(msg, (FragmentActivity) mContext);
        }

        @Override
        public void onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(MainActivity.this);
        }
    };

    private void showRecepieDetails(int position) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        intent.putExtra(DetailActivity.INGREDIENTS_JSONARRAY, recepieIngredients[position].toString());
        intent.putExtra(DetailActivity.STEPS_JSONARRAY, recepieSteps[position].toString());
        startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.fade_out);
    }

    public static void saveToSharedPref(Context mContext, JSONArray data) {
        SharedPreferences mPrefs = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(RECEPIE_JSON_ARRAY, data.toString());
        prefsEditor.commit();
    }

    public static String getRecepieStepNames(int position) {
        return recepieSteps[position];
    }

    public void saveToIngredientsToDB(final String recepieName, final String recepieIngredient, final String recepieQuantity, final String recepieMeasure) {


        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                //ContentValues instance to pass the values onto the insert query
                ContentValues cv = new ContentValues();
                cv.put(RecepieContract.IngredientEntry.COLUMN_RECEPIE_NAME, recepieName);
                cv.put(RecepieContract.IngredientEntry.COLUMN_INGREDIENT, recepieIngredient);
                cv.put(RecepieContract.IngredientEntry.COLUMN_RECEPIE_QUANTITY, recepieQuantity);
                cv.put(RecepieContract.IngredientEntry.COLUMN_MEASURE, recepieMeasure);

                mContext.getContentResolver().insert(
                        RecepieContract.IngredientEntry.CONTENT_URI,
                        cv);
                return null;
            }
        }.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR);
    }
}