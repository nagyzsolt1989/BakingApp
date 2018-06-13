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

import static com.android.volley.VolleyLog.TAG;
import static com.nagy.zsolt.bakingapp.DetailActivity.position;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private Context mContext;
    private Cursor mCursor;
    int recipeId;

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        if (intent.getData() != null) {
            recipeId = Integer.valueOf(intent.getData().getSchemeSpecificPart());
        }
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();
        Uri ingredientQueryUri = RecepieContract.IngredientEntry.CONTENT_URI;
        mCursor = mContext.getContentResolver().query(
                ingredientQueryUri,
                null,
                null,
                null,
                null
        );

        System.out.println("Viewsfactory: " + DatabaseUtils.dumpCursorToString(mCursor));


        Binder.restoreCallingIdentity(identityToken);
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
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_item_ingredient);
        String quantity = mCursor.getString(mCursor.getColumnIndex(RecepieContract.IngredientEntry.COLUMN_RECEPIE_QUANTITY));
        String measure = mCursor.getString(mCursor.getColumnIndex(RecepieContract.IngredientEntry.COLUMN_MEASURE));
        String ingredient = mCursor.getString(mCursor.getColumnIndex(RecepieContract.IngredientEntry.COLUMN_INGREDIENT));

        rv.setTextViewText(R.id.tvTitle3, ingredient);
        rv.setTextViewText(R.id.quantityValue, quantity);
        rv.setTextViewText(R.id.measureValue, measure);

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