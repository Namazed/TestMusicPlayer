package com.namazed.testmusicplayer.main_screen;


import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.namazed.testmusicplayer.api.models.Song;

import java.util.List;

public interface MainContract {

    interface Presenter extends MvpPresenter<MainContract.View> {
        void loadListSongs(String query);

        //choose some datum of song and this put in map
        void putDataOfSongInMap(Song song);
    }

    interface View extends MvpView {
        void showProgress(boolean isShow);

        void showError();

        //show when nothing is found
        void showEmptyList(boolean isShow);

        void showData(List<Song> listSongs);

        //show activity player
        void showPlayer(String mapDataOfSong);

        //show grid layout items sound recyclerView
        void showGridView();

        void showListView();
    }
}
