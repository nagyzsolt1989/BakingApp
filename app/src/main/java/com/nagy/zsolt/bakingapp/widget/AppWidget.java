package com.nagy.zsolt.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.nagy.zsolt.bakingapp.R;

import static com.nagy.zsolt.bakingapp.data.Constants.Keys.SHARED_PREF_NAME;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link AppWidgetConfigureActivity AppWidgetConfigureActivity}
 */
public class AppWidget extends AppWidgetProvider {

    SharedPreferences mPrefs;
    int randomNumber;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {

            Intent intent = new Intent(context, WidgetRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews widget = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            widget.setRemoteAdapter(R.id.widget_listview, intent);

            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            String recipeName = mPrefs.getString("widget", "recepie was not in shared preferences");
            String recipeDetails = mPrefs.getString(recipeName, "recepie was not in shared preferences");
            if (recipeName != null) {

                widget.setTextViewText(R.id.appwidget_text, recipeName);
                widget.setTextViewText(R.id.appwidget_details, recipeDetails);

            }

            appWidgetManager.updateAppWidget(widgetId, widget);

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, AppWidget.class);
            int ids[] = manager.getAppWidgetIds(componentName);
            for (int i = 0; i < ids.length; i++) {
                System.out.println("onReceive-be léptünk" + ids[i]);
            }
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(componentName), R.id.widget_listview);
        }
        super.onReceive(context, intent);
    }
}

