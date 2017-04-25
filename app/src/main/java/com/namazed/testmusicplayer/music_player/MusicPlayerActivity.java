package com.namazed.testmusicplayer.music_player;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

public class MusicPlayerActivity
        extends MvpActivity<MusicPlayerContract.View, MusicPlayerContract.Presenter>
        implements MusicPlayerContract.View {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @NonNull
    @Override
    public MusicPlayerContract.Presenter createPresenter() {
        return new MusicPlayerPresenter();
    }
}
