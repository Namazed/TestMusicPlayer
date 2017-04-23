package com.namazed.testmusicplayer.api.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Song {
    public static TypeAdapter<Song> typeAdapter(Gson gson) {
        return new AutoValue_Song.GsonTypeAdapter(gson);
    }

    @Nullable
    @SerializedName("artistName")
    public abstract String getArtistName();

    @Nullable
    @SerializedName("trackName")
    public abstract String getTrackName();

    @Nullable
    @SerializedName("artworkUrl60")
    public abstract String getArtworkUrl60();

    @Nullable
    @SerializedName("artworkUrl100")
    public abstract String getArtworkUrl100();

    @Nullable
    @SerializedName("previewUrl")
    public abstract String getPreviewUrl();

}