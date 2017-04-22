package com.namazed.testmusicplayer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

public class MainActivity
        extends MvpActivity<MainContract.View, MainContract.Presenter> implements MainContract.View {

    private Toolbar toolbar;
    private EditText searchSongsEditText;
    private RecyclerView listSongsRecyclerView;
    private ProgressBar searchingProgressBar;
    private TextView emptyAnswerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchSongsEditText = (EditText) findViewById(R.id.edit_search);
        listSongsRecyclerView = (RecyclerView) findViewById(R.id.recycler_list_songs);
        searchingProgressBar = (ProgressBar) findViewById(R.id.progress_searching);
        emptyAnswerTextView = (TextView) findViewById(R.id.text_empty_answer);
    }

    @NonNull
    @Override
    public MainContract.Presenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void showProgress() {
        searchingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        searchingProgressBar.setVisibility(View.GONE);
    }
}
