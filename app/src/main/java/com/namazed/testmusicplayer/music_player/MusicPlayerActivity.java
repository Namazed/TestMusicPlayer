package com.namazed.testmusicplayer.music_player;


import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.viewstate.MvpViewStateActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.namazed.testmusicplayer.R;
import com.namazed.testmusicplayer.TestMusicPlayerApplication;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class MusicPlayerActivity
        extends MvpViewStateActivity<
        MusicPlayerContract.View, MusicPlayerContract.Presenter, MusicPlayerContract.ViewState>
        implements MusicPlayerContract.View {

    private Handler progressSongHandler = new Handler();

    private TextView nameArtistTextView;
    private TextView nameSongTextView;
    private ImageView wrapperAlbumImageView;
    private SeekBar progressSongSeekBar;
    private ImageButton playSongImageButton;
    private ImageButton pauseSongImageButton;
    private MediaPlayer mediaPlayer;
    private CompositeDisposable compositeDisposable;
    private int durationSong;
    private ImageButton stopSongImageButton;
    private String dataSourcePlayer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        compositeDisposable = new CompositeDisposable();

        initViews();
        getMapDataOfSong();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compositeDisposable = new CompositeDisposable();
        if (progressSongHandler == null) {
            progressSongHandler = new Handler();
        }
    }

    private void getMapDataOfSong() {
        Intent intent = getIntent();
        String mapJson = intent.getStringExtra(TestMusicPlayerApplication.EXTRA_MAP_DATA_OF_SONG);
        getPresenter().convertJsonToMap(mapJson);
    }

    private void initViews() {
        nameArtistTextView = (TextView) findViewById(R.id.text_name_artist);
        nameSongTextView = (TextView) findViewById(R.id.text_name_song);
        wrapperAlbumImageView = (ImageView) findViewById(R.id.image_wrapper_album);
        progressSongSeekBar = (SeekBar) findViewById(R.id.seek_progress_song);
        progressSongSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                progressSongHandler.removeCallbacks(updateProgressBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressSongHandler.removeCallbacks(updateProgressBar);
                if (mediaPlayer != null) {
                    viewState.setProgressMusic(seekBar.getProgress());
                    setProgressMusic(seekBar.getProgress());
                }

                updateProgressSong();
            }
        });

        playSongImageButton = (ImageButton) findViewById(R.id.button_play);
        pauseSongImageButton = (ImageButton) findViewById(R.id.button_pause);
        stopSongImageButton = (ImageButton) findViewById(R.id.button_stop);

        compositeDisposable.add(RxView.clicks(stopSongImageButton)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next -> {
                    if (mediaPlayer != null) {
                        getPresenter().onClickStop();
                    }
                })
        );
        compositeDisposable.add(RxView.clicks(playSongImageButton)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next -> {
                    if (mediaPlayer != null) {
                        getPresenter().onClickPlay(mediaPlayer.getCurrentPosition());
                    }
                })
        );
        compositeDisposable.add(RxView.clicks(pauseSongImageButton)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next -> {
                    if (mediaPlayer != null) {
                        getPresenter().onClickPause(mediaPlayer.getCurrentPosition());
                    }
                })
        );
    }

    private void setProgressMusic(int progress) {
        mediaPlayer.seekTo(progress);
    }

    @NonNull
    @Override
    public MusicPlayerContract.Presenter createPresenter() {
        return new MusicPlayerPresenter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressSongHandler != null) {
            progressSongHandler.removeCallbacks(updateProgressBar);
            progressSongHandler = null;
        }

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }

        if (progressSongHandler != null) {
            progressSongHandler.removeCallbacks(updateProgressBar);
            progressSongHandler = null;
        }

        progressSongSeekBar.setOnSeekBarChangeListener(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }

        if (progressSongHandler != null) {
            progressSongHandler.removeCallbacks(updateProgressBar);
            progressSongHandler = null;
        }

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer = null;
        }

        progressSongSeekBar.setOnSeekBarChangeListener(null);
    }

    @Override
    public void setNameArtist(String artistName) {
        nameArtistTextView.setText(artistName);
    }

    @Override
    public void setNameSong(String songName) {
        nameSongTextView.setText(songName);
    }

    @Override
    public void setWrapperAlbum(Uri artworkUrl100) {
        Picasso.with(this)
                .load(artworkUrl100)
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .into(wrapperAlbumImageView);
    }

    @Override
    public void createPlayer(String previewUrl) {
        try {
            dataSourcePlayer = previewUrl;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(previewUrl);
            mediaPlayer.prepare();
            durationSong = mediaPlayer.getDuration();
            progressSongSeekBar.setMax(durationSong);
            progressSongSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            pauseSongImageButton.setEnabled(false);
            stopSongImageButton.setEnabled(false);
        } catch (IOException e) {
            Timber.d(e, e.getMessage());
            Toast.makeText(this, R.string.error_msg_problem_with_playing, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void stopPlayer() {
        viewState.setStateStop();
        mediaPlayer.stop();
        mediaPlayer = null;
        playSongImageButton.setEnabled(true);
        pauseSongImageButton.setEnabled(false);
        stopSongImageButton.setEnabled(false);
        progressSongHandler.removeCallbacks(updateProgressBar);
        progressSongSeekBar.setProgress(0);
    }

    @Override
    public void startPlayer(int progress) {
        viewState.setStatePlay();
        viewState.setDataSourcePlayer(dataSourcePlayer);
        mediaPlayer.start();
        setProgressMusic(progress);
        progressSongSeekBar.setProgress(mediaPlayer.getCurrentPosition());
        playSongImageButton.setEnabled(false);
        pauseSongImageButton.setEnabled(true);
        stopSongImageButton.setEnabled(true);

        updateProgressSong();
    }

    @Override
    public void pausePlayer(int progress) {
        viewState.setStatePause();
        viewState.setProgressMusic(progress);
        viewState.setDataSourcePlayer(dataSourcePlayer);
        progressSongSeekBar.setProgress(progress);
        setProgressMusic(progress);
        mediaPlayer.pause();
        progressSongHandler.removeCallbacks(updateProgressBar);
        pauseSongImageButton.setEnabled(false);
        playSongImageButton.setEnabled(true);
    }

    private void updateProgressSong() {
        progressSongHandler.postDelayed(updateProgressBar, TimeUnit.MILLISECONDS.toMillis(
                TestMusicPlayerApplication.TIMER_REPEAT
        ));
    }

    /**
     * Update progress seekBar from mediaPlayer currentPosition
     */
    private Runnable updateProgressBar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                viewState.setProgressMusic(mediaPlayer.getCurrentPosition());
                progressSongSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }

            progressSongHandler.postDelayed(this, TimeUnit.MILLISECONDS.toMillis(
                    TestMusicPlayerApplication.TIMER_REPEAT
            ));
        }
    };

    @NonNull
    @Override
    public MusicPlayerContract.ViewState createViewState() {
        return new MusicPlayerViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        //don't need
    }
}
