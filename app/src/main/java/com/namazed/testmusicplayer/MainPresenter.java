package com.namazed.testmusicplayer;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import io.reactivex.disposables.CompositeDisposable;


public class MainPresenter
        extends MvpBasePresenter<MainContract.View> implements MainContract.Presenter {

    private CompositeDisposable compositeDisposable;

    @Override
    public void attachView(MainContract.View view) {
        super.attachView(view);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadListSongs() {
        if (!isViewAttached()) {
            return;
        }

    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
    }
}
