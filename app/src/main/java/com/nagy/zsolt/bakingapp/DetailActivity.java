package com.nagy.zsolt.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements RecepieStepListFragment.OnStepClickListener, AdapterView.OnItemClickListener{

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    public static final String INGREDIENTS_JSONARRAY = "ingredients_jsonarray";
    public static final String STEPS_JSONARRAY = "steps_jsonarray";
    public static String recepieIngredients, recepieSteps;
    JSONArray stepJSONArray;
    String[] recepieStepTitle, recepieStepDescription, recepieStepVideoURI, recepieStepThumbNail;
    public static int position;
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        recepieIngredients  = intent.getStringExtra(INGREDIENTS_JSONARRAY);
        recepieSteps  = intent.getStringExtra(STEPS_JSONARRAY);

        System.out.println("recepieSteps" + recepieSteps);

        JSONArray stepJSONArray = null;
        try {
            stepJSONArray = new JSONArray(recepieSteps);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recepieStepTitle = new String[stepJSONArray.length()];
        recepieStepDescription = new String[stepJSONArray.length()];
        recepieStepVideoURI = new String[stepJSONArray.length()];
        recepieStepThumbNail = new String[stepJSONArray.length()];

        System.out.println("stespJSONarray" + stepJSONArray);


        for (int i = 0; i < stepJSONArray.length(); i++) {
            recepieStepTitle[i] = stepJSONArray.optJSONObject(i).optString("shortDescription");
            recepieStepDescription[i] = stepJSONArray.optJSONObject(i).optString("description");
            recepieStepVideoURI[i] = stepJSONArray.optJSONObject(i).optString("videoURL");
            recepieStepThumbNail[i] = stepJSONArray.optJSONObject(i).optString("thumbnailURL");
        }

        if (findViewById(R.id.recepie_step_details_linear_layout) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {

//                ListView listView = (ListView) findViewById(R.id.recepieList);
                FragmentManager fragmentManager = getSupportFragmentManager();

                Bundle bundle = new Bundle();
                bundle.putInt("Position", position);

                RecepieStepListFragment recepieStepListFragment = new RecepieStepListFragment();
                recepieStepListFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .add(R.id.step_list_container, recepieStepListFragment)
                        .commit();

                RecepieStepFragment recepieStepFragment = new RecepieStepFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, recepieStepFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;

            FragmentManager fragmentManager = getSupportFragmentManager();

            Bundle bundle = new Bundle();
            bundle.putInt("Position", position);

            RecepieStepListFragment recepieStepListFragment = new RecepieStepListFragment();
            recepieStepListFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.step_list_container, recepieStepListFragment)
                    .commit();

        }
    }

    public void onStepSelected(int stepPosition) {
        // Create a Toast that displays the position that was clicked
        Toast.makeText(this, "Position clicked = " + stepPosition, Toast.LENGTH_SHORT).show();


        if (mTwoPane) { // single activity with list and detail
            // Replace frame layout with correct detail fragment
            if(stepPosition == 0){
                showIngredientsDetails(DetailActivity.position, DetailActivity.recepieIngredients);
            } else{
                Bundle bundle = new Bundle();
                bundle.putStringArray("StepTitle", recepieStepTitle);
                bundle.putStringArray("StepDescription", recepieStepDescription);
                bundle.putStringArray("StepVideoURI", recepieStepVideoURI);
                bundle.putStringArray("StepThumbNail", recepieStepThumbNail);
                bundle.putInt("StepPosition", stepPosition-1);
                RecepieStepFragment recepieStepFragment = new RecepieStepFragment();
                recepieStepFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.step_detail_container, recepieStepFragment);
                fragmentTransaction.commit();
            }
        } else {
            if(stepPosition == 0){
                showIngredientsDetails(DetailActivity.position, DetailActivity.recepieIngredients);
            } else {
                // separate activities
                // launch detail activity using intent
                Bundle bundle = new Bundle();
                bundle.putStringArray("StepTitle", recepieStepTitle);
                bundle.putStringArray("StepDescription", recepieStepDescription);
                bundle.putStringArray("StepVideoURI", recepieStepVideoURI);
                bundle.putStringArray("StepThumbNail", recepieStepThumbNail);
                bundle.putInt("StepPosition", stepPosition-1);
                Intent i = new Intent(getApplicationContext(), RecepieStepActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        }

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
