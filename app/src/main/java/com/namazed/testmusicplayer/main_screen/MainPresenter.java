package com.namazed.testmusicplayer.main_screen;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.namazed.testmusicplayer.api.SearchClient;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class MainPresenter
        extends MvpBasePresenter<MainContract.View> implements MainContract.Presenter {

    private final static String MEDIA_TYPE = "music";
    public static final int LIST_SIZE_0 = 0;
    private Disposable disposable;
    private final SearchClient searchClient;

    public MainPresenter(SearchClient searchClient) {
        this.searchClient = searchClient;
    }

    @Override
    public void attachView(MainContract.View view) {
        super.attachView(view);
    }

    @Override
    public void loadListSongs(String query) {
        if (isViewAttached()) {
            getView().showProgress(true);
        }

        disposable = searchClient.getListSongs(query, MEDIA_TYPE)
                .retry((integer, throwable) ->
                        throwable instanceof SocketTimeoutException || throwable instanceof UnknownHostException)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listSongs -> {
                    if (listSongs != null && listSongs.getListSize() != null && isViewAttached()) {
                        int listSize = listSongs.getListSize();
                        if (listSize == LIST_SIZE_0) {
                            getView().showEmptyList(true);
                            getView().showProgress(false);
                            // TODO: 23.04.2017 save state emptyList in ViewState for rotation screen
                        } else {
                            // TODO: 23.04.2017 will be call method showData in view
                            Timber.d(String.valueOf(listSongs.getListSize()));
                            getView().showProgress(false);
                        }
                    } else if (listSongs != null && listSongs.getListSize() == null && isViewAttached()) {
                        Timber.w("Bad answer loadListSongs: %s", listSongs.getErrorMessage());
                        getView().showProgress(false);
                        getView().showError();
                    } else if (!isViewAttached()) {
                        // TODO: 23.04.2017 will be save data in ViewState for rotation screen
                    }
                }, throwable -> {
                    Timber.d(throwable, throwable.getMessage());
                    if (isViewAttached()) {
                        getView().showProgress(false);
                        getView().showError();
                    }
                });
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
