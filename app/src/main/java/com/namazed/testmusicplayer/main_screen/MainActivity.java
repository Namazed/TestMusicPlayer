package com.namazed.testmusicplayer.main_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.namazed.testmusicplayer.R;
import com.namazed.testmusicplayer.TestMusicPlayerApplication;
import com.namazed.testmusicplayer.api.models.Song;
import com.namazed.testmusicplayer.main_screen.adapter.SongRecyclerAdapter;
import com.namazed.testmusicplayer.music_player.MusicPlayerActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainActivity
        extends MvpActivity<MainContract.View, MainContract.Presenter> implements MainContract.View {

    public static final String EXTRA_SONG = "com.namazed.testmusicplayer.main_screen.MESSAGE";

    private Toolbar toolbar;
    private EditText searchSongsEditText;
    private RecyclerView listSongsRecyclerView;
    private ProgressBar searchingProgressBar;
    private TextView emptyAnswerTextView;
    private Disposable disposable;
    private SongRecyclerAdapter.SongAdapterListener songAdapterListener;
    private SongRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initAdapterAndSongListener();
    }

    private void initAdapterAndSongListener() {
        songAdapterListener = (view, song) -> {
            Intent intent = new Intent(this, MusicPlayerActivity.class);
            Gson gson = new Gson();
            String songString = gson.toJson(song);
            intent.putExtra("com.namazed.testmusicplayer.main_screen.SONG", songString);
            startActivity(intent);
        };
        adapter = new SongRecyclerAdapter(getApplication(), new ArrayList<>(), songAdapterListener);
        listSongsRecyclerView.setAdapter(adapter);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        searchSongsEditText = (EditText) findViewById(R.id.edit_search);
        disposable = getEditTextDisposable().subscribe(textViewTextChangeEvent ->
                getPresenter().loadListSongs(textViewTextChangeEvent.text().toString()));

        listSongsRecyclerView = (RecyclerView) findViewById(R.id.recycler_list_songs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listSongsRecyclerView.setLayoutManager(layoutManager);
        listSongsRecyclerView.setItemAnimator(new DefaultItemAnimator());


        searchingProgressBar = (ProgressBar) findViewById(R.id.progress_searching);
        emptyAnswerTextView = (TextView) findViewById(R.id.text_empty_answer);
    }

    @NonNull
    private Observable<TextViewTextChangeEvent> getEditTextDisposable() {
        return RxTextView.textChangeEvents(searchSongsEditText)
                .filter(textViewTextChangeEvent ->
                        textViewTextChangeEvent.text().toString().trim().length() > 4)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    @Override
    public MainContract.Presenter createPresenter() {
        return new MainPresenter(((TestMusicPlayerApplication) getApplication()).getSearchClient());
    }

    @Override
    protected void onResume() {
        super.onResume();
        disposable = getEditTextDisposable().subscribe(textViewTextChangeEvent ->
                getPresenter().loadListSongs(textViewTextChangeEvent.text().toString()));
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

    @Override
    public void showData(List<Song> listSongs) {
        if (adapter != null) {
            adapter.setData(listSongs);
        }
    }
}
