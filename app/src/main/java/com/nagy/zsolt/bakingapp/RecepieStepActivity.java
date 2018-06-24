package com.nagy.zsolt.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nagy.zsolt.bakingapp.data.Constants.Keys;

import static com.nagy.zsolt.bakingapp.data.Constants.Keys.SELECTED_POSITION_KEY;
import static com.nagy.zsolt.bakingapp.data.Constants.Keys.SELECTED_STEP_POSITION_KEY;

public class RecepieStepActivity extends AppCompatActivity {

    String[] recepieStepTitle, recepieStepDescription, recepieStepVideo;
    int stepPosition;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private long mPlayerPosition;
    private boolean getPlayerWhenReady;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepie_step);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        if (intent.getExtras() != null) {
            recepieStepTitle = intent.getExtras().getStringArray("StepTitle");
            recepieStepDescription = intent.getExtras().getStringArray("StepDescription");
            recepieStepVideo = intent.getExtras().getStringArray("StepVideoURI");
            stepPosition = intent.getExtras().getInt("StepPosition");

        }

        if (savedInstanceState != null) {
            mPlayerPosition = savedInstanceState.getLong(SELECTED_POSITION_KEY);
            stepPosition = savedInstanceState.getInt(SELECTED_STEP_POSITION_KEY);
        }

        mPlayerView = (SimpleExoPlayerView) findViewById(R.id.playerView_activity);

        final TextView recepieStepTitleTV = (TextView) findViewById(R.id.recepieStepTitle_activity);
        final TextView recepieStepDescriptionTV = (TextView) findViewById(R.id.recepieStepDescription_activity);

        Button prevBtn = findViewById(R.id.previousStep_activity);
        Button nextBtn = findViewById(R.id.nextStep_activity);

        prevBtn.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);

        if (recepieStepTitle != null) {
            prevBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
            recepieStepTitleTV.setText(recepieStepTitle[stepPosition]);
            recepieStepDescriptionTV.setText(recepieStepDescription[stepPosition]);
            uri = Uri.parse(recepieStepVideo[stepPosition]);
            initializePlayer(uri);
        }

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increment position as long as the index remains <= the size of the image ids list
                if (stepPosition > 0) stepPosition--;
                // Set the image resource to the new list item
                recepieStepTitleTV.setText(recepieStepTitle[stepPosition]);
                recepieStepDescriptionTV.setText(recepieStepDescription[stepPosition]);
                uri = Uri.parse(recepieStepVideo[stepPosition]);
                initializePlayer(uri);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increment position as long as the index remains <= the size of the image ids list
                if (stepPosition < recepieStepTitle.length - 1) stepPosition++;
                // Set the image resource to the new list item
                recepieStepTitleTV.setText(recepieStepTitle[stepPosition]);
                recepieStepDescriptionTV.setText(recepieStepDescription[stepPosition]);
                uri = Uri.parse(recepieStepVideo[stepPosition]);
                initializePlayer(uri);
            }
        });

    }


    /**
     * Initialize ExoPlayer.
     *
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        // Create an instance of the ExoPlayer.
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector, loadControl);
        mPlayerView.setPlayer(mExoPlayer);
        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(getApplicationContext(), "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                getApplicationContext(), userAgent), new DefaultExtractorsFactory(), null, null);

        mExoPlayer.prepare(mediaSource);
        if (mPlayerPosition != C.TIME_UNSET) {
            mExoPlayer.seekTo(mPlayerPosition);
            mExoPlayer.setPlayWhenReady(getPlayerWhenReady);
        }
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getContentPosition();
            getPlayerWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            if (uri != null) {
                initializePlayer(uri);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if (uri != null) {
                initializePlayer(uri);
            }
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(SELECTED_POSITION_KEY, mPlayerPosition);
        outState.putInt(SELECTED_STEP_POSITION_KEY, stepPosition);
        outState.putBoolean("state", getPlayerWhenReady);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SELECTED_POSITION_KEY)) {
                mPlayerPosition = savedInstanceState.getLong(SELECTED_POSITION_KEY);
            }
            if (savedInstanceState.containsKey(SELECTED_STEP_POSITION_KEY)) {
                stepPosition = savedInstanceState.getInt(SELECTED_STEP_POSITION_KEY);
            }
            if (savedInstanceState.containsKey("state")) {
                getPlayerWhenReady = savedInstanceState.getBoolean("state");
            }
        }
    }
}
