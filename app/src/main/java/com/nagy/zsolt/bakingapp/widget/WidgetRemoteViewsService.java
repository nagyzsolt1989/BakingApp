package com.nagy.zsolt.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.nagy.zsolt.bakingapp.R;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static com.nagy.zsolt.bakingapp.data.Constants.Keys.SHARED_PREF_NAME;

public class WidgetRemoteViewsService extends RemoteViewsService{

    // The shared preferences that has our Recipe from the ConfigActivity
    SharedPreferences mPrefs;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        System.out.println("onGetViewFactory-be léptünk");
        int mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        mPrefs = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String recipeName = mPrefs.getString("widget" + mAppWidgetId, "recepie was not in shared preferences");

        RemoteViews widget = new RemoteViews(getApplicationContext().getPackageName(), R.layout.app_widget);

        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
        manager.updateAppWidget(mAppWidgetId, widget);

        System.out.println("onGetViewFactory: " + intent);
        return (new WidgetRemoteViewsFactory(this.getApplicationContext(), intent));
    }
}
