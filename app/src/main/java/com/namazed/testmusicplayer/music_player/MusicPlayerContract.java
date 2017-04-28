package com.namazed.testmusicplayer.music_player;


import android.net.Uri;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState;

public interface MusicPlayerContract {

    interface Presenter extends MvpPresenter<MusicPlayerContract.View> {
        void convertJsonToMap(String mapJson);

        void onClickStop();

        void onClickPlay(int currentPosition);

        void onClickPause(int currentPosition);
    }

    interface View extends MvpView {
        void setNameArtist(String artistName);

        void setNameSong(String songName);

        void setWrapperAlbum(Uri artworkUrl100);

        void createPlayer(String previewUrl);

        void stopPlayer();

        void startPlayer(int progress);

        void pausePlayer(int progress);
    }

    interface ViewState extends RestorableViewState<MusicPlayerContract.View> {
        //save view state
        void setStatePause();

        void setStatePlay();

        void setStateStop();

        //save mediaPlayer currentPosition
        void setProgressMusic(int progressMusic);

        //save Url of music
        void setDataSourcePlayer(String dataSourcePlayer);
    }
}
