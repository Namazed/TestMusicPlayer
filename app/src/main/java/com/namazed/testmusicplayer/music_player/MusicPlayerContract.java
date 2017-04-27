package com.namazed.testmusicplayer.music_player;


import android.net.Uri;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface MusicPlayerContract {

    interface Presenter extends MvpPresenter<MusicPlayerContract.View> {

        void convertJsonToMap(String mapJson);

        void onClickStop();

        void onClickPlay();

        void onClickPause();
    }

    interface View extends MvpView {

        void setNameArtist(String artistName);

        void setNameSong(String songName);

        void setWrapperAlbum(Uri artworkUrl100);

        void createPlayer(String previewUrl);

        void stopPlayer();

        void startPlayer();

        void pausePlayer();
    }
}
