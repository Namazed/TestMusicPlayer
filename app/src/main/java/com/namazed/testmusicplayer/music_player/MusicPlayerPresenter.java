package com.namazed.testmusicplayer.music_player;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.namazed.testmusicplayer.TestMusicPlayerApplication;

import java.lang.reflect.Type;
import java.util.Map;


public class MusicPlayerPresenter
        extends MvpBasePresenter<MusicPlayerContract.View> implements MusicPlayerContract.Presenter {

    private String previewUrl;

    @Override
    public void convertJsonToMap(String mapJson) {
        if (!isViewAttached()) {
            return;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> mapDataOfSong = gson.fromJson(mapJson, type);
        for (Map.Entry<String, String> stringEntry : mapDataOfSong.entrySet()) {
            switch (stringEntry.getKey()) {
                case TestMusicPlayerApplication.NAME_ARTIST:
                    getView().setNameArtist(stringEntry.getValue());
                    break;
                case TestMusicPlayerApplication.NAME_SONG:
                    getView().setNameSong(stringEntry.getValue());
                    break;
                case TestMusicPlayerApplication.URL_IMAGE:
                    Uri uriImage = Uri.parse(stringEntry.getValue());
                    getView().setWrapperAlbum(uriImage);
                    break;
                case TestMusicPlayerApplication.URL_SONG:
                    previewUrl = stringEntry.getValue();
                    getView().createPlayer(previewUrl);
                    break;
            }
        }
    }

    @Override
    public void onClickStop() {
        if (!isViewAttached()) {
            return;
        }

        getView().stopPlayer();
        getView().createPlayer(previewUrl);
    }

    @Override
    public void onClickPlay(int currentPosition) {
        if (!isViewAttached()) {
            return;
        }

        getView().startPlayer(currentPosition);
    }

    @Override
    public void onClickPause(int currentPosition) {
        if (!isViewAttached()) {
            return;
        }

        getView().pausePlayer(currentPosition);
    }
}
