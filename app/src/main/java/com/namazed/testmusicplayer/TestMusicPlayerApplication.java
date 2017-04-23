package com.namazed.testmusicplayer;

import android.app.Application;

import com.namazed.testmusicplayer.api.RetrofitService;
import com.namazed.testmusicplayer.api.SearchClient;

import timber.log.Timber;


public class TestMusicPlayerApplication extends Application {

    private RetrofitService retrofitService;
    private SearchClient searchClient;

    @Override
    public void onCreate() {
        retrofitService = new RetrofitService();
        searchClient = retrofitService.createRetrofitClient(SearchClient.class);
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public SearchClient getSearchClient() {
        return searchClient;
    }
}
