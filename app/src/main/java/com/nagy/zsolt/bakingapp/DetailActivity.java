package com.nagy.zsolt.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.nagy.zsolt.bakingapp.util.RecepieAdapter;
import com.nagy.zsolt.bakingapp.util.RecepieStepAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements MasterListFragment.OnStepClickListener{

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    public static final String INGREDIENTS_JSONARRAY = "ingredients_jsonarray";
    public static final String STEPS_JSONARRAY = "steps_jsonarray";
    private String[] stepNames, ingredients;
    ListView recepieStepListView;
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (findViewById(R.id.recepie_step_details_linear_layout) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {

                ListView listView = (ListView) findViewById(R.id.recepieList);
                FragmentManager fragmentManager = getSupportFragmentManager();

                RecepieStepFragment recepieStepFragment = new RecepieStepFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, recepieStepFragment)
                        .commit();

//                BodyPartFragment headFragment = new BodyPartFragment();
//                fragmentManager.beginTransaction()
//                        .add(R.id.head_container, headFragment)
//                        .commit();
//
//                // Create and display the body and leg BodyPartFragments
//
//                BodyPartFragment bodyFragment = new BodyPartFragment();
//                bodyFragment.setImageIds(AndroidImageAssets.getBodies());
//                fragmentManager.beginTransaction()
//                        .add(R.id.body_container, bodyFragment)
//                        .commit();
//
//                BodyPartFragment legFragment = new BodyPartFragment();
//                legFragment.setImageIds(AndroidImageAssets.getLegs());
//                fragmentManager.beginTransaction()
//                        .add(R.id.leg_container, legFragment)
//                        .commit();
            }
        } else {
            mTwoPane = false;
        }

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
        final String recepieSteps = intent.getStringExtra(STEPS_JSONARRAY);

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

        stepNames = new String[stepsJSONArray.length() + 1];
        stepNames[0] = "Ingredients";

        for (int i = 0; i < stepsJSONArray.length(); i++) {
            JSONObject obj = stepsJSONArray.optJSONObject(i);
            stepNames[i + 1] = obj.optString(getString(R.string.shortDescription));
//            System.out.println(stepNames[i]);
        }

        RecepieStepAdapter recepieAdapter = new RecepieStepAdapter(getApplicationContext(), stepNames);
        recepieStepListView.setAdapter(recepieAdapter);

//        recepieStepListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                // Trigger the callback method and pass in the position that was clicked
//                mCallback.onStepSelected(position, rece);
//            }
//        });
//        recepieStepListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                if (position == 0) showIngredientsDetails(position, recepieIngredients);
//                else showRecepieStepDetails(position, recepieSteps);
//            }
//        });
    }

    public void onStepSelected(int position, String recepieSteps) {
        // Create a Toast that displays the position that was clicked
        Toast.makeText(this, "Position clicked = " + position, Toast.LENGTH_SHORT).show();

        if (mTwoPane) {
            RecepieStepFragment newFragment = new RecepieStepFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, newFragment)
                    .commit();
        } else {
        }

        // Attach the Bundle to an intent
        final Intent intent = new Intent(this, RecepieStepFragment.class);
        intent.putExtra(RecepieDetailActivity.EXTRA_POSITION, position);
        intent.putExtra(RecepieDetailActivity.STEPS_JSONARRAY, recepieSteps);
        startActivity(intent);

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
