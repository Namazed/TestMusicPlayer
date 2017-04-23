package com.namazed.testmusicplayer.main_screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.namazed.testmusicplayer.R;
import com.namazed.testmusicplayer.TestMusicPlayerApplication;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainActivity
        extends MvpActivity<MainContract.View, MainContract.Presenter> implements MainContract.View {

    private Toolbar toolbar;
    private EditText searchSongsEditText;
    private RecyclerView listSongsRecyclerView;
    private ProgressBar searchingProgressBar;
    private TextView emptyAnswerTextView;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        searchSongsEditText = (EditText) findViewById(R.id.edit_search);
        disposable = RxTextView.textChangeEvents(searchSongsEditText)
                .filter(textViewTextChangeEvent ->
                        textViewTextChangeEvent.text().toString().trim().length() > 4)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textViewTextChangeEvent ->
                        getPresenter().loadListSongs(textViewTextChangeEvent.text().toString()));

        listSongsRecyclerView = (RecyclerView) findViewById(R.id.recycler_list_songs);
        searchingProgressBar = (ProgressBar) findViewById(R.id.progress_searching);
        emptyAnswerTextView = (TextView) findViewById(R.id.text_empty_answer);
    }

    @NonNull
    @Override
    public MainContract.Presenter createPresenter() {
        return new MainPresenter(((TestMusicPlayerApplication) getApplication()).getSearchClient());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void showProgress(boolean isShow) {
        if (isShow) {
            searchingProgressBar.setVisibility(View.VISIBLE);
        } else {
            searchingProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError() {
        View view = MainActivity.this.getCurrentFocus();
        if (view == null) {
            return;
        }
        Snackbar.make(view, R.string.error_connect_msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyList(boolean isShow) {
        if (isShow) {
            emptyAnswerTextView.setVisibility(View.VISIBLE);
        } else {
            emptyAnswerTextView.setVisibility(View.GONE);
        }
    }
}
