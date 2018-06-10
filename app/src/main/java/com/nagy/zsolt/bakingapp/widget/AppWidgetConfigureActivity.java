package com.nagy.zsolt.bakingapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.nagy.zsolt.bakingapp.MainActivity;
import com.nagy.zsolt.bakingapp.R;
import com.nagy.zsolt.bakingapp.util.RecepieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.PendingIntent.getActivity;

/**
 * The configuration screen for the {@link AppWidget AppWidget} AppWidget.
 */
public class AppWidgetConfigureActivity extends AppCompatActivity {

    public static final String SHARED_PREF_NAME = "RECEPIE";
    public static final String RECEPIE_JSON_ARRAY = "RECEPIE_JSON_ARRAY";
    public static final String EXTRA_APPWIDGET_INGREDIENTS = "EXTRA_APPWIDGET_INGREDIENTS";


    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    JSONArray recepiesJsonArray;
    String[] recepieNames, recepieIngredients;
    ListView mRecepieListView;
    Context mContext;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.app_widget_configure);


        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        try {
            recepiesJsonArray = new JSONArray(sharedPref.getString(RECEPIE_JSON_ARRAY, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("fdsd", "onCreate: HÁROM!!!");
        System.out.println("egy" + recepiesJsonArray);

        recepieNames = new String[recepiesJsonArray.length()];
        recepieIngredients = new String[recepiesJsonArray.length()];
        for (int i = 0; i < recepiesJsonArray.length(); i++) {
            JSONObject obj = recepiesJsonArray.optJSONObject(i);
            recepieNames[i] = obj.optString(getString(R.string.recepieName));
            recepieIngredients[i] = obj.optString(getString(R.string.ingredients));
        }

        Log.i("fdsd", "onCreate: NÉGY!!!");

        mRecepieListView = (ListView) findViewById(R.id.appwidget_configure_listview);

        RecepieAdapter recepieAdapter = new RecepieAdapter(getApplicationContext(), recepieNames);
        mRecepieListView.setAdapter(recepieAdapter);


        Log.i("fdsd", "onCreate: ÖT!!!");
        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Log.i("fdsd", "onCreate: HAT!!!");

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

//        mRecepieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
////                AppWidget.sendRefreshBroadcast(mContext);
//                for (int i = 0; i < recepieIngredients[position].length(); i++) {
//                    System.out.println("Bubó" + recepieIngredients[position]);
//                }
//                Intent resultValue = new Intent();
//                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
//                resultValue.putExtra(EXTRA_APPWIDGET_INGREDIENTS, recepieIngredients[position].toString());
//                setResult(RESULT_OK, resultValue);
//                finish();
//            }
//        });

    }
}

