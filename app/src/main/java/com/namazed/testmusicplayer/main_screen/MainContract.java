package com.namazed.testmusicplayer.main_screen;


import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.namazed.testmusicplayer.api.models.Song;

import java.util.List;

public interface MainContract {

    interface Presenter extends MvpPresenter<MainContract.View> {
        void loadListSongs(String query);

        void putDataOfSongInMap(Song song);
    }

    interface View extends MvpView {
        void showProgress(boolean isShow);

        void showError();

        void showEmptyList(boolean isShow);

        void showData(List<Song> listSongs);

        void showPlayer(String mapDataOfSong);
    }
}
