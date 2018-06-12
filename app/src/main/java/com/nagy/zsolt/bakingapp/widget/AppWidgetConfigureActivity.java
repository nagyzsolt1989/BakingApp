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
    public int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    JSONArray recepiesJsonArray;
    public String[] recepieNames, recepieIngredients;
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

        recepieNames = new String[recepiesJsonArray.length()];
        recepieIngredients = new String[recepiesJsonArray.length()];
        for (int i = 0; i < recepiesJsonArray.length(); i++) {
            JSONObject obj = recepiesJsonArray.optJSONObject(i);
            recepieNames[i] = obj.optString(getString(R.string.recepieName));
            recepieIngredients[i] = obj.optString(getString(R.string.ingredients));
        }

        mRecepieListView = (ListView) findViewById(R.id.appwidget_configure_listview);

        RecepieAdapter recepieAdapter = new RecepieAdapter(getApplicationContext(), recepieNames);
        mRecepieListView.setAdapter(recepieAdapter);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mRecepieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // this intent is essential to show the widget
                // if this intent is not included,you can't show
                // widget on homescreen
                Intent intent = new Intent();
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(Activity.RESULT_OK, intent);

                // start your service
                System.out.println("position" + position);
                System.out.println("RecepieName" + recepieNames[position]);
                AppWidget.setWidgetData(getApplicationContext(), position, recepieNames[position]);

                // finish this activity
                finish();

            }
        });

    }
}

