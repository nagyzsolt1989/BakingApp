package com.nagy.zsolt.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.nagy.zsolt.bakingapp.MainActivity;
import com.nagy.zsolt.bakingapp.R;
import com.nagy.zsolt.bakingapp.util.IngredientsAdapter;

import static com.android.volley.VolleyLog.TAG;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link AppWidgetConfigureActivity AppWidgetConfigureActivity}
 */
public class AppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int[] appWidgetIds, int recipeId, String recipeName) {
        Intent intent;
        PendingIntent pendingIntent;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        for (int appWidgetId : appWidgetIds) {
            if (recipeId >= 0 && recipeName != null) {
                Bundle args = new Bundle();

                Intent service = new Intent(context, WidgetRemoteViewsService.class);
                service.setData(Uri.fromParts("content", String.valueOf(recipeId), null));
                views.setRemoteAdapter(R.id.widget_listview, service);
            }

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    public static void setWidgetData(Context context, int recipeId, String recipeName) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,
                AppWidget.class));

        updateAppWidget(context, appWidgetManager, appWidgetIds, recipeId, recipeName);
        System.out.println("setWidgetData: " + recipeId +" " + recipeName);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateAppWidget(context, appWidgetManager, appWidgetIds, -1, null);
//        for (int appWidgetId : appWidgetIds) {
//            RemoteViews views = new RemoteViews(
//                    context.getPackageName(),
//                    R.layout.app_widget
//            );
//            Intent intent = new Intent(context, WidgetRemoteViewsService.class);
//            views.setRemoteAdapter(R.id.widget_listview, intent);
//            appWidgetManager.updateAppWidget(appWidgetId, views);
//        }
    }
}

