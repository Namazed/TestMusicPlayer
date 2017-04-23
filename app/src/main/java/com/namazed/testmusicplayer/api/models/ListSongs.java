package com.namazed.testmusicplayer.api.models;


import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class ListSongs {
    public static TypeAdapter<ListSongs> typeAdapter(Gson gson) {
        return new AutoValue_ListSongs.GsonTypeAdapter(gson);
    }

    @Nullable
    @SerializedName("resultCount")
    public abstract Integer getListSize();

    @Nullable
    @SerializedName("results")
    public abstract List<Song> getListSongs();

    @Nullable
    @SerializedName("errorMessage")
    public abstract String getErrorMessage();
}
