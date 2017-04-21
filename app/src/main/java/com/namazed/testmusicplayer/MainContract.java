package com.namazed.testmusicplayer;


import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface MainContract {

    interface Presenter extends MvpPresenter<MainContract.View> {

    }

    interface View extends MvpView {

    }
}
