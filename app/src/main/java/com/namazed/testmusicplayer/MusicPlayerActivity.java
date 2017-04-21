package com.namazed.testmusicplayer;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

public class MusicPlayerActivity
        extends MvpActivity<MusicPlayerContract.View, MusicPlayerContract.Presenter>
        implements MusicPlayerContract.View {

    @NonNull
    @Override
    public MusicPlayerContract.Presenter createPresenter() {
        return new MusicPlayerPresenter();
    }
}
