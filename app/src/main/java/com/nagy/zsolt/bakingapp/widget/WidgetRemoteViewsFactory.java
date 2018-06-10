package com.nagy.zsolt.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import com.nagy.zsolt.bakingapp.util.RecepieAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import static com.android.volley.VolleyLog.TAG;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    public Context mContext;
    private Cursor mCursor;
    public static final String SHARED_PREF_NAME = "RECEPIE";
    public static final String RECEPIE_JSON_ARRAY = "RECEPIE_JSON_ARRAY";
    JSONArray recepiesJsonArray;
    public String[] ingredients, quantities, measure;

    public WidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
        SharedPreferences sharedPref = mContext.getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        try {
            recepiesJsonArray = new JSONArray(sharedPref.getString(RECEPIE_JSON_ARRAY, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("HAT!!!");
        System.out.println("Sólyom" + recepiesJsonArray);

        ingredients = new String[recepiesJsonArray.length()];
        quantities = new String[recepiesJsonArray.length()];
        measure = new String[recepiesJsonArray.length()];

        for (int i = 0; i < recepiesJsonArray.length(); i++) {
            ingredients[i] = recepiesJsonArray.optJSONObject(i).optString("ingredient");
            quantities[i] = recepiesJsonArray.optJSONObject(i).optString("quantity");
            measure[i] = recepiesJsonArray.optJSONObject(i).optString("measure");
            System.out.println(ingredients[i]);
        }

    }

    //called whenever the appwidget is updated
    @Override
    public void onDataSetChanged() {

        Log.i(TAG, "onDataSetChanged: ");

//        final long identityToken = Binder.clearCallingIdentity();
//        Uri uri = Contract.PATH_TODOS_URI;
//        mCursor = mContext.getContentResolver().query(uri,
//                null,
//                null,
//                null,
//                Contract._ID + " DESC");
//
//        Binder.restoreCallingIdentity(identityToken);


    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_item_ingredient);

        rv.setTextViewText(R.id.tvTitle3, ingredients[position]);
        rv.setTextViewText(R.id.quantityValue, quantities[position]);
        rv.setTextViewText(R.id.measureValue, measure[position]);

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