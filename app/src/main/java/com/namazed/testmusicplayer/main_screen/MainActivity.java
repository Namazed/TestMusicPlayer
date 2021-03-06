package com.namazed.testmusicplayer.main_screen;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.viewstate.MvpViewStateActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.namazed.testmusicplayer.R;
import com.namazed.testmusicplayer.TestMusicPlayerApplication;
import com.namazed.testmusicplayer.api.models.Song;
import com.namazed.testmusicplayer.main_screen.adapter.SongRecyclerAdapter;
import com.namazed.testmusicplayer.music_player.MusicPlayerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainActivity
        extends MvpViewStateActivity<MainContract.View, MainContract.Presenter, MainContract.ViewState>
        implements MainContract.View {

    private Toolbar toolbar;
    private EditText searchSongsEditText;
    private RecyclerView listSongsRecyclerView;
    private ProgressBar searchingProgressBar;
    private TextView emptyAnswerTextView;
    private Disposable disposable;
    private SongRecyclerAdapter.SongAdapterListener songAdapterListener;
    private SongRecyclerAdapter adapter;

    private Handler handlerMenuItem = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initAdapterAndSongListener();
    }

    @NonNull
    @Override
    public MainContract.Presenter createPresenter() {
        return new MainPresenter(((TestMusicPlayerApplication) getApplication()).getSearchClient());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (handlerMenuItem == null) {
            handlerMenuItem = new Handler();
        }

        disposable = getEditTextObservable().subscribe(textViewTextChangeEvent ->
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

        if (handlerMenuItem != null) {
            handlerMenuItem.removeCallbacksAndMessages(null);
            handlerMenuItem = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_grid_view:
                showGridView();
                return true;
            case R.id.action_list_view:
                showListView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showProgress(boolean isShow) {
        if (isShow) {
            viewState.setStateShowLoading();
            searchingProgressBar.setVisibility(View.VISIBLE);
        } else {
            searchingProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError() {
        viewState.setStateShowError();
        View view = MainActivity.this.getCurrentFocus();
        if (view == null) {
            return;
        }
        Snackbar.make(view, R.string.error_connect_msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyList(boolean isShow) {
        if (isShow) {
            viewState.setStateShowEmptyList();
            emptyAnswerTextView.setVisibility(View.VISIBLE);
        } else {
            emptyAnswerTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showData(List<Song> listSongs) {
        if (adapter != null) {
            viewState.setStateShowListSong();
            adapter.setData(listSongs);
        }
    }

    @Override
    public void showPlayer(String mapDataOfSong) {
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        intent.putExtra(TestMusicPlayerApplication.EXTRA_MAP_DATA_OF_SONG, mapDataOfSong);
        startActivity(intent);
    }

    @Override
    public void showGridView() {
        viewState.setGridViewType();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
            listSongsRecyclerView.setLayoutManager(layoutManager);
            showGridViewMenuItem(false);
        } else {
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
            listSongsRecyclerView.setLayoutManager(layoutManager);
            showGridViewMenuItem(false);
        }
        adapter.setGridViewType(true);
    }

    @Override
    public void showListView() {
        viewState.setListViewType();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listSongsRecyclerView.setLayoutManager(layoutManager);
        showGridViewMenuItem(true);
        adapter.setGridViewType(false);
    }

    @NonNull
    @Override
    public MainContract.ViewState createViewState() {
        return new MainViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    private void initAdapterAndSongListener() {
        songAdapterListener = (view, song) -> getPresenter().putDataOfSongInMap(song);
        adapter = new SongRecyclerAdapter(getApplication(), new ArrayList<>(), songAdapterListener);
        listSongsRecyclerView.setAdapter(adapter);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchSongsEditText = (EditText) findViewById(R.id.edit_search);
        disposable = getEditTextObservable().subscribe(textViewTextChangeEvent ->
                getPresenter().loadListSongs(textViewTextChangeEvent.text().toString()));

        listSongsRecyclerView = (RecyclerView) findViewById(R.id.recycler_list_songs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listSongsRecyclerView.setLayoutManager(layoutManager);
        listSongsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        searchingProgressBar = (ProgressBar) findViewById(R.id.progress_searching);
        emptyAnswerTextView = (TextView) findViewById(R.id.text_empty_answer);
    }

    @NonNull
    private Observable<TextViewTextChangeEvent> getEditTextObservable() {
        return RxTextView.textChangeEvents(searchSongsEditText)
                .filter(textViewTextChangeEvent ->
                        textViewTextChangeEvent.text().toString().trim().length() > 4)
                .debounce(TestMusicPlayerApplication.TIMER_DEBOUNCE, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void showGridViewMenuItem(boolean isShow) {
        MenuItem gridViewItem = toolbar.getMenu().findItem(R.id.action_grid_view);
        MenuItem listViewItem = toolbar.getMenu().findItem(R.id.action_list_view);
        if (gridViewItem == null || listViewItem == null) {
            handlerMenuItem.postDelayed(() -> showGridViewMenuItem(isShow),
                    TimeUnit.MILLISECONDS.toMillis(TestMusicPlayerApplication.TIMER_REPEAT));
        } else {
            if (isShow) {
                gridViewItem.setVisible(true);
                listViewItem.setVisible(false);
            } else {
                gridViewItem.setVisible(false);
                listViewItem.setVisible(true);
            }
        }
    }
}
