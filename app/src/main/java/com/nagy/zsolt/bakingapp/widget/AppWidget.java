package com.nagy.zsolt.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.nagy.zsolt.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link AppWidgetConfigureActivity AppWidgetConfigureActivity}
 */
public class AppWidget extends AppWidgetProvider {

    public static final String EXTRA_APPWIDGET_INGREDIENTS = "EXTRA_APPWIDGET_INGREDIENTS";
    String recepIngredients;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//        CharSequence widgetText = AppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setTextViewText(R.id.appwidget_text, "SON GOKU");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
//            AppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        // Display which View is clicked
        Toast.makeText(context,"onReceive: "+intent.getAction(), Toast.LENGTH_LONG).show();

//        // Generate a random number
//        Random rand = new Random();
//        Integer randomNumber = rand.nextInt(25);
//
//            /*
//                String getAction()
//                Retrieve the general action to be performed, such as ACTION_VIEW.
//            */
//
//        if (RED_CLICKED.equals(intent.getAction())) {
//            // If the Red TextView clicked, then do that
//            remoteViews.setTextViewText(R.id.tv_red, "" + randomNumber);
//        }
//
//        if (GREEN_CLICKED.equals(intent.getAction())) {
//            // If the Green TextView clicked, then do that
//            remoteViews.setTextViewText(R.id.tv_green, "" + randomNumber);
//        }
//        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        recepIngredients = intent.getStringExtra(EXTRA_APPWIDGET_INGREDIENTS);

        System.out.println("Végre" + recepIngredients);
    }
}

