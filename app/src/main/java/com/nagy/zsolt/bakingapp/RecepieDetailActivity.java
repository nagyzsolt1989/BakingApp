package com.nagy.zsolt.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.nagy.zsolt.bakingapp.util.StepDetailAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecepieDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    public static final String STEPS_JSONARRAY = "steps_jsonarray";

    String[] id, stepTitle, stepDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recepie_detail_avticity);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        final String recepieSteps = intent.getStringExtra(STEPS_JSONARRAY);

//        System.out.println("recepieSteps:" + recepieSteps);

        JSONArray stepsJSONArray = null;
        try {
            stepsJSONArray = new JSONArray(recepieSteps);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(recepieSteps);

        stepTitle = new String[stepsJSONArray.length()];
        stepDescription = new String[stepsJSONArray.length()];
        id = new String[stepsJSONArray.length()];

        for (int i = 0; i < stepsJSONArray.length(); i++) {
            JSONObject obj = stepsJSONArray.optJSONObject(i);
            id[i] = obj.optString("id");
            stepTitle[i] = obj.optString(getString(R.string.shortDescription));
            stepDescription[i] = obj.optString("description");
            System.out.println(id[i]);
            System.out.println(stepTitle[i]);
            System.out.println(stepDescription[i]);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new StepDetailAdapter(getApplicationContext(), id, stepTitle, stepDescription, null, null);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

}
