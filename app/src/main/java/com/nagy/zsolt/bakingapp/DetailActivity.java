package com.nagy.zsolt.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.nagy.zsolt.bakingapp.util.RecepieAdapter;
import com.nagy.zsolt.bakingapp.util.RecepieStepAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    public static final String INGREDIENTS_JSONARRAY = "ingredients_jsonarray";
    public static final String STEPS_JSONARRAY = "steps_jsonarray";
    String[] stepNames;
    ListView recepieStepListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        recepieStepListView = findViewById(R.id.recepieStepList);

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

        String recepieIngredients = intent.getStringExtra(INGREDIENTS_JSONARRAY);
        String recepieSteps = intent.getStringExtra(STEPS_JSONARRAY);

        System.out.println("Batman" + recepieSteps);
        JSONArray stepsJSONArray = null;
        try {
            stepsJSONArray = new JSONArray(recepieSteps);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Robin" + stepsJSONArray);

        stepNames = new String[stepsJSONArray.length()+1];
        stepNames[0] = "Ingredients";

        for (int i = 0; i < stepsJSONArray.length(); i++) {
            JSONObject obj = stepsJSONArray.optJSONObject(i);
            stepNames[i+1] = obj.optString(getString(R.string.shortDescription));
            System.out.println(stepNames[i]);
        }

        RecepieStepAdapter recepieAdapter = new RecepieStepAdapter(getApplicationContext(), stepNames);
        recepieStepListView.setAdapter(recepieAdapter);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
