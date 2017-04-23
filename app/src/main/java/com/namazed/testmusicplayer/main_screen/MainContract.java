package com.namazed.testmusicplayer.main_screen;


import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface MainContract {

    interface Presenter extends MvpPresenter<MainContract.View> {
        void loadListSongs(String query);
    }

    interface View extends MvpView {
        void showProgress(boolean isShow);

        void showError();

        void showEmptyList(boolean isShow);
    }
}
