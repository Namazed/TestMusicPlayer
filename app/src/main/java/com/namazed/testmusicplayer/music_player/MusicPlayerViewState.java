package com.namazed.testmusicplayer.music_player;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState;

/**
 * ViewState from Mosby library. Save state view.
 */

class MusicPlayerViewState implements MusicPlayerContract.ViewState {

    private static final String KEY_STATE_VIEW = "state_view";
    private static final String KEY_PROGRESS_MUSIC = "progress_music";
    private static final String KEY_DATA_SOURCE = "data_source";
    private static final String EMPTINESS = "";
    private static final int DEFAULT_VALUE = 0;

    private final int STATE_PLAY = 0;
    private final int STATE_PAUSE = 1;
    private final int STATE_STOP = 2;

    private int state = STATE_PLAY;
    private int progressMusic;
    private String dataSourcePlayer;

    @Override
    public void setStatePause() {
        state = STATE_PAUSE;
    }

    @Override
    public void setStatePlay() {
        state = STATE_PLAY;
    }

    @Override
    public void setStateStop() {
        state = STATE_STOP;
    }

    @Override
    public void setProgressMusic(int progressMusic) {
        this.progressMusic = progressMusic;
    }

    @Override
    public void setDataSourcePlayer(String dataSourcePlayer) {
        this.dataSourcePlayer = dataSourcePlayer;
    }

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        out.putInt(KEY_STATE_VIEW, state);
        out.putInt(KEY_PROGRESS_MUSIC, progressMusic);
        out.putString(KEY_DATA_SOURCE, dataSourcePlayer);
    }

    @Override
    public RestorableViewState<MusicPlayerContract.View> restoreInstanceState(Bundle in) {
        if (in == null) {
            return null;
        }

        state = in.getInt(KEY_STATE_VIEW, STATE_PLAY);
        progressMusic = in.getInt(KEY_PROGRESS_MUSIC, DEFAULT_VALUE);
        dataSourcePlayer = in.getString(KEY_DATA_SOURCE, EMPTINESS);

        return this;
    }

    @Override
    public void apply(MusicPlayerContract.View view, boolean retained) {
        switch (state) {
            case STATE_PAUSE:
                //restore state view, but not pause.
                view.createPlayer(dataSourcePlayer);
                view.startPlayer(progressMusic);
                break;
            case STATE_PLAY:
                view.createPlayer(dataSourcePlayer);
                view.startPlayer(progressMusic);
                break;
            case STATE_STOP:
                view.createPlayer(dataSourcePlayer);
                break;
        }
    }
}
