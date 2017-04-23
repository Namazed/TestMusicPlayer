package com.namazed.testmusicplayer.music_player;


import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface MusicPlayerContract {

    interface Presenter extends MvpPresenter<MusicPlayerContract.View> {

    }

    interface View extends MvpView {

    }
}
