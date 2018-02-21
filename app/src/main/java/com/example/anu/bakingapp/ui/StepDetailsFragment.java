package com.example.anu.bakingapp.ui;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anu.bakingapp.R;
import com.example.anu.bakingapp.data.Step;
import com.example.anu.bakingapp.utils.DisplayUtils;
import com.example.anu.bakingapp.utils.NetworkUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.net.MalformedURLException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.anu.bakingapp.utils.Constants.KEY_PAGE;
import static com.example.anu.bakingapp.utils.Constants.KEY_STEP;

public class StepDetailsFragment extends Fragment implements ExoPlayer.EventListener {
    private static final String TAG = StepDetailsFragment.class.getSimpleName();
    private static MediaSessionCompat mediaSession;
    @BindView(R.id.main_media_frame)
    LinearLayout mainMediaFrame;
    private PlaybackStateCompat.Builder statBuilder;
    private Unbinder unbinder;
    @BindView(R.id.txt_description)
    TextView txtDescription;
    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView exoPlayerView;

    private int page;
    private Step step;
    private SimpleExoPlayer exoPlayer;
    private long currentPlayerPos;
    private boolean playWhenReady = true;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    public static StepDetailsFragment newInstance(int page, Step step) {
        StepDetailsFragment stepDetailsMainFragment = new StepDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PAGE, page);
        bundle.putParcelable(KEY_STEP, step);
        stepDetailsMainFragment.setArguments(bundle);
        return stepDetailsMainFragment;
    }


    /**
     * store the instance variables based on the arguments passed
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            page = getArguments().getInt(KEY_PAGE);
            step = getArguments().getParcelable(KEY_STEP);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (null != savedInstanceState) {
            currentPlayerPos = savedInstanceState.getLong("position");
            playWhenReady = savedInstanceState.getBoolean("play_when_ready");
        } else {
            currentPlayerPos = 0;
        }
        populateDetails(step);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != savedInstanceState)
            currentPlayerPos = savedInstanceState.getLong("position");
    }

    /**
     * method to initialize the player
     *
     * @param mediaUri uri of the music sample to play
     */
    private void initializePlayer(Uri mediaUri) {

        //Instantiate a SimpleExoPlayer object using DefaultTrackSelector and DefaultLoadControl.
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
        exoPlayer.seekTo(currentPlayerPos);
        exoPlayerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(playWhenReady);
        exoPlayer.addListener(this);

        //Prepare the MediaSource using DefaultDataSourceFactory and DefaultExtractorsFactory, as well as the Sample URI you passed in.
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getContext(),
                "BakingApp")), new DefaultExtractorsFactory(), null, null);
        exoPlayer.prepare(mediaSource);
    }

    /**
     * method to populate step details
     *
     * @param step
     */
    private void populateDetails(Step step) {
        txtDescription.setText(step.getDescription());
        setVideo();
            /*
             * set default art work
             */
            exoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.exo_controls_play));
            initializeMediaSession();

            try {
                initializePlayer(NetworkUtils.buildVideoUri(step.getVideoURL()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    /**
     * method to determine wether to show exoplayer or not
     */
    private void setVideo() {
        if (step.getVideoURL() == null || step.getVideoURL().equals("")) {
          setNoVideo();
        }
        else {
            setHaveVideo();
        }
    }

    /**
     * method to manage ui, when there is video, based on the orientation
     */
    private void setHaveVideo() {
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            /**
             * In the landscape mode,
             * make exo layer full screen
             * make window as full screen
             * hide description
             */
            ViewGroup.LayoutParams layoutParams =
                    exoPlayerView.getLayoutParams();
            layoutParams.width = DisplayUtils.getDeviceWidth(getActivity());
            layoutParams.height = DisplayUtils.getDeviceHeight(getActivity());
            exoPlayerView.setLayoutParams(layoutParams);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            exoPlayerView.setVisibility(View.VISIBLE);
            txtDescription.setVisibility(View.GONE);
        }
        else {
            /**
         * In the portrait mode
         */
            ViewGroup.LayoutParams layoutParams =
                    exoPlayerView.getLayoutParams();
            layoutParams.width = DisplayUtils.getDeviceWidth(getActivity());
            layoutParams.height = (int) (9.0f / 16.0f * layoutParams.width);
            exoPlayerView.setLayoutParams(layoutParams);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            txtDescription.setVisibility(View.VISIBLE);
            exoPlayerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * method to display ui when there is no video available
     */
    private void setNoVideo() {
        txtDescription.setVisibility(View.VISIBLE);
        exoPlayerView.setVisibility(View.GONE);
    }


    /**
     * method to initialize the media session. it should be MediaSessionCompat
     * set flags for external clients, set the available actions you want to support, and then start the session
     * <p>
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        //create the media session object
        mediaSession = new MediaSessionCompat(getActivity(), TAG);

        //enable callbacks from media buttons and transport controls
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS | MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);

        //do not let the media button start the player when app is not visible
        mediaSession.setMediaButtonReceiver(null);

        /*
         * set initial play back state to auto play so that media buttons can start the player
         */
        statBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(statBuilder.build());

        /*
         * {@link MySessionCallbacks} has methods that handles callbacks from media controller
         */
        mediaSession.setCallback(new MySessionCallbacks());

        /*
         * start the media session since the activity is alive
         */
        mediaSession.setActive(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        releasePlayer();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     *
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            statBuilder.setState(PlaybackStateCompat.STATE_PLAYING, exoPlayer.getCurrentPosition(), 1f);
        } else {
            statBuilder.setState(PlaybackStateCompat.STATE_PAUSED, exoPlayer.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(statBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * media session callbacks where all the external clients control the player
     */
    private class MySessionCallbacks extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            exoPlayer.seekTo(0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        currentPlayerPos = exoPlayer.getCurrentPosition();
    }

    /**
     * stop and release the player when the Activity is destroyed.
     */
    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
        if (mediaSession != null)
            mediaSession.setActive(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != exoPlayer) {
            currentPlayerPos = exoPlayer.getCurrentPosition();
            playWhenReady = exoPlayer.getPlayWhenReady();
        }
        releasePlayer();
        try {
            initializePlayer(NetworkUtils.buildVideoUri(step.getVideoURL()));
            initializeMediaSession();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("position", currentPlayerPos);
        outState.putBoolean("play_when_ready", playWhenReady);
    }

    /**
     * method called when configuration changes
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setVideo();
    }

}