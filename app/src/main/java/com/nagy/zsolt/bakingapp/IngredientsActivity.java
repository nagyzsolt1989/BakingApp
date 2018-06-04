package com.nagy.zsolt.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.nagy.zsolt.bakingapp.util.IngredientsAdapter;
import com.nagy.zsolt.bakingapp.util.RecepieStepAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

public class IngredientsActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    public static final String INGREDIENTS_JSONARRAY = "ingredients_jsonarray";
    String[] ingredients, quantities, measure;
    ListView ingredientsListView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ButterKnife.bind(this);

        ingredientsListView = findViewById(R.id.ingredientsList);

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

        System.out.println("Vöröshagyma" + recepieIngredients);

        JSONArray ingredientsJSONArray = null;
        try {
            ingredientsJSONArray = new JSONArray(recepieIngredients);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ingredients = new String[ingredientsJSONArray.length()];
        quantities = new String[ingredientsJSONArray.length()];
        measure = new String[ingredientsJSONArray.length()];

        for (int i = 0; i < ingredientsJSONArray.length(); i++) {
            ingredients[i] = ingredientsJSONArray.optJSONObject(i).optString("ingredient");
            quantities[i] = ingredientsJSONArray.optJSONObject(i).optString("quantity");
            measure[i] = ingredientsJSONArray.optJSONObject(i).optString("measure");
            System.out.println(ingredients[i]);
        }

        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getApplicationContext(), ingredients, quantities, measure);
        ingredientsListView.setAdapter(ingredientsAdapter);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
