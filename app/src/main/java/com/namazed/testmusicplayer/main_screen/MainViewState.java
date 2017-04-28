package com.namazed.testmusicplayer.main_screen;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState;


public class MainViewState implements RestorableViewState<MainContract.View> {

    private static final String KEY_STATE_VIEW = "state_view";
    private static final String KEY_VIEW_TYPE = "state_view";

    private final int STATE_SHOW_LIST_SONG = 0;
    private final int STATE_SHOW_LOADING = 1;
    private final int STATE_SHOW_ERROR = 2;
    private final int STATE_SHOW_EMPTY_LIST = 3;
    private final int GRID_VIEW_TYPE = 0;
    private final int LIST_VIEW_TYPE = 1;

    private int state = STATE_SHOW_LIST_SONG;
    private int viewType = LIST_VIEW_TYPE;

    void setShowListSong() {
        state = STATE_SHOW_LIST_SONG;
    }

    void setShowError() {
        state = STATE_SHOW_ERROR;
    }

    void setShowEmptyList() {
        state = STATE_SHOW_EMPTY_LIST;
    }

    void setShowLoading() {
        state = STATE_SHOW_LOADING;
    }

    void setGridViewType() {
        viewType = GRID_VIEW_TYPE;
    }

    void setListViewType() {
        viewType = LIST_VIEW_TYPE;
    }

    @Override
    public void apply(MainContract.View view, boolean retained) {
        switch (state) {
            case STATE_SHOW_EMPTY_LIST:
                view.showEmptyList(true);
                break;
            case STATE_SHOW_ERROR:
                view.showError();
                break;
            case STATE_SHOW_LOADING:
                view.showProgress(true);
                break;
            case STATE_SHOW_LIST_SONG:
                view.showEmptyList(false);
                view.showProgress(false);
                break;
        }

        switch (viewType) {
            case GRID_VIEW_TYPE:
                view.showGridView();
                break;
            case LIST_VIEW_TYPE:
                view.showListView();
                break;
        }
    }

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        out.putInt(KEY_STATE_VIEW, state);
        out.putInt(KEY_VIEW_TYPE, viewType);
    }

    @Override
    public RestorableViewState<MainContract.View> restoreInstanceState(Bundle in) {
        if (in == null) {
            return null;
        }

        state = in.getInt(KEY_STATE_VIEW, STATE_SHOW_LIST_SONG);
        viewType = in.getInt(KEY_VIEW_TYPE, LIST_VIEW_TYPE);

        return this;
    }
}
