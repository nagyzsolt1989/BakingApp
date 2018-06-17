package com.nagy.zsolt.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

            System.out.println("onUpdate-be léptünk");
            Intent intent = new Intent(context, WidgetRemoteViewsService.class);

            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            intent.putExtra("random", randomNumber);
            randomNumber++;
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews widget = new RemoteViews(context.getPackageName(), R.layout.app_widget);

            System.out.println("setRemoteAdapter előtt");
            widget.setRemoteAdapter(R.id.widget_listview, intent);
            System.out.println("setRemoteAdapter után");

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);


            // At the time of widget creation when this method is called, mPrefs does not have the
            // recipe data for the widget so we need to set the recipe data in the WidgetService
            // class to display the recipe correctly for the FIRST time.  When the user launches the
            // config activity to change the recipe, we can use this block of code to UPDATE the
            // recipe information for an EXISTING widget.
            mPrefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            String recipeName = mPrefs.getString("widget", "recepie was not in shared preferences");
            if (recipeName != null){

                widget.setTextViewText(R.id.appwidget_text, recipeName);
            }
            System.out.println("appWidgetManager.updateAppWidget előtt");
            appWidgetManager.updateAppWidget(widgetId, widget);
            System.out.println("appWidgetManager.updateAppWidget után");
        }
        System.out.println("super.onUpdate előtt");
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        System.out.println("super.onUpdate után");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, AppWidget.class);
            int ids[] =manager.getAppWidgetIds(componentName);
            for (int i = 0; i <ids.length; i++) {
                System.out.println("onReceive-be léptünk" + ids[i]);
            }
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(componentName), R.id.widget_listview);
        }
        super.onReceive(context, intent);
    }
}

