package com.namazed.testmusicplayer;

import android.app.Application;

import com.namazed.testmusicplayer.api.RetrofitService;
import com.namazed.testmusicplayer.api.SearchClient;

import timber.log.Timber;


public class TestMusicPlayerApplication extends Application {

    public static final String NAME_ARTIST = "com.namazed.testmusicplayer.main_screen.NAME_ARTIST";
    public static final String NAME_SONG = "com.namazed.testmusicplayer.main_screen.NAME_SONG";
    public static final String URL_IMAGE = "com.namazed.testmusicplayer.main_screen.URL_IMAGE";
    public static final String URL_SONG = "com.namazed.testmusicplayer.main_screen.URL_SONG";
    public static final String EXTRA_MAP_DATA_OF_SONG =
            "com.namazed.testmusicplayer.main_screen.MAP_DATA_OF_SONG";

    public static final int TIMER_REPEAT = 100;
    public static final int TIMER_DEBOUNCE = 1000;

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
