package com.nagy.zsolt.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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
    private String[] stepNames, ingredients;
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

        final String recepieIngredients = intent.getStringExtra(INGREDIENTS_JSONARRAY);
        String recepieSteps = intent.getStringExtra(STEPS_JSONARRAY);

        JSONArray ingredientsJSONArray = null;
        try {
            ingredientsJSONArray = new JSONArray(recepieIngredients);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray stepsJSONArray = null;
        try {
            stepsJSONArray = new JSONArray(recepieSteps);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ingredients = new String[ingredientsJSONArray.length()];

        System.out.println(recepieIngredients);

        stepNames = new String[stepsJSONArray.length()+1];
        stepNames[0] = "Ingredients";

        for (int i = 0; i < stepsJSONArray.length(); i++) {
            JSONObject obj = stepsJSONArray.optJSONObject(i);
            stepNames[i+1] = obj.optString(getString(R.string.shortDescription));
//            System.out.println(stepNames[i]);
        }

        RecepieStepAdapter recepieAdapter = new RecepieStepAdapter(getApplicationContext(), stepNames);
        recepieStepListView.setAdapter(recepieAdapter);

        recepieStepListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) showIngredientsDetails(position, recepieIngredients);
            }
        });
    }

    private void showIngredientsDetails(int position, String recepieIngredients) {
        Intent intent = new Intent(getApplicationContext(), IngredientsActivity.class);
        intent.putExtra(IngredientsActivity.EXTRA_POSITION, position);
        intent.putExtra(IngredientsActivity.INGREDIENTS_JSONARRAY, recepieIngredients);
        startActivity(intent);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
