package com.nagy.zsolt.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.nagy.zsolt.bakingapp.R;
import com.nagy.zsolt.bakingapp.data.RecepieContract;
import com.nagy.zsolt.bakingapp.data.RecepieProvider;
import com.nagy.zsolt.bakingapp.util.RecepieAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;
import static com.nagy.zsolt.bakingapp.DetailActivity.position;
import static com.nagy.zsolt.bakingapp.data.Constants.Keys.SHARED_PREF_NAME;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;
    private int mAppWidgetId;
    SharedPreferences mPrefs;
    String recipeName;
    ArrayList<String> ingredients, quantities, measures;
    int recipeId;

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        System.out.println("WidgetRemoteViewsFactory-ba léptünk: ");
        this.mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        System.out.println("Viewsfactory recipeId: " + recipeId);
    }

    @Override
    public void onCreate() {

        System.out.println("WidgetRemoteViewsFactory onCreate-ba léptünk: ");
        // Get the saved Recipe from the ConfigurationActivity
        mPrefs = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        ArrayList<String> ingredients = new ArrayList<String>();
        ArrayList<String> quantities = new ArrayList<String>();
        ArrayList<String> measures = new ArrayList<String>();

    }

    @Override
    public void onDataSetChanged() {
        System.out.println("onDataSetChanged-be léptünk ");
        recipeName = mPrefs.getString("widget", "recepie was not in shared preferences");


        String[] projection = {
                RecepieContract.IngredientEntry._ID,
                RecepieContract.IngredientEntry.COLUMN_RECEPIE_NAME,
                RecepieContract.IngredientEntry.COLUMN_RECEPIE_QUANTITY,
                RecepieContract.IngredientEntry.COLUMN_MEASURE,
                RecepieContract.IngredientEntry.COLUMN_INGREDIENT,
        };

        String selection = RecepieContract.IngredientEntry.COLUMN_RECEPIE_NAME + "=?";

        String[] selectionArgs = {recipeName};

        // Check if the recipe exists in the database
        mCursor = mContext.getContentResolver().query(
                RecepieContract.IngredientEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null,
                null
        );

        if (mCursor != null && mCursor.moveToFirst()) {

            String quantity, measure, ingredient;

            quantity = mCursor.getString(mCursor.getColumnIndex(RecepieContract.IngredientEntry.COLUMN_RECEPIE_QUANTITY));
            measure = mCursor.getString(mCursor.getColumnIndex(RecepieContract.IngredientEntry.COLUMN_MEASURE));
            ingredient = mCursor.getString(mCursor.getColumnIndex( RecepieContract.IngredientEntry.COLUMN_INGREDIENT));

            ingredients.add(ingredient);
            measures.add(measure);
            quantities.add(quantity);

            mCursor.close();
        }
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        System.out.println("mcoursor getcount" + mCursor.getCount());
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_item_ingredient);

        rv.setTextViewText(R.id.tvTitle3, ingredients.get(position));
        rv.setTextViewText(R.id.quantityValue, quantities.get(position));
        rv.setTextViewText(R.id.measureValue, measures.get(position));

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}