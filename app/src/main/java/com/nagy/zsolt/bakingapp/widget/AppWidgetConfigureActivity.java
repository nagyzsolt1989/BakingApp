package com.nagy.zsolt.bakingapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.nagy.zsolt.bakingapp.MainActivity;
import com.nagy.zsolt.bakingapp.R;
import com.nagy.zsolt.bakingapp.util.RecepieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.PendingIntent.getActivity;
import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

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

        mContext = getApplicationContext();

        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
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

                SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();

                if (getIntent().hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
                    mAppWidgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);

                    prefsEditor.putString("widget", recepieNames[position]);
                    prefsEditor.commit();

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(AppWidgetConfigureActivity.this);

                    Intent intent = new Intent(AppWidgetConfigureActivity.this, AppWidget.class);
                    intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                    int ids[] = AppWidgetManager.getInstance(
                            getApplication()).getAppWidgetIds(new ComponentName(getApplication(), AppWidget.class));
                    for (int i = 0; i < ids.length; i++) {
                        System.out.println("Ids " + ids[i]);
                    }
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

                    AppWidgetConfigureActivity.this.sendBroadcast(intent);

                    System.out.println("IDE LÉPTÜNK BE" + ids);
                    int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                            new ComponentName(AppWidgetConfigureActivity.this, AppWidget.class));
                    for (int i = 0; i < appWidgetIds.length; i++) {
                        System.out.println("appWidgetIds " + appWidgetIds[i]);
                    }
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    mAppWidgetId = INVALID_APPWIDGET_ID;
                    Intent intent = getIntent();
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                                INVALID_APPWIDGET_ID);

                        AppWidgetProviderInfo providerInfo = AppWidgetManager.getInstance(
                                getBaseContext()).getAppWidgetInfo(mAppWidgetId);
                        String appWidgetLabel = providerInfo.label;

                        prefsEditor.putString("widget", recepieNames[position]);
                        prefsEditor.commit();

                        Intent startWidget = new Intent(AppWidgetConfigureActivity.this,
                                AppWidget.class);
                        startWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                        startWidget.setAction("FROM CONFIGURATION ACTIVITY");
                        setResult(RESULT_OK, startWidget);

                        finish();
                    }
                    if (mAppWidgetId == INVALID_APPWIDGET_ID) {
                        Log.i(AppWidgetConfigureActivity.class.getSimpleName(), "Invalid app widget ID");
                        finish();
                    }
                }

            }
        });

    }
}

