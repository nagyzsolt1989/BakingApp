package com.nagy.zsolt.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class RecepieStepFragment extends Fragment {

    String[] recepieStepTitle, recepieStepDescription, recepieStepVideo;
    int stepPosition;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    Uri uri;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public RecepieStepFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recepieStepTitle = getArguments().getStringArray("StepTitle");
            recepieStepDescription = getArguments().getStringArray("StepDescription");
            recepieStepVideo = getArguments().getStringArray("StepVideoURI");
            stepPosition = getArguments().getInt("StepPosition");

        }
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.content_recepie_detail, container, false);

        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);

        final TextView recepieStepTitleTV = (TextView) rootView.findViewById(R.id.recepieStepTitle);
        final TextView recepieStepDescriptionTV = (TextView) rootView.findViewById(R.id.recepieStepDescription);

        Button prevBtn = rootView.findViewById(R.id.previousStep);
        Button nextBtn = rootView.findViewById(R.id.nextStep);

        prevBtn.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);

        if(recepieStepTitle != null){
            prevBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
            recepieStepTitleTV.setText(recepieStepTitle[stepPosition]);
            recepieStepDescriptionTV.setText(recepieStepDescription[stepPosition]);
            System.out.println("Ezt fogod látni " + recepieStepVideo[stepPosition]);
            uri = Uri.parse(recepieStepVideo[stepPosition]);
            initializePlayer(uri);
        }

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increment position as long as the index remains <= the size of the image ids list
                if (stepPosition >  0) stepPosition--;
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
                if (stepPosition <  recepieStepTitle.length-1) stepPosition++;
                // Set the image resource to the new list item
                recepieStepTitleTV.setText(recepieStepTitle[stepPosition]);
                recepieStepDescriptionTV.setText(recepieStepDescription[stepPosition]);
                uri = Uri.parse(recepieStepVideo[stepPosition]);
                initializePlayer(uri);
            }
        });

        // Return the rootView
        return rootView;
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "ClassicalMusicQuiz");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
//            mExoPlayer.setPlayWhenReady(true);
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }
}
